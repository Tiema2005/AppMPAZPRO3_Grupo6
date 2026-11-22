package com.example.appmpazpro3_grupo6.validation

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ErroresFormularioCoberturaTest {

    @Test
    fun loginSinErroresEsValido() {
        val errores = ErroresLogin()

        assertTrue(errores.esValido)
    }

    @Test
    fun loginConCorreoIncorrectoNoEsValido() {
        val errores = ErroresLogin(
            correo = "Correo incorrecto"
        )

        assertFalse(errores.esValido)
    }

    @Test
    fun loginConContrasenaIncorrectaNoEsValido() {
        val errores = ErroresLogin(
            contrasena = "Contrasena incorrecta"
        )

        assertFalse(errores.esValido)
    }

    @Test
    fun registroSinErroresEsValido() {
        val errores = ErroresRegistro()

        assertTrue(errores.esValido)
    }

    @Test
    fun registroConNombreIncorrectoNoEsValido() {
        val errores = ErroresRegistro(
            nombre = "Nombre obligatorio"
        )

        assertFalse(errores.esValido)
    }

    @Test
    fun registroConCorreoIncorrectoNoEsValido() {
        val errores = ErroresRegistro(
            correo = "Correo incorrecto"
        )

        assertFalse(errores.esValido)
    }

    @Test
    fun registroConContrasenaIncorrectaNoEsValido() {
        val errores = ErroresRegistro(
            contrasena = "Contrasena incorrecta"
        )

        assertFalse(errores.esValido)
    }

    @Test
    fun registroConConfirmacionIncorrectaNoEsValido() {
        val errores = ErroresRegistro(
            confirmacion = "Las contrasenas no coinciden"
        )

        assertFalse(errores.esValido)
    }
}