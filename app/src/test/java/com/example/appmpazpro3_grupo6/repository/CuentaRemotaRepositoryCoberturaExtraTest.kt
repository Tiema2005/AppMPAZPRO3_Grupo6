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

class CuentaRemotaRepositoryCoberturaExtraTest {

    private lateinit var servidor: MockWebServer
    private lateinit var repo: CuentaRemotaRepository

    @Before
    fun preparar() {
        servidor = MockWebServer()
        servidor.start()
        repo = CuentaRemotaRepository(
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
    fun registroDuplicadoDevuelveError409() = runBlocking {
        servidor.enqueue(
            MockResponse().setResponseCode(409)
        )

        val error = runCatching {
            repo.registrarCuenta(
                "Nombre",
                "  ALUMNO@EJEMPLO.CL  ",
                "Clave123"
            )
        }.exceptionOrNull()

        assertTrue(error is ErrorCuentaRemota)
        assertEquals(
            "Este correo ya está registrado.",
            error?.message
        )
        assertEquals(1, servidor.requestCount)

        val solicitud = servidor.takeRequest()
        assertEquals(
            "/api/estudiantes/registro",
            solicitud.path
        )
        assertTrue(
            solicitud.body.readUtf8()
                .contains("alumno@ejemplo.cl")
        )
    }

    @Test
    fun registroExitosoSinDatosGeneraError() = runBlocking {
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
            repo.registrarCuenta(
                "Nombre",
                "alumno@ejemplo.cl",
                "Clave123"
            )
        }.exceptionOrNull()

        assertTrue(error is ErrorCuentaRemota)
        assertEquals(
            "El servidor no devolvió los datos de registro.",
            error?.message
        )
        assertEquals(1, servidor.requestCount)
    }

    @Test
    fun loginSinCuerpoGeneraError() = runBlocking {
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
            repo.iniciarSesion(
                "alumno@ejemplo.cl",
                "Clave123"
            )
        }.exceptionOrNull()

        assertTrue(error is ErrorCuentaRemota)
        assertEquals(
            "El servidor no devolvió los datos del estudiante.",
            error?.message
        )
    }

    @Test
    fun perfilSinSesionDevuelveError401() = runBlocking {
        servidor.enqueue(
            MockResponse().setResponseCode(401)
        )

        val error = runCatching {
            repo.obtenerPerfil()
        }.exceptionOrNull()

        assertTrue(error is ErrorCuentaRemota)
        assertEquals(
            "La sesión ha expirado. Inicia sesión nuevamente.",
            error?.message
        )
        assertEquals(
            "/api/estudiantes/perfil",
            servidor.takeRequest().path
        )
    }

    @Test
    fun perfilSinCuerpoExplicaElProblema() = runBlocking {
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
            repo.obtenerPerfil()
        }.exceptionOrNull()

        assertTrue(error is ErrorCuentaRemota)
        assertEquals(
            "No se recibieron los datos del perfil.",
            error?.message
        )
    }

    @Test
    fun cerrarSesionConRespuesta401SeConsideraCerrada() = runBlocking {
        servidor.enqueue(
            MockResponse().setResponseCode(401)
        )

        repo.cerrarSesion()

        assertEquals(
            "/api/estudiantes/logout",
            servidor.takeRequest().path
        )
    }

    @Test
    fun cerrarSesionConError500NotificaProblema() = runBlocking {
        servidor.enqueue(
            MockResponse().setResponseCode(500)
        )

        val error = runCatching {
            repo.cerrarSesion()
        }.exceptionOrNull()

        assertTrue(error is ErrorCuentaRemota)
        assertEquals(
            "No se pudo confirmar el cierre de sesión.",
            error?.message
        )
    }
}