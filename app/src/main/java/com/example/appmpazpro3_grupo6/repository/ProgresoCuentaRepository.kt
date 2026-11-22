package com.example.appmpazpro3_grupo6.repository

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.security.MessageDigest
import java.util.Locale

private val Context.progresoCuentasDataStore by preferencesDataStore(
    name = "mpaz_progreso_por_cuenta_v1"
)

class ProgresoCuentaRepository(context: Context) {

    private val contexto = context.applicationContext
    private val gson = Gson()

    private val claves = listOf(
        "lenguaje",
        "matematica",
        "ciencias",
        "historia"
    ).flatMap { materia ->
        listOf("facil", "medio", "dificil").map { nivel ->
            "${materia}_${nivel}"
        }
    }

    private fun identificador(correo: String): String {

        val normalizado = correo.trim().lowercase(Locale.ROOT)

        require(normalizado.isNotBlank())

        val bytes = MessageDigest.getInstance("SHA-256")
            .digest(normalizado.toByteArray(Charsets.UTF_8))

        return bytes.joinToString("") {
            (it.toInt() and 255)
                .toString(16)
                .padStart(2, '0')
        }
    }

    private fun claveDatos(correo: String) =
        stringPreferencesKey(
            "progreso_${identificador(correo)}"
        )

    private fun clavePendiente(correo: String) =
        booleanPreferencesKey(
            "pendiente_${identificador(correo)}"
        )

    private fun estadoVacio(): ProgresoEstudiante {
        return ProgresoEstudiante(
            progresoPorNivel = emptyMap(),
            resultados = emptyMap(),
            sesionesPractica = claves.associateWith {
                SesionPractica()
            }
        )
    }

    private fun leerEstado(json: String?): ProgresoEstudiante {

        if (json.isNullOrBlank()) {
            return estadoVacio()
        }

        val guardado = runCatching {
            gson.fromJson(json, ProgresoEstudiante::class.java)
        }.getOrNull() ?: return estadoVacio()

        val contadores = claves.associateWith { id ->
            runCatching {
                (guardado.progresoPorNivel[id] ?: 0).coerceIn(0, 5)
            }.getOrDefault(0)
        }

        val resultados = claves.associateWith { id ->

            (1..5).associateWith { numero ->

                val resultado = runCatching {
                    guardado.resultados[id]?.get(numero)
                }.getOrNull()

                ResultadoEjercicio(
                    intentos = resultado?.intentos
                        ?.coerceAtLeast(0) ?: 0,
                    historial = runCatching {
                        resultado?.historial
                            ?.takeLast(30)
                            .orEmpty()
                    }.getOrDefault(emptyList())
                )
            }
        }

        val sesiones = claves.associateWith { id ->

            val sesion = runCatching {
                guardado.sesionesPractica?.get(id)
            }.getOrNull() ?: SesionPractica()

            SesionPractica(
                numeroEjercicio =
                    sesion.numeroEjercicio.coerceIn(0, 4),
                respuestaSeleccionada =
                    sesion.respuestaSeleccionada,
                aciertos = sesion.aciertos.coerceIn(0, 5),
                terminado = sesion.terminado
            )
        }

        return ProgresoEstudiante(
            progresoPorNivel = contadores,
            resultados = resultados,
            sesionesPractica = sesiones
        )
    }

    private fun leerPreferencias(
        preferencias: Preferences,
        correo: String
    ): ProgresoEstudiante {
        return leerEstado(
            preferencias[claveDatos(correo)]
        )
    }

    private val datosSeguros
        get() = contexto.progresoCuentasDataStore.data
            .catch { error ->
                if (error is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw error
                }
            }

    fun observar(
        correo: String
    ): Flow<ProgresoEstudiante> {
        return datosSeguros.map { preferencias ->
            leerPreferencias(preferencias, correo)
        }
    }

    suspend fun obtener(
        correo: String
    ): ProgresoEstudiante {
        return observar(correo).first()
    }

    suspend fun exportar(
        correo: String
    ): String {
        return gson.toJson(obtener(correo))
    }

    suspend fun importar(
        correo: String,
        datosJson: String
    ) {

        require(
            datosJson.isNotBlank() &&
                    datosJson.length <= 250000
        )

        val validado = leerEstado(datosJson)

        contexto.progresoCuentasDataStore.edit { preferencias ->
            preferencias[claveDatos(correo)] =
                gson.toJson(validado)

            preferencias[clavePendiente(correo)] = false
        }
    }

    suspend fun hayCambiosPendientes(
        correo: String
    ): Boolean {
        return datosSeguros.first()[
            clavePendiente(correo)
        ] ?: false
    }

