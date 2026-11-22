package com.example.appmpazpro3_grupo6.repository

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import java.util.UUID

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class EstadoSincronizacionMPAZCoberturaTest {

    @Test
    fun hashSha256YVersionPorCorreoNormalizado() = runBlocking {
        val repositorio = EstadoSincronizacionMPAZ(
            RuntimeEnvironment.getApplication()
        )
        val correo = "lt71-${UUID.randomUUID()}@ejemplo.cl"

        assertEquals(
            "ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad",
            repositorio.calcularHash("abc")
        )
        assertNull(repositorio.obtenerVersion(correo))

        repositorio.guardarVersion("  ${correo.uppercase()}  ", "version-1")
        assertEquals("version-1", repositorio.obtenerVersion(correo))

        repositorio.guardarVersion(correo, "version-2")
        assertEquals("version-2", repositorio.obtenerVersion(correo))

        repositorio.guardarVersion(correo, null)
        assertNull(repositorio.obtenerVersion(correo))
    }
}