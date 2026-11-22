package com.example.appmpazpro3_grupo6.repository

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class CuentaLocalRepositoryCoberturaTest {

    @Test
    fun registroLoginYProteccionDeCuentaExistente() = runBlocking {
        val app = RuntimeEnvironment.getApplication()
        val repositorio = CuentaLocalRepository(app)

        assertFalse(repositorio.existeCuenta())
        assertNull(repositorio.iniciarSesion("nadie@ejemplo.cl", "Clave123"))

        assertTrue(
            repositorio.registrarCuenta(
                nombre = "  Alumna Prueba  ",
                correo = "  ALUMNA@EJEMPLO.CL  ",
                contrasena = "ClaveSegura123",
                curso = "3° Básico"
            )
        )
        assertTrue(repositorio.existeCuenta())

        assertFalse(
            repositorio.registrarCuenta(
                nombre = "Otra persona",
                correo = "otra@ejemplo.cl",
                contrasena = "ClaveDistinta",
                curso = "4° Básico"
            )
        )
        assertNull(repositorio.iniciarSesion("alumna@ejemplo.cl", "Incorrecta"))
        assertNull(repositorio.iniciarSesion("otra@ejemplo.cl", "ClaveSegura123"))

        val cuenta = repositorio.iniciarSesion(
            correo = " ALUMNA@EJEMPLO.CL ",
            contrasena = "ClaveSegura123"
        )

        assertEquals("Alumna Prueba", cuenta?.nombre)
        assertEquals("alumna@ejemplo.cl", cuenta?.correo)
        assertEquals("3° Básico", cuenta?.curso)
    }
}