package com.example.appmpazpro3_grupo6.repository

import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import java.util.UUID

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class ProgresoCuentaRepositoryCoberturaTest {

    private fun correo() = "lt71-${UUID.randomUUID()}@ejemplo.cl"
    private fun repositorio() = ProgresoCuentaRepository(
        RuntimeEnvironment.getApplication()
    )

    @Test
    fun cuentaNuevaTieneDoceSesionesYNoTienePendientes() = runBlocking {
        val repo = repositorio()
        val email = correo()
        val estado = repo.obtener(email)

        assertEquals(0, estado.totalCompletados)
        assertEquals(12, estado.sesionesPractica?.size)
        assertFalse(repo.hayCambiosPendientes(email))
    }

    @Test
    fun contadorNoRetrocedeYSeLimitaACinco() = runBlocking {
        val repo = repositorio()
        val email = correo()
        repo.registrarEjercicioRespondido(email, "matematica_facil", 3)
        repo.registrarEjercicioRespondido(email, "matematica_facil", 1)
        assertEquals(3, repo.obtener(email).completados("matematica_facil"))

        repo.registrarEjercicioRespondido(email, "matematica_facil", 100)
        assertEquals(5, repo.obtener(email).completados("matematica_facil"))
        assertTrue(repo.hayCambiosPendientes(email))

        val versionEnviada = repo.exportar(email)
        repo.confirmarSincronizacion(email, versionEnviada)
        assertFalse(repo.hayCambiosPendientes(email))

        repo.registrarEjercicioRespondido(email, "matematica_facil", 5)
        assertTrue(repo.hayCambiosPendientes(email))
        repo.confirmarSincronizacion(email, "{\"version\":\"antigua\"}")
        assertTrue(repo.hayCambiosPendientes(email))
    }

    @Test
    fun registraReintentosYLimitaHistorialATreinta() = runBlocking {
        val repo = repositorio()
        val email = correo()
        repeat(32) { intento ->
            repo.registrarRespuesta(
                correo = email,
                asignaturaId = "lenguaje_facil",
                numeroEjercicio = 1,
                esCorrecta = intento % 2 == 0
            )
        }

        val resultado = repo.obtener(email)
            .resultadoEjercicio("lenguaje_facil", 1)
        assertEquals(32, resultado.intentos)
        assertEquals(30, resultado.historial.size)
        assertEquals(false, resultado.ultimaRespuestaCorrecta)
        assertEquals(1, repo.obtener(email).completados("lenguaje_facil"))
    }

    @Test
    fun importaNormalizaYRechazaJsonVacio() = runBlocking {
        val repo = repositorio()
        val email = correo()
        val json = Gson().toJson(
            ProgresoEstudiante(
                progresoPorNivel = mapOf("ciencias_dificil" to 99),
                resultados = emptyMap(),
                sesionesPractica = emptyMap()
            )
        )
        repo.importar(email, json)

        assertEquals(5, repo.obtener(email).completados("ciencias_dificil"))
        assertFalse(repo.hayCambiosPendientes(email))

        val error = runCatching { repo.importar(email, "  ") }.exceptionOrNull()
        assertTrue(error is IllegalArgumentException)
        val errorTamano = runCatching {
            repo.importar(email, "x".repeat(250001))
        }.exceptionOrNull()
        assertTrue(errorTamano is IllegalArgumentException)
    }

    @Test
    fun guardarSesionRegistraAciertoYPermiteReinicio() = runBlocking {
        val repo = repositorio()
        val email = correo()
        val inicial = SesionPractica()
        val respondida = SesionPractica(
            numeroEjercicio = 0,
            respuestaSeleccionada = "B",
            aciertos = 1
        )
        repo.guardarSesion(email, "historia_facil", inicial, respondida, true)

        val estado = repo.obtener(email)
        assertEquals(1, estado.completados("historia_facil"))
        assertEquals(1, estado.resultadoEjercicio("historia_facil", 1).intentos)
        assertEquals(true, estado.resultadoEjercicio("historia_facil", 1).ultimaRespuestaCorrecta)
        assertEquals("B", estado.sesionesPractica?.get("historia_facil")?.respuestaSeleccionada)

        val terminada = respondida.copy(terminado = true)
        repo.guardarSesion(email, "historia_facil", respondida, terminada, null)
        repo.guardarSesion(email, "historia_facil", terminada, SesionPractica(), null)

        val reiniciada = repo.obtener(email)
        assertEquals(0, reiniciada.completados("historia_facil"))
        assertEquals(0, reiniciada.resultadoEjercicio("historia_facil", 1).intentos)
        assertEquals(SesionPractica(), reiniciada.sesionesPractica?.get("historia_facil"))
    }

    @Test
    fun ignoraNivelInvalidoYSesionDesactualizada() = runBlocking {
        val repo = repositorio()
        val email = correo()

        repo.registrarEjercicioRespondido(email, "ingles_facil", 4)
        repo.registrarRespuesta(email, "matematica_facil", 6, true)
        repo.guardarSesion(
            correo = email,
            asignaturaId = "ciencias_facil",
            anterior = SesionPractica(numeroEjercicio = 3),
            siguiente = SesionPractica(numeroEjercicio = 4),
            esCorrecta = null
        )

        assertEquals(0, repo.obtener(email).totalCompletados)
        assertFalse(repo.hayCambiosPendientes(email))
    }
}