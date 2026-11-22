package com.example.appmpazpro3_grupo6.repository

import com.example.appmpazpro3_grupo6.data.remote.ClienteEstudiantesMPAZ
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class MetasRepositoryMPAZCoberturaExtraTest {

    private lateinit var servidor: MockWebServer
    private lateinit var repo: MetasRepositoryMPAZ

    @Before
    fun preparar() {
        servidor = MockWebServer()
        servidor.start()
        repo = MetasRepositoryMPAZ(
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
    fun rechazaTituloVacioLargoYDescripcionExtensa() = runBlocking {
        val sinTitulo = runCatching {
            repo.crear("  ", "Detalle")
        }.exceptionOrNull()

        val tituloLargo = runCatching {
            repo.crear("A".repeat(121), "Detalle")
        }.exceptionOrNull()

        val descripcionLarga = runCatching {
            repo.crear("Meta", "X".repeat(501))
        }.exceptionOrNull()

        assertEquals(
            "Debes escribir el título de la meta.",
            sinTitulo?.message
        )
        assertEquals(
            "El título no puede superar 120 caracteres.",
            tituloLargo?.message
        )
        assertEquals(
            "La descripción no puede superar 500 caracteres.",
            descripcionLarga?.message
        )
        assertTrue(sinTitulo is ErrorMetasMPAZ)
        assertTrue(tituloLargo is ErrorMetasMPAZ)
        assertTrue(descripcionLarga is ErrorMetasMPAZ)
        assertEquals(0, servidor.requestCount)
    }

    @Test
    fun alCrearSeRecortanLosEspaciosYSeOmiteDescripcionVacia() = runBlocking {
        servidor.enqueue(
            MockResponse()
                .setResponseCode(201)
                .addHeader(
                    "Content-Type",
                    "application/json"
                )
                .setBody(
                    """{"id":4,"titulo":"Practicar","descripcion":null,"completada":false}"""
                )
        )

        val meta = repo.crear(
            "  Practicar  ",
            "  "
        )

        assertEquals(4L, meta.id)
        assertEquals("Practicar", meta.titulo)

        val solicitud = servidor.takeRequest()
        assertEquals("POST", solicitud.method)
        assertEquals("/api/metas", solicitud.path)

        val cuerpo = solicitud.body.readUtf8()

        assertTrue(
            cuerpo.contains("\"titulo\":\"Practicar\"")
        )
        assertFalse(
            cuerpo.contains("\"descripcion\":\"  \"")
        )
    }

    @Test
    fun listadoExitosoSinCuerpoSeRechaza() = runBlocking {
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
            repo.listar()
        }.exceptionOrNull()

        assertTrue(error is ErrorMetasMPAZ)
        assertEquals(
            "El servidor no devolvió las metas.",
            error?.message
        )
    }

    @Test
    fun actualizarSinPermisoDevuelveError403() = runBlocking {
        servidor.enqueue(
            MockResponse().setResponseCode(403)
        )

        val error = runCatching {
            repo.actualizar(
                8L,
                "Meta",
                "Detalle",
                true
            )
        }.exceptionOrNull()

        assertTrue(error is ErrorMetasMPAZ)
        assertEquals(
            "No tienes permiso para modificar esta meta.",
            error?.message
        )
        assertEquals(
            "PUT",
            servidor.takeRequest().method
        )
    }

    @Test
    fun eliminarMetaInexistenteDevuelveError404() = runBlocking {
        servidor.enqueue(
            MockResponse().setResponseCode(404)
        )

        val error = runCatching {
            repo.eliminar(99L)
        }.exceptionOrNull()

        assertTrue(error is ErrorMetasMPAZ)
        assertEquals(
            "La meta no existe o no pertenece a tu cuenta.",
            error?.message
        )

        val solicitud = servidor.takeRequest()
        assertEquals("DELETE", solicitud.method)
        assertEquals("/api/metas/99", solicitud.path)
    }

    @Test
    fun crearSinRespuestaDeDatosMuestraError() = runBlocking {
        servidor.enqueue(
            MockResponse()
                .setResponseCode(201)
                .addHeader(
                    "Content-Type",
                    "application/json"
                )
                .setBody("null")
        )

        val error = runCatching {
            repo.crear("Meta", "Descripcion")
        }.exceptionOrNull()

        assertTrue(error is ErrorMetasMPAZ)
        assertEquals(
            "No se recibieron los datos de la meta creada.",
            error?.message
        )
    }
}