package com.example.appmpazpro3_grupo6.validation

import android.util.Patterns

data class ErroresLogin(
    val correo: String? = null,
    val contrasena: String? = null
) {
    val esValido: Boolean
        get() = correo == null && contrasena == null
}

data class ErroresRegistro(
    val nombre: String? = null,
    val correo: String? = null,
    val contrasena: String? = null,
    val confirmacion: String? = null
) {
    val esValido: Boolean
        get() = nombre == null &&
                correo == null &&
                contrasena == null &&
                confirmacion == null
}

object ValidadorFormulario {

    fun validarLogin(
        correo: String,
        contrasena: String
    ): ErroresLogin {

        return ErroresLogin(
            correo = validarCorreo(correo),
            contrasena = validarContrasena(
                contrasena,
                mensajeVacio = "Ingresa tu contraseña"
            )
        )
    }

    fun validarRegistro(
        nombre: String,
        correo: String,
        contrasena: String,
        confirmarContrasena: String
    ): ErroresRegistro {

        return ErroresRegistro(
            nombre = if (nombre.isBlank()) {
                "Ingresa tu nombre"
            } else {
                null
            },

            correo = validarCorreo(correo),

            contrasena = validarContrasena(
                contrasena,
                mensajeVacio = "Ingresa una contraseña"
            ),

            confirmacion = when {
                confirmarContrasena.isBlank() ->
                    "Confirma tu contraseña"

                confirmarContrasena != contrasena ->
                    "Las contraseñas no coinciden"

                else -> null
            }
        )
    }

    private fun validarCorreo(correo: String): String? {
        return when {
            correo.isBlank() ->
                "Ingresa tu correo"

            !Patterns.EMAIL_ADDRESS
                .matcher(correo.trim())
                .matches() ->
                "Ingresa un correo válido"

            else -> null
        }
    }

    private fun validarContrasena(
        contrasena: String,
        mensajeVacio: String
    ): String? {

        return when {
            contrasena.isBlank() ->
                mensajeVacio

            contrasena.length < 6 ->
                "Debe tener al menos 6 caracteres"

            else -> null
        }
    }
}