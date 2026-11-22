
package com.example.appmpazpro3_grupo6.validation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class ValidadorFormularioTest {

    private val correoValido = "alumno@ejemplo.cl"
    private val claveValida = "Clave123"

    @Test
    fun loginConDatosValidos() {
        val resultado = ValidadorFormulario.validarLogin(
            correoValido,
            claveValida
        )

        assertTrue(resultado.esValido)
        assertNull(resultado.correo)
        assertNull(resultado.contrasena)
    }

    @Test
    fun loginConCorreoVacio() {
        val resultado = ValidadorFormulario.validarLogin(
            "",
            claveValida
        )

        assertFalse(resultado.esValido)
        assertEquals("Ingresa tu correo", resultado.correo)
    }

    @Test
    fun loginConCorreoInvalido() {
        val resultado = ValidadorFormulario.validarLogin(
            "correo-invalido",
            claveValida
        )

        assertFalse(resultado.esValido)
        assertNotNull(resultado.correo)
    }

    @Test
    fun loginConCorreoConEspacios() {
        val resultado = ValidadorFormulario.validarLogin(
            "  alumno@ejemplo.cl  ",
            claveValida
        )

        assertTrue(resultado.esValido)
        assertNull(resultado.correo)
    }

    @Test
    fun loginConContrasenaVacia() {
        val resultado = ValidadorFormulario.validarLogin(
            correoValido,
            ""
        )

        assertFalse(resultado.esValido)
        assertNotNull(resultado.contrasena)
    }

    @Test
    fun loginConContrasenaCorta() {
        val resultado = ValidadorFormulario.validarLogin(
            correoValido,
            "12345"
        )

        assertFalse(resultado.esValido)
        assertEquals(
            "Debe tener al menos 6 caracteres",
            resultado.contrasena
        )
    }

    @Test
    fun loginConContrasenaDeSeisCaracteres() {
        val resultado = ValidadorFormulario.validarLogin(
            correoValido,
            "123456"
        )

        assertTrue(resultado.esValido)
    }

    @Test
    fun registroConDatosValidos() {
        val resultado = ValidadorFormulario.validarRegistro(
            "Estudiante MPAZ",
            correoValido,
            claveValida,
            claveValida
        )

        assertTrue(resultado.esValido)
        assertNull(resultado.nombre)
        assertNull(resultado.correo)
        assertNull(resultado.contrasena)
        assertNull(resultado.confirmacion)
    }

    @Test
    fun registroConNombreVacio() {
        val resultado = ValidadorFormulario.validarRegistro(
            "",
            correoValido,
            claveValida,
            claveValida
        )

        assertFalse(resultado.esValido)
        assertEquals("Ingresa tu nombre", resultado.nombre)
    }

    @Test
    fun registroConNombreSoloEspacios() {
        val resultado = ValidadorFormulario.validarRegistro(
            "   ",
            correoValido,
            claveValida,
            claveValida
        )

        assertFalse(resultado.esValido)
        assertNotNull(resultado.nombre)
    }

    @Test
    fun registroConCorreoVacio() {
        val resultado = ValidadorFormulario.validarRegistro(
            "Estudiante",
            "",
            claveValida,
            claveValida
        )

        assertFalse(resultado.esValido)
        assertNotNull(resultado.correo)
    }

    @Test
    fun registroConCorreoInvalido() {
        val resultado = ValidadorFormulario.validarRegistro(
            "Estudiante",
            "correo-sin-arroba",
            claveValida,
            claveValida
        )

        assertFalse(resultado.esValido)
        assertNotNull(resultado.correo)
    }

    @Test
    fun registroConContrasenaVacia() {
        val resultado = ValidadorFormulario.validarRegistro(
            "Estudiante",
            correoValido,
            "",
            ""
        )

        assertFalse(resultado.esValido)
        assertNotNull(resultado.contrasena)
        assertNotNull(resultado.confirmacion)
    }

    @Test
    fun registroConContrasenaCorta() {
        val resultado = ValidadorFormulario.validarRegistro(
            "Estudiante",
            correoValido,
            "123",
            "123"
        )

        assertFalse(resultado.esValido)
        assertNotNull(resultado.contrasena)
        assertNull(resultado.confirmacion)
    }

    @Test
    fun registroSinConfirmacion() {
        val resultado = ValidadorFormulario.validarRegistro(
            "Estudiante",
            correoValido,
            claveValida,
            ""
        )

        assertFalse(resultado.esValido)
        assertNotNull(resultado.confirmacion)
    }

    @Test
    fun registroConContrasenasDiferentes() {
        val resultado = ValidadorFormulario.validarRegistro(
            "Estudiante",
            correoValido,
            claveValida,
            "OtraClave"
        )

        assertFalse(resultado.esValido)
        assertNotNull(resultado.confirmacion)
    }

    @Test
    fun registroConConfirmacionCorrecta() {
        val resultado = ValidadorFormulario.validarRegistro(
            "Estudiante",
            correoValido,
            "123456",
            "123456"
        )

        assertTrue(resultado.esValido)
    }

    @Test
    fun erroresLoginSinErroresEsValido() {
        val errores = ErroresLogin()
        assertTrue(errores.esValido)
    }

    @Test
    fun erroresLoginConErrorNoEsValido() {
        val errores = ErroresLogin(correo = "Error")
        assertFalse(errores.esValido)
    }

    @Test
    fun erroresRegistroSinErroresEsValido() {
        val errores = ErroresRegistro()
        assertTrue(errores.esValido)
    }

    @Test
    fun erroresRegistroConErrorNoEsValido() {
        val errores = ErroresRegistro(nombre = "Error")
        assertFalse(errores.esValido)
    }
}