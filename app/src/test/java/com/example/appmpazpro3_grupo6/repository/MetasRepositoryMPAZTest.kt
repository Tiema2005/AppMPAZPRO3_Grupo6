package com.example.appmpazpro3_grupo6.repository

import com.example.appmpazpro3_grupo6.data.remote.ClienteEstudiantesMPAZ
import com.example.appmpazpro3_grupo6.data.remote.LoginEstudianteMPAZ
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MetasRepositoryMPAZTest {

    @Test
    fun listarMetasConservaSesionDelEstudiante() = runBlocking {

        val servidor = MockWebServer()
        servidor.start()

        try {

            servidor.enqueue(
                MockResponse()
                    .setResponseCode(200)
                    .addHeader("Content-Type", "application/json")
                    .addHeader(
                        "Set-Cookie",
                        "JSESSIONID=metas123; Path=/; HttpOnly"
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
                                "titulo": "Practicar Matemática",
                                "descripcion": "Resolver ejercicios",
                                "completada": false
                            }
                        ]
                        """.trimIndent()
                    )
            )

            val cliente = ClienteEstudiantesMPAZ.crear(
                servidor.url("/").toString()
            )

            val respuestaLogin = cliente.api.iniciarSesion(
                LoginEstudianteMPAZ(
                    correo = "prueba@example.com",
                    contrasena = "ClavePrueba123"
                )
            )

            assertTrue(respuestaLogin.isSuccessful)

            val repositorio = MetasRepositoryMPAZ(cliente)

            val metas = repositorio.listar()

            assertEquals(1, metas.size)
            assertEquals(10L, metas.first().id)
            assertEquals(
                "Practicar Matemática",
                metas.first().titulo
            )

            val solicitudLogin = servidor.takeRequest()

            assertEquals(
                "/api/estudiantes/login",
                solicitudLogin.path
            )

            val solicitudMetas = servidor.takeRequest()

            assertEquals("GET", solicitudMetas.method)

            assertEquals(
                "/api/metas",
                solicitudMetas.path
            )

            assertEquals(
                "JSESSIONID=metas123",
                solicitudMetas.getHeader("Cookie")
            )

        } finally {
            servidor.shutdown()
        }
    }

    @Test
    fun crearActualizarYEliminarMetas() = runBlocking {

        val servidor = MockWebServer()
        servidor.start()

        try {

            servidor.enqueue(
                MockResponse()
                    .setResponseCode(201)
                    .addHeader("Content-Type", "application/json")
                    .setBody(
                        """
                        {
                            "id": 20,
                            "titulo": "Leer un cuento",
                            "descripcion": "Leer diez páginas",
                            "completada": false
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
                        {
                            "id": 20,
                            "titulo": "Leer un cuento",
                            "descripcion": "Leer veinte páginas",
                            "completada": true
                        }
                        """.trimIndent()
                    )
            )

            servidor.enqueue(
                MockResponse()
                    .setResponseCode(204)
            )

            val cliente = ClienteEstudiantesMPAZ.crear(
                servidor.url("/").toString()
            )

            val repositorio = MetasRepositoryMPAZ(cliente)

            val creada = repositorio.crear(
                titulo = "Leer un cuento",
                descripcion = "Leer diez páginas"
            )

            assertEquals(20L, creada.id)
            assertFalse(creada.completada)

            val actualizada = repositorio.actualizar(
                id = creada.id,
                titulo = "Leer un cuento",
                descripcion = "Leer veinte páginas",
                completada = true
            )

            assertTrue(actualizada.completada)

            assertEquals(
                "Leer veinte páginas",
                actualizada.descripcion
            )

            repositorio.eliminar(actualizada.id)

            val solicitudCrear = servidor.takeRequest()

            assertEquals("POST", solicitudCrear.method)

            assertEquals(
                "/api/metas",
                solicitudCrear.path
            )

            val cuerpoCrear = solicitudCrear.body.readUtf8()

            assertTrue(
                cuerpoCrear.contains(
                    "\"titulo\":\"Leer un cuento\""
                )
            )

            val solicitudActualizar = servidor.takeRequest()

            assertEquals("PUT", solicitudActualizar.method)

            assertEquals(
                "/api/metas/20",
                solicitudActualizar.path
            )

            assertTrue(
                solicitudActualizar.body.readUtf8()
                    .contains("\"completada\":true")
            )

            val solicitudEliminar = servidor.takeRequest()

            assertEquals(
                "DELETE",
                solicitudEliminar.method
            )

            assertEquals(
                "/api/metas/20",
                solicitudEliminar.path
            )

        } finally {
            servidor.shutdown()
        }
    }

    @Test
    fun consultarMetasSinSesionDevuelveError() = runBlocking {

        val servidor = MockWebServer()
        servidor.start()

        try {

            servidor.enqueue(
                MockResponse()
                    .setResponseCode(401)
            )

            val repositorio = MetasRepositoryMPAZ(
                ClienteEstudiantesMPAZ.crear(
                    servidor.url("/").toString()
                )
            )

            var errorDetectado = false

            try {
                repositorio.listar()

            } catch (e: ErrorMetasMPAZ) {

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