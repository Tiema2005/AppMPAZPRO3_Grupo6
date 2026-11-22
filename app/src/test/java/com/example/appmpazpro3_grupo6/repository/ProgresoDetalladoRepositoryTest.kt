package com.example.appmpazpro3_grupo6.repository

import com.example.appmpazpro3_grupo6.data.remote.ClienteEstudiantesMPAZ
import com.example.appmpazpro3_grupo6.data.remote.LoginEstudianteMPAZ
import com.example.appmpazpro3_grupo6.data.remote.ProgresoDetalleMPAZ
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ProgresoDetalladoRepositoryTest {

    @Test
    fun guardarYRecuperarDetalleConSesion() = runBlocking {

        val servidor = MockWebServer()
        servidor.start()

        try {

            val datosJson = """
                {
                    "progresoPorNivel": {
                        "matematica_facil": 3
                    },
                    "resultados": {},
                    "sesionesPractica": {}
                }
            """.trimIndent()

            val gson = Gson()

            servidor.enqueue(
                MockResponse()
                    .setResponseCode(200)
                    .addHeader(
                        "Content-Type",
                        "application/json"
                    )
                    .addHeader(
                        "Set-Cookie",
                        "JSESSIONID=detalle123; Path=/; HttpOnly"
                    )
                    .setBody(
                        """
                        {
                            "id": 1,
                            "nombre": "Estudiante Prueba",
                            "correo": "prueba@example.com",
                            "curso": "3° Básico"
                        }
                        """.trimIndent()
                    )
            )

            val respuestaDetalle = gson.toJson(
                ProgresoDetalleMPAZ(datosJson)
            )

            servidor.enqueue(
                MockResponse()
                    .setResponseCode(404)
            )

            servidor.enqueue(
                MockResponse()
                    .setResponseCode(200)
                    .addHeader(
                        "Content-Type",
                        "application/json"
                    )
                    .setBody(respuestaDetalle)
            )

            servidor.enqueue(
                MockResponse()
                    .setResponseCode(200)
                    .addHeader(
                        "Content-Type",
                        "application/json"
                    )
                    .setBody(respuestaDetalle)
            )

            val cliente = ClienteEstudiantesMPAZ.crear(
                servidor.url("/").toString()
            )

            val login = cliente.api.iniciarSesion(
                LoginEstudianteMPAZ(
                    correo = "prueba@example.com",
                    contrasena = "ClavePrueba123"
                )
            )

            assertTrue(login.isSuccessful)

            val repositorio = ProgresoRemotoRepository(cliente)

            val inicial = repositorio.obtenerDetalle()

            assertNull(inicial)

            val guardado = repositorio.guardarDetalle(
                datosJson
            )

            assertEquals(datosJson, guardado)

            val recuperado = repositorio.obtenerDetalle()

            assertEquals(datosJson, recuperado)

            val solicitudLogin = servidor.takeRequest()

            assertEquals(
                "/api/estudiantes/login",
                solicitudLogin.path
            )

            val solicitudInicial = servidor.takeRequest()

            assertEquals(
                "/api/progreso/detalle",
                solicitudInicial.path
            )

            val solicitudGuardar = servidor.takeRequest()

            assertEquals(
                "PUT",
                solicitudGuardar.method
            )

            assertEquals(
                "/api/progreso/detalle",
                solicitudGuardar.path
            )

            assertEquals(
                "JSESSIONID=detalle123",
                solicitudGuardar.getHeader("Cookie")
            )

            val cuerpo = gson.fromJson(
                solicitudGuardar.body.readUtf8(),
                ProgresoDetalleMPAZ::class.java
            )

            assertEquals(
                datosJson,
                cuerpo.datosJson
            )

            val solicitudRecuperar = servidor.takeRequest()

            assertEquals(
                "GET",
                solicitudRecuperar.method
            )

            assertEquals(
                "/api/progreso/detalle",
                solicitudRecuperar.path
            )

        } finally {
            servidor.shutdown()
        }
    }

    @Test
    fun consultarDetalleSinSesionDetectaError401() = runBlocking {

        val servidor = MockWebServer()
        servidor.start()

        try {

            servidor.enqueue(
                MockResponse()
                    .setResponseCode(401)
            )

            val repositorio = ProgresoRemotoRepository(
                ClienteEstudiantesMPAZ.crear(
                    servidor.url("/").toString()
                )
            )

            var errorDetectado = false

            try {

                repositorio.obtenerDetalle()

            } catch (e: ErrorProgresoRemoto) {

                errorDetectado = true

                assertEquals(
                    "Tu sesión ha expirado. Inicia sesión nuevamente.",
                    e.message
                )
            }

            assertTrue(errorDetectado)

        } finally {
            servidor.shutdown()
        }
    }
}