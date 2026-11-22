package com.example.appmpazpro3_grupo6.repository

import com.example.appmpazpro3_grupo6.data.remote.ClienteEstudiantesMPAZ
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SincronizadorEstadisticasMPAZTest {

    @Test
    fun soloEnviaNivelesModificados() = runBlocking {

        val servidor = MockWebServer()
        servidor.start()

        try {

            servidor.enqueue(
                MockResponse()
                    .setResponseCode(200)
                    .addHeader("Content-Type", "application/json")
                    .setBody("[]")
            )

            servidor.enqueue(
                MockResponse()
                    .setResponseCode(200)
                    .addHeader("Content-Type", "application/json")
                    .setBody(
                        """
                        {
                            "id": 1,
                            "asignatura": "matematica_facil",
                            "ejerciciosRespondidos": 3,
                            "respuestasCorrectas": 2,
                            "porcentajeAcierto": 66.67
                        }
                        """.trimIndent()
                    )
            )

            val cliente = ClienteEstudiantesMPAZ.crear(
                servidor.url("/").toString()
            )

            val sincronizador = SincronizadorEstadisticasMPAZ(
                ProgresoRemotoRepository(cliente)
            )

            val progreso = ProgresoEstudiante(
                progresoPorNivel = mapOf(
                    "matematica_facil" to 3
                ),
                resultados = mapOf(
                    "matematica_facil" to mapOf(
                        1 to ResultadoEjercicio(
                            intentos = 1,
                            historial = listOf(true)
                        ),
                        2 to ResultadoEjercicio(
                            intentos = 1,
                            historial = listOf(false)
                        ),
                        3 to ResultadoEjercicio(
                            intentos = 1,
                            historial = listOf(true)
                        )
                    )
                )
            )

            sincronizador.sincronizar(progreso)

            assertEquals(2, servidor.requestCount)

            val solicitudListado = servidor.takeRequest()

            assertEquals("GET", solicitudListado.method)
            assertEquals("/api/progreso", solicitudListado.path)

            val solicitudGuardado = servidor.takeRequest()

            assertEquals("POST", solicitudGuardado.method)
            assertEquals("/api/progreso", solicitudGuardado.path)

            val cuerpo = solicitudGuardado.body.readUtf8()

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

        } finally {
            servidor.shutdown()
        }
    }

    @Test
    fun reiniciarNivelActualizaEstadisticasEnCero() = runBlocking {

        val servidor = MockWebServer()
        servidor.start()

        try {

            servidor.enqueue(
                MockResponse()
                    .setResponseCode(200)
                    .addHeader("Content-Type", "application/json")
                    .setBody(
                        """
                        [
                            {
                                "id": 4,
                                "asignatura": "matematica_facil",
                                "ejerciciosRespondidos": 5,
                                "respuestasCorrectas": 4,
                                "porcentajeAcierto": 80.0
                            }
                        ]
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
                            "id": 4,
                            "asignatura": "matematica_facil",
                            "ejerciciosRespondidos": 0,
                            "respuestasCorrectas": 0,
                            "porcentajeAcierto": 0.0
                        }
                        """.trimIndent()
                    )
            )

            val cliente = ClienteEstudiantesMPAZ.crear(
                servidor.url("/").toString()
            )

            val sincronizador = SincronizadorEstadisticasMPAZ(
                ProgresoRemotoRepository(cliente)
            )

            sincronizador.sincronizar(
                ProgresoEstudiante()
            )

            assertEquals(2, servidor.requestCount)

            servidor.takeRequest()

            val solicitud = servidor.takeRequest()

            assertEquals("POST", solicitud.method)

            val cuerpo = solicitud.body.readUtf8()

            assertTrue(
                cuerpo.contains(
                    "\"asignatura\":\"matematica_facil\""
                )
            )

            assertTrue(
                cuerpo.contains(
                    "\"ejerciciosRespondidos\":0"
                )
            )

            assertTrue(
                cuerpo.contains(
                    "\"respuestasCorrectas\":0"
                )
            )

        } finally {
            servidor.shutdown()
        }
    }
}