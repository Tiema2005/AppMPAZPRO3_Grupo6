package com.example.appmpazpro3_grupo6.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.appmpazpro3_grupo6.repository.EstadoSincronizacionMPAZ
import com.example.appmpazpro3_grupo6.repository.ErrorProgresoRemoto
import com.example.appmpazpro3_grupo6.repository.ProgresoCuentaRepository
import com.example.appmpazpro3_grupo6.repository.ProgresoEstudiante
import com.example.appmpazpro3_grupo6.repository.ProgresoRemotoRepository
import com.example.appmpazpro3_grupo6.repository.SesionPractica
import com.example.appmpazpro3_grupo6.repository.SincronizadorEstadisticasMPAZ
import com.google.gson.Gson
import com.google.gson.JsonParser
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.IOException
import java.util.Locale

@OptIn(ExperimentalCoroutinesApi::class)
class ProgresoViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val local = ProgresoCuentaRepository(application)
    private val remoto = ProgresoRemotoRepository()

    private val estadisticas =
        SincronizadorEstadisticasMPAZ(remoto)

    private val versiones =
        EstadoSincronizacionMPAZ(application)

    private val gson = Gson()

    private val mutexSincronizacion = Mutex()

    private val _cuentaActiva =
        MutableStateFlow<String?>(null)

    val cuentaActiva: StateFlow<String?> =
        _cuentaActiva.asStateFlow()

    val progreso: StateFlow<ProgresoEstudiante> =
        _cuentaActiva
            .flatMapLatest { correo ->

                if (correo == null) {

                    flowOf(ProgresoEstudiante())

                } else {

                    local.observar(correo)
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = ProgresoEstudiante()
            )

    private val _errorGuardado =
        MutableStateFlow<String?>(null)

    val errorGuardado: StateFlow<String?> =
        _errorGuardado.asStateFlow()

    private val _sesionesGuardando =
        MutableStateFlow<Set<String>>(emptySet())

    val sesionesGuardando: StateFlow<Set<String>> =
        _sesionesGuardando.asStateFlow()

    private val _cargandoProgreso =
        MutableStateFlow(false)

    val cargandoProgreso: StateFlow<Boolean> =
        _cargandoProgreso.asStateFlow()

    private val _sincronizando =
        MutableStateFlow(false)

    val sincronizando: StateFlow<Boolean> =
        _sincronizando.asStateFlow()

    private var generacionCuenta = 0L

    private var trabajoInicial: Job? = null

    fun activarCuenta(correo: String?) {

        val normalizado = correo
            ?.trim()
            ?.lowercase(Locale.ROOT)
            ?.takeIf { it.isNotBlank() }

        if (_cuentaActiva.value == normalizado) {
            return
        }

        generacionCuenta++

        trabajoInicial?.cancel()

        _cuentaActiva.value = normalizado
        _errorGuardado.value = null
        _sesionesGuardando.value = emptySet()
        _sincronizando.value = false
        _cargandoProgreso.value = normalizado != null

        if (normalizado == null) {
            return
        }

        val version = generacionCuenta

        trabajoInicial = viewModelScope.launch {

            try {

                sincronizarInternamente(
                    correo = normalizado,
                    version = version,
                    permitirDescarga = true
                )

            } catch (e: CancellationException) {

                throw e

            } catch (e: Exception) {

                if (cuentaVigente(normalizado, version)) {
                    _errorGuardado.value = describirError(e)
                }

            } finally {

                if (cuentaVigente(normalizado, version)) {
                    _cargandoProgreso.value = false
                }
            }
        }
    }

    fun sincronizarAhora() {

        val correo = _cuentaActiva.value ?: return
        val version = generacionCuenta

        viewModelScope.launch {

            try {

                sincronizarInternamente(
                    correo = correo,
                    version = version,
                    permitirDescarga = true
                )

                if (cuentaVigente(correo, version)) {
                    _errorGuardado.value = null
                }

            } catch (e: CancellationException) {

                throw e

            } catch (e: Exception) {

                if (cuentaVigente(correo, version)) {
                    _errorGuardado.value = describirError(e)
                }
            }
        }
    }

    private fun cuentaVigente(
        correo: String,
        version: Long
    ): Boolean {

        return _cuentaActiva.value == correo &&
                generacionCuenta == version
    }

    private fun validarDetalle(datosJson: String) {

        val objeto = JsonParser
            .parseString(datosJson)
            .asJsonObject

        require(
            objeto.has("progresoPorNivel") &&
                    objeto.get("progresoPorNivel").isJsonObject &&
                    objeto.has("resultados") &&
                    objeto.get("resultados").isJsonObject &&
                    objeto.has("sesionesPractica") &&
                    objeto.get("sesionesPractica").isJsonObject
        ) {
            "El servidor devolvió un progreso no válido."
        }
    }

    private fun leerEstadoConfirmado(
        datosJson: String
    ): ProgresoEstudiante {

        validarDetalle(datosJson)

        return gson.fromJson(
            datosJson,
            ProgresoEstudiante::class.java
        ) ?: throw IllegalStateException(
            "No se pudo interpretar el progreso guardado."
        )
    }

    private suspend fun sincronizarInternamente(
        correo: String,
        version: Long,
        permitirDescarga: Boolean
    ) {

        mutexSincronizacion.withLock {

            if (!cuentaVigente(correo, version)) {
                return@withLock
            }

            _sincronizando.value = true

            try {

                val pendiente =
                    local.hayCambiosPendientes(correo)

                if (!pendiente && !permitirDescarga) {
                    return@withLock
                }

                val datosRemotos = remoto.obtenerDetalle()

                if (!cuentaVigente(correo, version)) {
                    return@withLock
                }

                val datosLocales = local.exportar(correo)

                val versionRemota = datosRemotos?.let {
                    versiones.calcularHash(it)
                }

                val versionBase =
                    versiones.obtenerVersion(correo)

                var estadoConfirmado: ProgresoEstudiante? = null

                if (pendiente) {

                    val versionLocal =
                        versiones.calcularHash(datosLocales)

                    if (
                        datosRemotos != null &&
                        versionRemota == versionLocal
                    ) {

                        versiones.guardarVersion(
                            correo,
                            versionRemota
                        )

                        local.confirmarSincronizacion(
                            correo,
                            datosLocales
                        )

                        estadoConfirmado =
                            leerEstadoConfirmado(datosLocales)

                    } else {

                        if (versionRemota != versionBase) {

                            throw IllegalStateException(
                                "Hay cambios diferentes en el servidor. " +
                                        "No sobrescribimos tu progreso local."
                            )
                        }

                        val guardado = remoto.guardarDetalle(
                            datosLocales
                        )

                        if (!cuentaVigente(correo, version)) {
                            return@withLock
                        }

                        versiones.guardarVersion(
                            correo,
                            versiones.calcularHash(guardado)
                        )

                        local.confirmarSincronizacion(
                            correo,
                            datosLocales
                        )

                        estadoConfirmado =
                            leerEstadoConfirmado(guardado)
                    }

                } else if (permitirDescarga) {

                    if (datosRemotos == null) {

                        local.importar(
                            correo,
                            gson.toJson(
                                ProgresoEstudiante(
                                    progresoPorNivel = emptyMap(),
                                    resultados = emptyMap(),
                                    sesionesPractica = emptyMap()
                                )
                            )
                        )

                        versiones.guardarVersion(
                            correo,
                            null
                        )

                    } else {

                        validarDetalle(datosRemotos)

                        local.importar(
                            correo,
                            datosRemotos
                        )

                        versiones.guardarVersion(
                            correo,
                            versionRemota
                        )
                    }

                    estadoConfirmado = local.obtener(correo)
                }

                if (
                    estadoConfirmado != null &&
                    cuentaVigente(correo, version)
                ) {

                    estadisticas.sincronizar(
                        estadoConfirmado
                    )
                }

            } finally {

                if (cuentaVigente(correo, version)) {
                    _sincronizando.value = false
                }
            }
        }
    }

    fun guardarSesion(
        asignaturaId: String,
        anterior: SesionPractica,
        siguiente: SesionPractica,
        esCorrecta: Boolean?
    ) {

        val correo = _cuentaActiva.value ?: return
        val version = generacionCuenta

        if (
            asignaturaId in _sesionesGuardando.value ||
            _cargandoProgreso.value
        ) {
            return
        }

        _sesionesGuardando.value =
            _sesionesGuardando.value + asignaturaId

        _errorGuardado.value = null

        viewModelScope.launch {

            var guardadoLocal = false

            try {

                local.guardarSesion(
                    correo = correo,
                    asignaturaId = asignaturaId,
                    anterior = anterior,
                    siguiente = siguiente,
                    esCorrecta = esCorrecta
                )

                guardadoLocal = true

            } catch (e: CancellationException) {

                throw e

            } catch (e: Exception) {

                if (cuentaVigente(correo, version)) {

                    _errorGuardado.value =
                        "No se pudo guardar la práctica local."
                }

            } finally {

                if (cuentaVigente(correo, version)) {

                    _sesionesGuardando.value =
                        _sesionesGuardando.value - asignaturaId
                }
            }

            if (
                guardadoLocal &&
                cuentaVigente(correo, version)
            ) {

                try {

                    sincronizarInternamente(
                        correo = correo,
                        version = version,
                        permitirDescarga = false
                    )

                } catch (e: CancellationException) {

                    throw e

                } catch (e: Exception) {

                    if (cuentaVigente(correo, version)) {
                        _errorGuardado.value = describirError(e)
                    }
                }
            }
        }
    }

    fun registrarEjercicio(
        asignaturaId: String,
        cantidadCompletada: Int
    ) {

        val correo = _cuentaActiva.value ?: return
        val version = generacionCuenta

        viewModelScope.launch {

            try {

                local.registrarEjercicioRespondido(
                    correo = correo,
                    id = asignaturaId,
                    ejerciciosRespondidos = cantidadCompletada
                )

                if (cuentaVigente(correo, version)) {

                    sincronizarInternamente(
                        correo = correo,
                        version = version,
                        permitirDescarga = false
                    )
                }

            } catch (e: CancellationException) {

                throw e

            } catch (e: Exception) {

                if (cuentaVigente(correo, version)) {
                    _errorGuardado.value = describirError(e)
                }
            }
        }
    }

    fun registrarRespuesta(
        asignaturaId: String,
        numeroEjercicio: Int,
        esCorrecta: Boolean
    ) {

        val correo = _cuentaActiva.value ?: return
        val version = generacionCuenta

        viewModelScope.launch {

            try {

                local.registrarRespuesta(
                    correo = correo,
                    asignaturaId = asignaturaId,
                    numeroEjercicio = numeroEjercicio,
                    esCorrecta = esCorrecta
                )

                if (cuentaVigente(correo, version)) {

                    sincronizarInternamente(
                        correo = correo,
                        version = version,
                        permitirDescarga = false
                    )
                }

            } catch (e: CancellationException) {

                throw e

            } catch (e: Exception) {

                if (cuentaVigente(correo, version)) {
                    _errorGuardado.value = describirError(e)
                }
            }
        }
    }

    fun limpiarError() {
        _errorGuardado.value = null
    }

    private fun describirError(
        error: Exception
    ): String {

        return when (error) {

            is ErrorProgresoRemoto ->
                error.message
                    ?: "No se pudo sincronizar el progreso."

            is IllegalStateException ->
                error.message
                    ?: "Se detectó un conflicto de sincronización."

            is IOException ->
                "El progreso está guardado localmente, " +
                        "pero no se pudo sincronizar con el servidor."

            else ->
                "No fue posible sincronizar el progreso. " +
                        "Puedes intentar nuevamente."
        }
    }
}