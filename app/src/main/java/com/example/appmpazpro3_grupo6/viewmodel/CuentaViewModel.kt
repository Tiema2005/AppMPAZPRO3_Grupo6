package com.example.appmpazpro3_grupo6.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.appmpazpro3_grupo6.repository.CuentaEstudiante
import com.example.appmpazpro3_grupo6.repository.CuentaRemotaRepository
import com.example.appmpazpro3_grupo6.repository.ErrorCuentaRemota
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class CuentaViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repositorio = CuentaRemotaRepository()

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

                val estudiante =
                    repositorio.registrarCuenta(
                        nombre = nombre,
                        correo = correo,
                        contrasena = contrasena
                    )

                _cuentaActual.value = estudiante

                onExito(estudiante)

            } catch (e: CancellationException) {
                throw e

            } catch (e: ErrorCuentaRemota) {
                _mensajeError.value = e.message

            } catch (e: IOException) {
                _mensajeError.value =
                    "No se pudo conectar con el servidor. " +
                            "Verifica que Spring Boot esté funcionando."

            } catch (e: Exception) {
                _mensajeError.value =
                    "Ocurrió un error al crear la cuenta."

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

                _cuentaActual.value = estudiante

                onExito(estudiante)

            } catch (e: CancellationException) {
                throw e

            } catch (e: ErrorCuentaRemota) {
                _mensajeError.value = e.message

            } catch (e: IOException) {
                _mensajeError.value =
                    "No se pudo conectar con el servidor. " +
                            "Verifica que Spring Boot esté funcionando."

            } catch (e: Exception) {
                _mensajeError.value =
                    "Ocurrió un error al iniciar sesión."

            } finally {
                _procesando.value = false
            }
        }
    }

    fun cargarPerfil(
        onExito: (CuentaEstudiante) -> Unit = {}
    ) {

        if (_procesando.value) return

        viewModelScope.launch {

            _procesando.value = true
            _mensajeError.value = null

            try {

                val estudiante =
                    repositorio.obtenerPerfil()

                _cuentaActual.value = estudiante

                onExito(estudiante)

            } catch (e: CancellationException) {
                throw e

            } catch (e: ErrorCuentaRemota) {
                _mensajeError.value = e.message

            } catch (e: IOException) {
                _mensajeError.value =
                    "No fue posible consultar el perfil en el servidor."

            } catch (e: Exception) {
                _mensajeError.value =
                    "Ocurrió un error al cargar el perfil."

            } finally {
                _procesando.value = false
            }
        }
    }

    fun cerrarSesion() {

        if (_procesando.value) return

        viewModelScope.launch {

            _procesando.value = true

            try {
                repositorio.cerrarSesion()

            } catch (e: CancellationException) {
                throw e

            } catch (e: Exception) {
                _mensajeError.value =
                    "No se pudo confirmar el cierre de sesión en el servidor."

            } finally {
                _cuentaActual.value = null
                _procesando.value = false
            }
        }
    }

    fun limpiarError() {
        _mensajeError.value = null
    }
}