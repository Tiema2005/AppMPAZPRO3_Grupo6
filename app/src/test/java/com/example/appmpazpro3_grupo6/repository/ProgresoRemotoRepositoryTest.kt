package com.example.appmpazpro3_grupo6.repository

import com.example.appmpazpro3_grupo6.data.remote.ClienteEstudiantesMPAZ
import com.example.appmpazpro3_grupo6.data.remote.LoginEstudianteMPAZ
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ProgresoRemotoRepositoryTest {

    @Test
    fun listarProgresoConservaSesion() = runBlocking {

        val servidor = MockWebServer()
        servidor.start()

        try {
            servidor.enqueue(
                MockResponse()
                    .setResponseCode(200)
                    .addHeader("Content-Type", "application/json")
                    .addHeader(
                        "Set-Cookie",
                        "JSESSIONID=progreso123; Path=/; HttpOnly"
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

            servidor.enqueue(
                MockResponse()
                    .setResponseCode(200)
                    .addHeader("Content-Type", "application/json")
                    .setBody(
                        """
                        [
                            {
                                "id": 10,
                                "asignatura": "matematica_facil",
                                "ejerciciosRespondidos": 3,
                                "respuestasCorrectas": 2,
                                "porcentajeAcierto": 66.67
                            }
                        ]
                        """.trimIndent()
                    )
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

            val progresos = repositorio.listar()

            assertEquals(1, progresos.size)

            assertEquals(
                "matematica_facil",
                progresos.first().asignatura
            )

            assertEquals(
                3,
                progresos.first().ejerciciosRespondidos
            )

            assertEquals(
                2,
                progresos.first().respuestasCorrectas
            )

            assertEquals(
                66.67,
                progresos.first().porcentajeAcierto,
                0.01
            )

            val solicitudLogin = servidor.takeRequest()

            assertEquals(
                "/api/estudiantes/login",
                solicitudLogin.path
            )

            val solicitudProgreso = servidor.takeRequest()

            assertEquals(
                "/api/progreso",
                solicitudProgreso.path
            )

            assertEquals(
                "JSESSIONID=progreso123",
                solicitudProgreso.getHeader("Cookie")
            )

        } finally {
            servidor.shutdown()
        }
    }

    @Test
    fun guardarYConsultarProgresoPorNivel() = runBlocking {

        val servidor = MockWebServer()
        servidor.start()

        try {
            val resultadoJson = """
                {
                    "id": 15,
                    "asignatura": "matematica_facil",
                    "ejerciciosRespondidos": 3,
                    "respuestasCorrectas": 2,
                    "porcentajeAcierto": 66.67
                }
            """.trimIndent()

            servidor.enqueue(
                MockResponse()
                    .setResponseCode(200)
                    .addHeader("Content-Type", "application/json")
                    .setBody(resultadoJson)
            )

            servidor.enqueue(
                MockResponse()
                    .setResponseCode(200)
                    .addHeader("Content-Type", "application/json")
                    .setBody(resultadoJson)
            )

            val repositorio = ProgresoRemotoRepository(
                ClienteEstudiantesMPAZ.crear(
                    servidor.url("/").toString()
                )
            )

            val guardado = repositorio.guardar(
                asignaturaNivel = "matematica_facil",
                ejerciciosRespondidos = 3,
                respuestasCorrectas = 2
            )

            assertEquals(3, guardado.ejerciciosRespondidos)
            assertEquals(2, guardado.respuestasCorrectas)

            val solicitudGuardar = servidor.takeRequest()

            assertEquals("POST", solicitudGuardar.method)

            assertEquals(
                "/api/progreso",
                solicitudGuardar.path
            )

            val cuerpo = solicitudGuardar.body.readUtf8()

            assertTrue(
                cuerpo.contains(
                    "\"asignatura\":\"matematica_facil\""
                )
            )

            assertTrue(
                cuerpo.contains(
                    "\"ejerciciosRespondidos\":3"
                )
            )

            assertTrue(
                cuerpo.contains(
                    "\"respuestasCorrectas\":2"
                )
            )

            val obtenido = repositorio.obtener(
                "matematica_facil"
            )

            assertEquals(15L, obtenido?.id)

            assertEquals(
                "matematica_facil",
                obtenido?.asignatura
            )

            val solicitudObtener = servidor.takeRequest()

            assertEquals("GET", solicitudObtener.method)

            assertEquals(
                "/api/progreso/asignatura/matematica_facil",
                solicitudObtener.path
            )

        } finally {
            servidor.shutdown()
        }
    }

    @Test
    fun nivelSinProgresoDevuelveNull() = runBlocking {

        val servidor = MockWebServer()
        servidor.start()

        try {
            servidor.enqueue(
                MockResponse()
                    .setResponseCode(404)
            )

            val repositorio = ProgresoRemotoRepository(
                ClienteEstudiantesMPAZ.crear(
                    servidor.url("/").toString()
                )
            )

            val resultado = repositorio.obtener(
                "ciencias_dificil"
            )

            assertNull(resultado)

            assertEquals(
                "/api/progreso/asignatura/ciencias_dificil",
                servidor.takeRequest().path
            )

        } finally {
            servidor.shutdown()
        }
    }

    @Test
    fun rechazaResultadosInvalidos() = runBlocking {

        val servidor = MockWebServer()
        servidor.start()

        try {
            val repositorio = ProgresoRemotoRepository(
                ClienteEstudiantesMPAZ.crear(
                    servidor.url("/").toString()
                )
            )

            var errorDetectado = false

            try {
                repositorio.guardar(
                    asignaturaNivel = "matematica_facil",
                    ejerciciosRespondidos = 2,
                    respuestasCorrectas = 3
                )

            } catch (e: ErrorProgresoRemoto) {
                errorDetectado = true
            }

            assertTrue(errorDetectado)
            assertEquals(0, servidor.requestCount)

        } finally {
            servidor.shutdown()
        }
    }
}