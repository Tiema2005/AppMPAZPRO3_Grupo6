package com.example.appmpazpro3_grupo6.repository

import com.example.appmpazpro3_grupo6.data.remote.ClienteEstudiantesMPAZ
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CuentaRemotaRepositoryTest {

    @Test
    fun registrarCreaCuentaEIniciaSesion() = runBlocking {

        val servidor = MockWebServer()
        servidor.start()

        try {

            val estudianteJson = """
                {
                    "id": 10,
                    "nombre": "Estudiante Prueba",
                    "correo": "prueba@example.com",
                    "curso": "3° Básico"
                }
            """.trimIndent()

            servidor.enqueue(
                MockResponse()
                    .setResponseCode(201)
                    .addHeader(
                        "Content-Type",
                        "application/json"
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
                    .addHeader(
                        "Set-Cookie",
                        "JSESSIONID=abc123; Path=/; HttpOnly"
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

            val repositorio = CuentaRemotaRepository(
                cliente = cliente
            )

            val estudiante = repositorio.registrarCuenta(
                nombre = "Estudiante Prueba",
                correo = "prueba@example.com",
                contrasena = "ClavePrueba123"
            )

            assertEquals(
                "Estudiante Prueba",
                estudiante.nombre
            )

            assertEquals(
                "3° Básico",
                estudiante.curso
            )

            val solicitudRegistro = servidor.takeRequest()

            assertEquals(
                "POST",
                solicitudRegistro.method
            )

            assertEquals(
                "/api/estudiantes/registro",
                solicitudRegistro.path
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

            val perfil = repositorio.obtenerPerfil()

            assertEquals(
                "prueba@example.com",
                perfil.correo
            )

            val solicitudPerfil = servidor.takeRequest()

            assertEquals(
                "JSESSIONID=abc123",
                solicitudPerfil.getHeader("Cookie")
            )

            assertTrue(
                solicitudRegistro.body.readUtf8()
                    .contains("\"contrasena\":\"ClavePrueba123\"")
            )

        } finally {
            servidor.shutdown()
        }
    }

    @Test
    fun loginIncorrectoMuestraError() = runBlocking {

        val servidor = MockWebServer()
        servidor.start()

        try {

            servidor.enqueue(
                MockResponse()
                    .setResponseCode(401)
            )

            val repositorio = CuentaRemotaRepository(
                cliente = ClienteEstudiantesMPAZ.crear(
                    servidor.url("/").toString()
                )
            )

            var errorDetectado = false

            try {

                repositorio.iniciarSesion(
                    correo = "prueba@example.com",
                    contrasena = "ClaveIncorrecta"
                )

            } catch (e: ErrorCuentaRemota) {

                errorDetectado = true

                assertEquals(
                    "Correo o contraseña incorrectos.",
                    e.message
                )
            }

            assertTrue(errorDetectado)

            assertEquals(
                "/api/estudiantes/login",
                servidor.takeRequest().path
            )

        } finally {
            servidor.shutdown()
        }
    }
}