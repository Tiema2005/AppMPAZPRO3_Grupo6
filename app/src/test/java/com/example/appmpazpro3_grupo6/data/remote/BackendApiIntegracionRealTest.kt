package com.example.appmpazpro3_grupo6.data.remote

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.Assume.assumeTrue

class BackendApiIntegracionRealTest {

    @Test
    fun retrofitConectaConSpringBootReal() = runBlocking {

        assumeTrue(
            "Prueba real opcional: requiere Spring Boot",
            System.getenv("MPAZ_REAL_INTEGRATION") == "true"
        )

        val api = ClienteBackendMPAZ.crear(
            "http://127.0.0.1:8080/"
        )

        val respuesta = api.consultarEstado()

        assertTrue(
            "El backend respondio HTTP ${respuesta.code()}",
            respuesta.isSuccessful
        )

        assertEquals(200, respuesta.code())

        val datos = respuesta.body()

        assertNotNull(datos)

        assertEquals(
            "MPAZ PRO 3",
            datos?.aplicacion
        )

        assertEquals(
            "activo",
            datos?.estado
        )

        assertEquals(
            "Backend funcionando correctamente",
            datos?.mensaje
        )

        println("LT-66: CONEXION REAL EXITOSA")
        println("HTTP: ${respuesta.code()}")
        println("Aplicacion: ${datos?.aplicacion}")
        println("Estado: ${datos?.estado}")
        println("Mensaje: ${datos?.mensaje}")
    }
}