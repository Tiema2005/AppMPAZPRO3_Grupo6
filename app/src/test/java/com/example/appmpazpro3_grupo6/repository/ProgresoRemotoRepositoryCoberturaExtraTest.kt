package com.example.appmpazpro3_grupo6.repository

import com.example.appmpazpro3_grupo6.data.remote.ClienteEstudiantesMPAZ
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ProgresoRemotoRepositoryCoberturaExtraTest {

    private lateinit var servidor: MockWebServer
    private lateinit var repo: ProgresoRemotoRepository

    @Before
    fun preparar() {
        servidor = MockWebServer()
        servidor.start()
        repo = ProgresoRemotoRepository(
            ClienteEstudiantesMPAZ.crear(
                servidor.url("/").toString()
            )
        )
    }

    @After
    fun limpiar() {
        servidor.shutdown()
    }

    @Test
    fun rechazaAsignaturaYResultadosInvalidosSinConsultarServidor() = runBlocking {
        val asignatura = runCatching {
            repo.obtener("matematica_experto")
        }.exceptionOrNull()

        val avance = runCatching {
            repo.guardar(
                "matematica_facil",
                6,
                1
            )
        }.exceptionOrNull()

        val aciertos = runCatching {
            repo.guardar(
                "matematica_facil",
                2,
                3
            )
        }.exceptionOrNull()

        assertTrue(asignatura is ErrorProgresoRemoto)
        assertEquals(
            "Asignatura o dificultad no válida.",
            asignatura?.message
        )
        assertTrue(avance is ErrorProgresoRemoto)
        assertTrue(aciertos is ErrorProgresoRemoto)
        assertEquals(0, servidor.requestCount)
    }

    @Test
    fun rechazaDetalleVacioYDemasiadoGrande() = runBlocking {
        val vacio = runCatching {
            repo.guardarDetalle("  ")
        }.exceptionOrNull()

        val grande = runCatching {
            repo.guardarDetalle("x".repeat(250001))
        }.exceptionOrNull()

        assertTrue(vacio is ErrorProgresoRemoto)
        assertTrue(grande is ErrorProgresoRemoto)
        assertEquals(0, servidor.requestCount)
    }

    @Test
    fun listadoSinSesionDevuelveError401() = runBlocking {
        servidor.enqueue(
            MockResponse().setResponseCode(401)
        )

        val error = runCatching {
            repo.listar()
        }.exceptionOrNull()

        assertTrue(error is ErrorProgresoRemoto)
        assertEquals(
            "Tu sesión ha expirado. Inicia sesión nuevamente.",
            error?.message
        )
        assertEquals(
            "/api/progreso",
            servidor.takeRequest().path
        )
    }

    @Test
    fun obtenerProgresoExitosoPeroSinCuerpoDevuelveError() = runBlocking {
        servidor.enqueue(
            MockResponse()
                .setResponseCode(200)
                .addHeader(
                    "Content-Type",
                    "application/json"
                )
                .setBody("null")
        )

        val error = runCatching {
            repo.obtener("ciencias_facil")
        }.exceptionOrNull()

        assertTrue(error is ErrorProgresoRemoto)
        assertEquals(
            "El servidor no devolvió el progreso solicitado.",
            error?.message
        )
    }

    @Test
    fun detalleExitosoPeroSinCuerpoDevuelveError() = runBlocking {
        servidor.enqueue(
            MockResponse()
                .setResponseCode(200)
                .addHeader(
                    "Content-Type",
                    "application/json"
                )
                .setBody("null")
        )

        val error = runCatching {
            repo.obtenerDetalle()
        }.exceptionOrNull()

        assertTrue(error is ErrorProgresoRemoto)
        assertEquals(
            "El servidor no devolvió el progreso detallado.",
            error?.message
        )
    }

    @Test
    fun guardarDetalleError400MuestraMensaje() = runBlocking {
        servidor.enqueue(
            MockResponse().setResponseCode(400)
        )

        val error = runCatching {
            repo.guardarDetalle(
                "{\"progresoPorNivel\":{}}"
            )
        }.exceptionOrNull()

        assertTrue(error is ErrorProgresoRemoto)
        assertEquals(
            "El servidor rechazó los datos del progreso.",
            error?.message
        )

        val solicitud = servidor.takeRequest()
        assertEquals("PUT", solicitud.method)
        assertEquals(
            "/api/progreso/detalle",
            solicitud.path
        )
    }

    @Test
    fun guardarSinCuerpoDeRespuestaGeneraError() = runBlocking {
        servidor.enqueue(
            MockResponse()
                .setResponseCode(200)
                .addHeader(
                    "Content-Type",
                    "application/json"
                )
                .setBody("null")
        )

        val error = runCatching {
            repo.guardar(
                "historia_facil",
                2,
                1
            )
        }.exceptionOrNull()

        assertTrue(error is ErrorProgresoRemoto)
        assertEquals(
            "El servidor no devolvió el progreso guardado.",
            error?.message
        )
    }
}