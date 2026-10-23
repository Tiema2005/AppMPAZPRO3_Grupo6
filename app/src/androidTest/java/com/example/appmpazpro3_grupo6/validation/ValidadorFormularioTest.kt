package com.example.appmpazpro3_grupo6.validation

import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ValidadorFormularioTest {

    @Test
    fun loginConCorreoVacioMuestraError() {
        val resultado = ValidadorFormulario.validarLogin(
            correo = "",
            contrasena = "123456"
        )

        assertEquals(
            "Ingresa tu correo",
            resultado.correo
        )

        assertFalse(resultado.esValido)
    }

    @Test
    fun loginConCorreoIncorrectoMuestraError() {
        val resultado = ValidadorFormulario.validarLogin(
            correo = "correo-invalido",
            contrasena = "123456"
        )

        assertEquals(
            "Ingresa un correo válido",
            resultado.correo
        )

        assertFalse(resultado.esValido)
    }

    @Test
    fun loginConContrasenaCortaMuestraError() {
        val resultado = ValidadorFormulario.validarLogin(
            correo = "alumno@ejemplo.com",
            contrasena = "123"
        )

        assertEquals(
            "Debe tener al menos 6 caracteres",
            resultado.contrasena
        )

        assertFalse(resultado.esValido)
    }

    @Test
    fun loginValidoNoPresentaErrores() {
        val resultado = ValidadorFormulario.validarLogin(
            correo = "alumno@ejemplo.com",
            contrasena = "123456"
        )

        assertTrue(resultado.esValido)
        assertNull(resultado.correo)
        assertNull(resultado.contrasena)
    }

    @Test
    fun registroSinNombreMuestraError() {
        val resultado = ValidadorFormulario.validarRegistro(
            nombre = "",
            correo = "alumno@ejemplo.com",
            contrasena = "123456",
            confirmarContrasena = "123456"
        )

        assertEquals(
            "Ingresa tu nombre",
            resultado.nombre
        )

        assertFalse(resultado.esValido)
    }

    @Test
    fun registroConContrasenasDiferentesMuestraError() {
        val resultado = ValidadorFormulario.validarRegistro(
            nombre = "Camila González",
            correo = "alumno@ejemplo.com",
            contrasena = "123456",
            confirmarContrasena = "654321"
        )

        assertEquals(
            "Las contraseñas no coinciden",
            resultado.confirmacion
        )

        assertFalse(resultado.esValido)
    }

    @Test
    fun registroValidoNoPresentaErrores() {
        val resultado = ValidadorFormulario.validarRegistro(
            nombre = "Camila González",
            correo = "alumno@ejemplo.com",
            contrasena = "123456",
            confirmarContrasena = "123456"
        )

        assertTrue(resultado.esValido)
        assertNull(resultado.nombre)
        assertNull(resultado.correo)
        assertNull(resultado.contrasena)
        assertNull(resultado.confirmacion)
    }
}