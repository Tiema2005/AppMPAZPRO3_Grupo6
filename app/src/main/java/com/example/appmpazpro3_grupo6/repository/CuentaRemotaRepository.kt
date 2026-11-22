package com.example.appmpazpro3_grupo6.repository

import com.example.appmpazpro3_grupo6.data.remote.ClienteEstudiantesMPAZ
import com.example.appmpazpro3_grupo6.data.remote.EstudianteRespuestaMPAZ
import com.example.appmpazpro3_grupo6.data.remote.LoginEstudianteMPAZ
import com.example.appmpazpro3_grupo6.data.remote.RegistroEstudianteMPAZ
import com.example.appmpazpro3_grupo6.data.remote.SesionEstudiantesMPAZ

class ErrorCuentaRemota(
    message: String
) : Exception(message)

class CuentaRemotaRepository(
    private val cliente: ClienteEstudiantesMPAZ =
        SesionEstudiantesMPAZ.cliente
) {

    suspend fun registrarCuenta(
        nombre: String,
        correo: String,
        contrasena: String
    ): CuentaEstudiante {

        val respuesta = cliente.api.registrar(
            RegistroEstudianteMPAZ(
                nombre = nombre.trim(),
                correo = correo.trim().lowercase(),
                contrasena = contrasena
            )
        )

        if (!respuesta.isSuccessful) {
            val mensaje = when (respuesta.code()) {
                400 -> "Revisa los datos de registro."
                409 -> "Este correo ya está registrado."
                else -> "No se pudo registrar la cuenta. Código ${respuesta.code()}."
            }

            throw ErrorCuentaRemota(mensaje)
        }

        if (respuesta.body() == null) {
            throw ErrorCuentaRemota(
                "El servidor no devolvió los datos de registro."
            )
        }

        return iniciarSesion(
            correo = correo,
            contrasena = contrasena
        )
    }

    suspend fun iniciarSesion(
        correo: String,
        contrasena: String
    ): CuentaEstudiante {

        cliente.limpiarSesion()

        val respuesta = cliente.api.iniciarSesion(
            LoginEstudianteMPAZ(
                correo = correo.trim().lowercase(),
                contrasena = contrasena
            )
        )

        if (!respuesta.isSuccessful) {
            val mensaje = when (respuesta.code()) {
                400, 401, 403 ->
                    "Correo o contraseña incorrectos."

                else ->
                    "No se pudo iniciar sesión. Código ${respuesta.code()}."
            }

            throw ErrorCuentaRemota(mensaje)
        }

        val estudiante = respuesta.body()
            ?: throw ErrorCuentaRemota(
                "El servidor no devolvió los datos del estudiante."
            )

        return estudiante.toCuentaEstudiante()
    }

    suspend fun obtenerPerfil(): CuentaEstudiante {

        val respuesta = cliente.api.obtenerPerfil()

        if (!respuesta.isSuccessful) {
            val mensaje = when (respuesta.code()) {
                401 -> "La sesión ha expirado. Inicia sesión nuevamente."
                else -> "No se pudo obtener el perfil. Código ${respuesta.code()}."
            }

            throw ErrorCuentaRemota(mensaje)
        }

        val estudiante = respuesta.body()
            ?: throw ErrorCuentaRemota(
                "No se recibieron los datos del perfil."
            )

        return estudiante.toCuentaEstudiante()
    }

    suspend fun cerrarSesion() {

        try {
            val respuesta = cliente.api.cerrarSesion()

            if (
                !respuesta.isSuccessful &&
                respuesta.code() != 401
            ) {
                throw ErrorCuentaRemota(
                    "No se pudo confirmar el cierre de sesión."
                )
            }
        } finally {
            cliente.limpiarSesion()
        }
    }

    private fun EstudianteRespuestaMPAZ.toCuentaEstudiante():
            CuentaEstudiante {

        return CuentaEstudiante(
            nombre = nombre,
            correo = correo,
            curso = curso
        )
    }
}