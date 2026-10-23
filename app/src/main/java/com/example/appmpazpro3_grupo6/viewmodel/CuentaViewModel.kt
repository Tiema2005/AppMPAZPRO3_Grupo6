package com.example.appmpazpro3_grupo6.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.appmpazpro3_grupo6.repository.CuentaEstudiante
import com.example.appmpazpro3_grupo6.repository.CuentaLocalRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CuentaViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repositorio = CuentaLocalRepository(
        application.applicationContext
    )

    private val _cuentaActual =
        MutableStateFlow<CuentaEstudiante?>(null)

    val cuentaActual: StateFlow<CuentaEstudiante?> =
        _cuentaActual.asStateFlow()

    private val _procesando = MutableStateFlow(false)

    val procesando: StateFlow<Boolean> =
        _procesando.asStateFlow()

    private val _mensajeError =
        MutableStateFlow<String?>(null)

    val mensajeError: StateFlow<String?> =
        _mensajeError.asStateFlow()

    fun registrar(
        nombre: String,
        correo: String,
        contrasena: String,
        curso: String,
        onExito: (CuentaEstudiante) -> Unit
    ) {
        if (_procesando.value) return

        viewModelScope.launch {

            _procesando.value = true
            _mensajeError.value = null

            try {

                val registrada =
                    repositorio.registrarCuenta(
                        nombre = nombre,
                        correo = correo,
                        contrasena = contrasena,
                        curso = curso
                    )

                if (registrada) {

                    val estudiante = CuentaEstudiante(
                        nombre = nombre.trim(),
                        correo = correo.trim().lowercase(),
                        curso = curso
                    )

                    _cuentaActual.value = estudiante

                    onExito(estudiante)

                } else {

                    _mensajeError.value =
                        "Ya existe una cuenta registrada " +
                                "en este dispositivo."
                }

            } catch (e: CancellationException) {
                throw e

            } catch (e: Exception) {

                _mensajeError.value =
                    "No se pudo guardar la cuenta. " +
                            "Inténtalo nuevamente."

            } finally {
                _procesando.value = false
            }
        }
    }

    fun iniciarSesion(
        correo: String,
        contrasena: String,
        onExito: (CuentaEstudiante) -> Unit
    ) {
        if (_procesando.value) return

        viewModelScope.launch {

            _procesando.value = true
            _mensajeError.value = null

            try {

                val estudiante =
                    repositorio.iniciarSesion(
                        correo = correo,
                        contrasena = contrasena
                    )

                if (estudiante != null) {

                    _cuentaActual.value = estudiante

                    onExito(estudiante)

                } else {

                    _mensajeError.value =
                        "Correo o contraseña incorrectos."
                }

            } catch (e: CancellationException) {
                throw e

            } catch (e: Exception) {

                _mensajeError.value =
                    "No se pudo iniciar sesión. " +
                            "Inténtalo nuevamente."

            } finally {
                _procesando.value = false
            }
        }
    }

    fun cerrarSesion() {
        _cuentaActual.value = null
        _mensajeError.value = null
    }

    fun limpiarError() {
        _mensajeError.value = null
    }
}
