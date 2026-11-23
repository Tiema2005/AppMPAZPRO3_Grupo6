package com.example.appmpazpro3_grupo6.data.remote

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class BackendApiIntegracionRealTest {

    @Test
    fun retrofitConectaConSpringBootReal() = runBlocking {

        // Direccion del backend desde el computador
        val api = ClienteBackendMPAZ.crear(
            "http://127.0.0.1:8080/"
        )

        // Solicitud HTTP real mediante Retrofit
        val respuesta = api.consultarEstado()

        // Comprobar respuesta HTTP
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