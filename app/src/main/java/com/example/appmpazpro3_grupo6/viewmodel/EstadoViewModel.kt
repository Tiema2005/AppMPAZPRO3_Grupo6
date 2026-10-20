package com.example.appmpazpro3_grupo6.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.appmpazpro3_grupo6.repository.EstadoDataStore
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
    init {
        cargarEstado()
    }

    private fun cargarEstado() {
        viewModelScope.launch {
            try {
                delay(800)
                _activo.value = estadoDataStore
                    .obtenerEstado()
                    .first()
            } catch (e: Exception) {
                _activo.value = false
            }
        }
    }

    fun alternarEstado() {
        val estadoActual = _activo.value ?: return
        if (_guardando.value) return
        viewModelScope.launch {
            _guardando.value = true
            _mostrarMensaje.value = false
            try {
                val nuevoEstado = !estadoActual

                estadoDataStore.guardarEstado(nuevoEstado)

                _activo.value = nuevoEstado

                _mostrarMensaje.value = true
                delay(2000)
                _mostrarMensaje.value = false

            } catch (e: Exception) {
                _mostrarMensaje.value = false
            } finally {
                _guardando.value = false
            }
        }
    }
}