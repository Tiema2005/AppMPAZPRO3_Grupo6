package com.example.appmpazpro3_grupo6.data.remote

import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BackendApiTest {

    @Test
    fun consultarEstadoDevuelveRespuestaCorrecta() = runBlocking {
        val servidor = MockWebServer()
        servidor.start()

        try {
            servidor.enqueue(
                MockResponse()
                    .setResponseCode(200)
                    .addHeader("Content-Type", "application/json")
                    .setBody(
                        """
                        {
                            "mensaje": "Backend funcionando correctamente",
                            "aplicacion": "MPAZ PRO 3",
                            "estado": "activo"
                        }
                        """.trimIndent()
                    )
            )

            val api = ClienteBackendMPAZ.crear(
                servidor.url("/").toString()
            )

            val respuesta = api.consultarEstado()

            assertTrue(respuesta.isSuccessful)
            assertEquals(200, respuesta.code())
            assertEquals(
                "Backend funcionando correctamente",
                respuesta.body()?.mensaje
            )
            assertEquals(
                "MPAZ PRO 3",
                respuesta.body()?.aplicacion
            )
            assertEquals(
                "activo",
                respuesta.body()?.estado
            )
            assertEquals(
                "/api/estado",
                servidor.takeRequest().path
            )
        } finally {
            servidor.shutdown()
        }
    }

    @Test
    fun consultarEstadoDetectaErrorDelServidor() = runBlocking {
        val servidor = MockWebServer()
        servidor.start()

        try {
            servidor.enqueue(
                MockResponse()
                    .setResponseCode(503)
                    .addHeader("Content-Type", "application/json")
                    .setBody(
                        """
                        {
                            "mensaje": "Servicio no disponible",
                            "estado": "error"
                        }
                        """.trimIndent()
                    )
            )

            val api = ClienteBackendMPAZ.crear(
                servidor.url("/").toString()
            )

            val respuesta = api.consultarEstado()

            assertFalse(respuesta.isSuccessful)
            assertEquals(503, respuesta.code())
            assertEquals(
                "/api/estado",
                servidor.takeRequest().path
            )
        } finally {
            servidor.shutdown()
        }
    }
}