    suspend fun confirmarSincronizacion(
        correo: String,
        datosEnviados: String
    ) {

        contexto.progresoCuentasDataStore.edit { preferencias ->

            val datosActuales = gson.toJson(
                leerPreferencias(preferencias, correo)
            )

            if (datosActuales == datosEnviados) {
                preferencias[clavePendiente(correo)] = false
            }
        }
    }

    private fun actualizarRespuesta(
        estado: ProgresoEstudiante,
        id: String,
        numero: Int,
        correcta: Boolean
    ): ProgresoEstudiante {

        val anterior = estado.resultadoEjercicio(id, numero)

        val nuevo = ResultadoEjercicio(
            intentos = if (anterior.intentos == Int.MAX_VALUE) {
                Int.MAX_VALUE
            } else {
                anterior.intentos + 1
            },
            historial = (
                    anterior.historial + correcta
                    ).takeLast(30)
        )

        val resultadosNivel = estado.resultados[id]
            .orEmpty()
            .toMutableMap()

        resultadosNivel[numero] = nuevo

        return estado.copy(
            progresoPorNivel = estado.progresoPorNivel +
                    (
                            id to maxOf(
                                estado.progresoPorNivel[id] ?: 0,
                                numero
                            ).coerceIn(0, 5)
                            ),
            resultados = estado.resultados +
                    (id to resultadosNivel)
        )
    }

    suspend fun guardarSesion(
        correo: String,
        asignaturaId: String,
        anterior: SesionPractica,
        siguiente: SesionPractica,
        esCorrecta: Boolean?
    ) {

        if (asignaturaId !in claves) return

        contexto.progresoCuentasDataStore.edit { preferencias ->

            val estado = leerPreferencias(
                preferencias,
                correo
            )

            val sesionActual = estado.sesionesPractica
                ?.get(asignaturaId) ?: SesionPractica()

            if (sesionActual != anterior) {
                return@edit
            }

            val reiniciando =
                anterior.terminado &&
                        siguiente == SesionPractica()

            val nuevoEstado = when {

                reiniciando -> {

                    estado.copy(
                        progresoPorNivel =
                            estado.progresoPorNivel +
                                    (asignaturaId to 0),

                        resultados =
                            estado.resultados -
                                    asignaturaId,

                        sesionesPractica =
                            estado.sesionesPractica.orEmpty() +
                                    (
                                            asignaturaId to
                                                    SesionPractica()
                                            )
                    )
                }

                esCorrecta != null -> {

                    if (
                        anterior.terminado ||
                        anterior.respuestaSeleccionada != null ||
                        siguiente.respuestaSeleccionada == null ||
                        anterior.numeroEjercicio !in 0..4
                    ) {
                        return@edit
                    }

                    val actualizado = actualizarRespuesta(
                        estado = estado,
                        id = asignaturaId,
                        numero = anterior.numeroEjercicio + 1,
                        correcta = esCorrecta
                    )

                    actualizado.copy(
                        sesionesPractica =
                            actualizado.sesionesPractica.orEmpty() +
                                    (asignaturaId to siguiente)
                    )
                }

                else -> {

                    estado.copy(
                        sesionesPractica =
                            estado.sesionesPractica.orEmpty() +
                                    (asignaturaId to siguiente)
                    )
                }
            }

            preferencias[claveDatos(correo)] =
                gson.toJson(nuevoEstado)

            preferencias[clavePendiente(correo)] = true
        }
    }

    suspend fun registrarEjercicioRespondido(
        correo: String,
        id: String,
        ejerciciosRespondidos: Int
    ) {

        if (id !in claves) return

        contexto.progresoCuentasDataStore.edit { preferencias ->

            val estado = leerPreferencias(
                preferencias,
                correo
            )

            val actualizado = estado.copy(
                progresoPorNivel =
                    estado.progresoPorNivel +
                            (
                                    id to maxOf(
                                        estado.progresoPorNivel[id] ?: 0,
                                        ejerciciosRespondidos.coerceIn(0, 5)
                                    )
                                    )
            )

            preferencias[claveDatos(correo)] =
                gson.toJson(actualizado)

            preferencias[clavePendiente(correo)] = true
        }
    }

    suspend fun registrarRespuesta(
        correo: String,
        asignaturaId: String,
        numeroEjercicio: Int,
        esCorrecta: Boolean
    ) {

        if (
            asignaturaId !in claves ||
            numeroEjercicio !in 1..5
        ) {
            return
        }

        contexto.progresoCuentasDataStore.edit { preferencias ->

            val estado = leerPreferencias(
                preferencias,
                correo
            )

            val actualizado = actualizarRespuesta(
                estado = estado,
                id = asignaturaId,
                numero = numeroEjercicio,
                correcta = esCorrecta
            )

            preferencias[claveDatos(correo)] =
                gson.toJson(actualizado)

            preferencias[clavePendiente(correo)] = true
        }
    }
}