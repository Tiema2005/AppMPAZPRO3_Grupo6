
package com.example.appmpazpro3_grupo6.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.appmpazpro3_grupo6.repository.EstadoDataStore
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class EstadoViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val estadoDataStore = EstadoDataStore(application)

    private val _activo = MutableStateFlow<Boolean?>(null)
    val activo: StateFlow<Boolean?> = _activo

    private val _mostrarMensaje = MutableStateFlow(false)
    val mostrarMensaje: StateFlow<Boolean> = _mostrarMensaje

    private val _guardando = MutableStateFlow(false)
    val guardando: StateFlow<Boolean> = _guardando

    private var mensajeJob: Job? = null

    init {
        cargarEstado()
    }

    private fun cargarEstado() {
        viewModelScope.launch {
            try {
                _activo.value = estadoDataStore
                    .obtenerEstado()
                    .first()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _activo.value = false
            }
        }
    }

    fun alternarEstado() {
        val estadoActual = _activo.value ?: return

        if (_guardando.value) return

        _guardando.value = true
        mensajeJob?.cancel()
        _mostrarMensaje.value = false

        viewModelScope.launch {
            try {
                val nuevoEstado = !estadoActual

                estadoDataStore.guardarEstado(nuevoEstado)

                _activo.value = nuevoEstado
                _mostrarMensaje.value = true

                mensajeJob = viewModelScope.launch {
                    delay(2000)
                    _mostrarMensaje.value = false
                }

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _mostrarMensaje.value = false
            } finally {
                _guardando.value = false
            }
        }
    }
}