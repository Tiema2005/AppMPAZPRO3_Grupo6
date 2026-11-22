package com.example.appmpazpro3_grupo6.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appmpazpro3_grupo6.data.remote.MetaEstudioMPAZ
import com.example.appmpazpro3_grupo6.repository.ErrorMetasMPAZ
import com.example.appmpazpro3_grupo6.repository.MetasRepositoryMPAZ
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class MetasViewModel(
    private val repositorio: MetasRepositoryMPAZ = MetasRepositoryMPAZ()
) : ViewModel() {

    private val _metas =
        MutableStateFlow<List<MetaEstudioMPAZ>>(emptyList())

    val metas: StateFlow<List<MetaEstudioMPAZ>> =
        _metas.asStateFlow()

    private val _cargando = MutableStateFlow(false)

    val cargando: StateFlow<Boolean> =
        _cargando.asStateFlow()

    private val _procesando = MutableStateFlow(false)

    val procesando: StateFlow<Boolean> =
        _procesando.asStateFlow()

    private val _mensajeError =
        MutableStateFlow<String?>(null)

    val mensajeError: StateFlow<String?> =
        _mensajeError.asStateFlow()

    private var versionSesion = 0

    fun cargarMetas() {

        if (_cargando.value || _procesando.value) return

        val version = versionSesion

        viewModelScope.launch {

            _cargando.value = true
            _mensajeError.value = null

            try {

                val resultado = repositorio.listar()

                if (version == versionSesion) {
                    _metas.value = resultado
                }

            } catch (e: CancellationException) {
                throw e

            } catch (e: Exception) {
                if (version == versionSesion) {
                    _mensajeError.value = describirError(e)
                }

            } finally {
                if (version == versionSesion) {
                    _cargando.value = false
                }
            }
        }
    }

    fun guardarMeta(
        id: Long?,
        titulo: String,
        descripcion: String,
        completada: Boolean,
        onExito: () -> Unit = {}
    ) {

        if (_procesando.value || _cargando.value) return

        val version = versionSesion

        viewModelScope.launch {

            _procesando.value = true
            _mensajeError.value = null

            try {

                val guardada = if (id == null) {

                    repositorio.crear(
                        titulo = titulo,
                        descripcion = descripcion
                    )

                } else {

                    repositorio.actualizar(
                        id = id,
                        titulo = titulo,
                        descripcion = descripcion,
                        completada = completada
                    )
                }

                if (version == versionSesion) {

                    _metas.value = if (id == null) {

                        listOf(guardada) + _metas.value

                    } else {

                        _metas.value.map { meta ->
                            if (meta.id == id) guardada else meta
                        }
                    }

                    onExito()
                }

            } catch (e: CancellationException) {
                throw e

            } catch (e: Exception) {
                if (version == versionSesion) {
                    _mensajeError.value = describirError(e)
                }

            } finally {
                if (version == versionSesion) {
                    _procesando.value = false
                }
            }
        }
    }

    fun eliminarMeta(
        id: Long,
        onExito: () -> Unit = {}
    ) {

        if (_procesando.value || _cargando.value) return

        val version = versionSesion

        viewModelScope.launch {

            _procesando.value = true
            _mensajeError.value = null

            try {

                repositorio.eliminar(id)

                if (version == versionSesion) {

                    _metas.value = _metas.value.filterNot {
                        it.id == id
                    }

                    onExito()
                }

            } catch (e: CancellationException) {
                throw e

            } catch (e: Exception) {
                if (version == versionSesion) {
                    _mensajeError.value = describirError(e)
                }

            } finally {
                if (version == versionSesion) {
                    _procesando.value = false
                }
            }
        }
    }

    fun limpiar() {
        versionSesion++
        _metas.value = emptyList()
        _cargando.value = false
        _procesando.value = false
        _mensajeError.value = null
    }

    fun limpiarError() {
        _mensajeError.value = null
    }

    private fun describirError(error: Exception): String {
        return when (error) {

            is ErrorMetasMPAZ ->
                error.message ?: "No se pudo guardar la meta."

            is IOException ->
                "No se pudo conectar con Spring Boot."

            else ->
                "Ocurrió un error al gestionar las metas."
        }
    }
}