package com.example.appmpazpro3_grupo6.data.remote

import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class EstudiantesApiMPAZTest {

    @Test
    fun loginGuardaCookieYPermiteConsultarPerfil() = runBlocking {

        val servidor = MockWebServer()
        servidor.start()

        try {
            val estudianteJson = """
                {
                    "id": 1,
                    "nombre": "Estudiante Prueba",
                    "correo": "prueba@example.com",
                    "curso": "3° Básico"
                }
            """.trimIndent()

            servidor.enqueue(
                MockResponse()
                    .setResponseCode(200)
                    .addHeader(
                        "Content-Type",
                        "application/json"
                    )
                    .addHeader(
                        "Set-Cookie",
                        "JSESSIONID=sesion123; Path=/; HttpOnly"
                    )
                    .setBody(estudianteJson)
            )

            servidor.enqueue(
                MockResponse()
                    .setResponseCode(200)
                    .addHeader(
                        "Content-Type",
                        "application/json"
                    )
                    .setBody(estudianteJson)
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

            assertEquals(
                "Estudiante Prueba",
                login.body()?.nombre
            )

            val perfil = cliente.api.obtenerPerfil()

            assertTrue(perfil.isSuccessful)

            assertEquals(
                "prueba@example.com",
                perfil.body()?.correo
            )

            assertEquals(
                "3° Básico",
                perfil.body()?.curso
            )

            val solicitudLogin = servidor.takeRequest()

            assertEquals(
                "POST",
                solicitudLogin.method
            )

            assertEquals(
                "/api/estudiantes/login",
                solicitudLogin.path
            )

            val solicitudPerfil = servidor.takeRequest()

            assertEquals(
                "GET",
                solicitudPerfil.method
            )

            assertEquals(
                "/api/estudiantes/perfil",
                solicitudPerfil.path
            )

            assertEquals(
                "JSESSIONID=sesion123",
                solicitudPerfil.getHeader("Cookie")
            )

        } finally {
            servidor.shutdown()
        }
    }

    @Test
    fun perfilSinSesionDevuelveError401() = runBlocking {

        val servidor = MockWebServer()
        servidor.start()

        try {
            servidor.enqueue(
                MockResponse()
                    .setResponseCode(401)
            )

            val cliente = ClienteEstudiantesMPAZ.crear(
                servidor.url("/").toString()
            )

            val respuesta = cliente.api.obtenerPerfil()

            assertFalse(respuesta.isSuccessful)

            assertEquals(
                401,
                respuesta.code()
            )

        } finally {
            servidor.shutdown()
        }
    }

    @Test
    fun registroEnviaLosCamposCorrectos() = runBlocking {

        val servidor = MockWebServer()
        servidor.start()

        try {
            servidor.enqueue(
                MockResponse()
                    .setResponseCode(201)
                    .addHeader(
                        "Content-Type",
                        "application/json"
                    )
                    .setBody(
                        """
                        {
                            "id": 2,
                            "nombre": "Estudiante Nuevo",
                            "correo": "nuevo@example.com",
                            "curso": "3° Básico"
                        }
                        """.trimIndent()
                    )
            )

            val cliente = ClienteEstudiantesMPAZ.crear(
                servidor.url("/").toString()
            )

            val respuesta = cliente.api.registrar(
                RegistroEstudianteMPAZ(
                    nombre = "Estudiante Nuevo",
                    correo = "nuevo@example.com",
                    contrasena = "ClavePrueba123"
                )
            )

            assertTrue(respuesta.isSuccessful)

            assertEquals(
                201,
                respuesta.code()
            )

            val solicitud = servidor.takeRequest()

            assertEquals(
                "/api/estudiantes/registro",
                solicitud.path
            )

            val cuerpo = solicitud.body.readUtf8()

            assertTrue(
                cuerpo.contains(
                    "\"nombre\":\"Estudiante Nuevo\""
                )
            )

            assertTrue(
                cuerpo.contains(
                    "\"correo\":\"nuevo@example.com\""
                )
            )

            assertTrue(
                cuerpo.contains(
                    "\"contrasena\":\"ClavePrueba123\""
                )
            )

            assertFalse(
                cuerpo.contains("\"curso\"")
            )

        } finally {
            servidor.shutdown()
        }
    }
}