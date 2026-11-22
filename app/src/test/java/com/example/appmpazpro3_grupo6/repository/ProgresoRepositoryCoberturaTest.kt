package com.example.appmpazpro3_grupo6.repository

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class ProgresoRepositoryCoberturaTest {

    private fun repositorio() = ProgresoRepository(
        RuntimeEnvironment.getApplication()
    )

    @Test
    fun progresoContieneDoceNivelesYDatosValidos() = runBlocking {

        // DataStore puede conservar datos de otras pruebas.
        // Comprobamos la estructura, no que todo sea cero.

        val progreso = repositorio().progreso.first()

        assertEquals(
            12,
            progreso.progresoPorNivel.size
        )

        assertEquals(
            12,
            progreso.sesionesPractica?.size
        )

        assertEquals(
            12,
            progreso.resultados.size
        )

        assertTrue(
            progreso.progresoPorNivel.values.all {
                it in 0..5
            }
        )

        assertEquals(
            5,
            progreso.resultados["matematica_facil"]?.size
        )

        assertTrue(
            progreso.resultadoEjercicio(
                "matematica_facil",
                1
            ).intentos >= 0
        )
    }

    @Test
    fun contadorMantieneMaximoYCincoComoLimite() = runBlocking {

        val repo = repositorio()

        repo.registrarEjercicioRespondido(
            "matematica_facil",
            3
        )

        repo.registrarEjercicioRespondido(
            "matematica_facil",
            1
        )

        assertEquals(
            3,
            repo.progreso.first()
                .completados("matematica_facil")
        )

        repo.registrarEjercicioRespondido(
            "matematica_facil",
            100
        )

        repo.registrarEjercicioRespondido(
            "nivel_inexistente",
            4
        )

        assertEquals(
            5,
            repo.progreso.first()
                .completados("matematica_facil")
        )
    }

    @Test
    fun reintentarEjercicioRegistraHistorialYAvance() = runBlocking {

        val repo = repositorio()

        repo.registrarRespuesta(
            "lenguaje_facil",
            1,
            false
        )

        repo.registrarRespuesta(
            "lenguaje_facil",
            1,
            true
        )

        repo.registrarRespuesta(
            "lenguaje_facil",
            8,
            true
        )

        val progreso = repo.progreso.first()

        val respuesta = progreso.resultadoEjercicio(
            "lenguaje_facil",
            1
        )

        assertEquals(2, respuesta.intentos)

        assertEquals(
            listOf(false, true),
            respuesta.historial
        )

        assertEquals(
            1,
            progreso.completados("lenguaje_facil")
        )

        assertEquals(
            100,
            progreso.porcentajeRendimiento("lenguaje_facil")
        )
    }

    @Test
    fun guardadoDeSesionYRespuestaExigeEstadoAnteriorCorrecto() = runBlocking {

        val repo = repositorio()

        val anterior = SesionPractica()

        val siguiente = SesionPractica(
            respuestaSeleccionada = "A"
        )

        repo.guardarSesion(
            "ciencias_facil",
            anterior,
            siguiente,
            true
        )

        val guardado = repo.progreso.first()

        assertEquals(
            1,
            guardado.resultadoEjercicio(
                "ciencias_facil",
                1
            ).intentos
        )

        assertEquals(
            siguiente,
            guardado.sesionesPractica?.get("ciencias_facil")
        )

        // Repetir con un estado anterior obsoleto
        // no debe registrar otra respuesta.

        repo.guardarSesion(
            "ciencias_facil",
            anterior,
            siguiente,
            true
        )

        assertEquals(
            1,
            repo.progreso.first()
                .resultadoEjercicio(
                    "ciencias_facil",
                    1
                ).intentos
        )
    }

    @Test
    fun reiniciarSesionTerminadaEliminaResultadosDelNivel() = runBlocking {

        val repo = repositorio()

        repo.registrarRespuesta(
            "historia_facil",
            1,
            true
        )

        val inicial = SesionPractica()

        val terminada = SesionPractica(
            numeroEjercicio = 4,
            terminado = true
        )

        repo.guardarSesion(
            "historia_facil",
            inicial,
            terminada,
            null
        )

        assertTrue(
            repo.progreso.first()
                .sesionesPractica
                ?.get("historia_facil")
                ?.terminado == true
        )

        repo.guardarSesion(
            "historia_facil",
            terminada,
            SesionPractica(),
            null
        )

        val reiniciada = repo.progreso.first()

        assertEquals(
            0,
            reiniciada.completados("historia_facil")
        )

        assertEquals(
            0,
            reiniciada.resultadoEjercicio(
                "historia_facil",
                1
            ).intentos
        )

        assertFalse(
            reiniciada.sesionesPractica
                ?.get("historia_facil")
                ?.terminado ?: true
        )
    }
}