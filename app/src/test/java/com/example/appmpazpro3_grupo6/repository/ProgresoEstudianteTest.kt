package com.example.appmpazpro3_grupo6.repository

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ProgresoEstudianteTest {

    @Test
    fun estudianteNuevoComienzaEnCero() {
        val progreso = ProgresoEstudiante()
        assertEquals(0, progreso.totalCompletados)
        assertEquals(0, progreso.porcentajeGeneral)
        assertEquals(0f, progreso.progresoGeneral, 0.001f)
        assertEquals(0, progreso.completados("matematica_facil"))
    }

    @Test
    fun dosRespuestasDeCincoDan40PorcientoEnUnNivel() {
        val progreso = ProgresoEstudiante(
            progresoPorNivel = mapOf("matematica_facil" to 2)
        )
        assertEquals(2, progreso.completados("matematica_facil"))
        assertEquals(40, progreso.porcentaje("matematica_facil"))
        assertEquals(2, progreso.completados("matematica"))
    }

    @Test
    fun cadaNivelTieneProgresoIndependiente() {
        val progreso = ProgresoEstudiante(
            progresoPorNivel = mapOf(
                "matematica_facil" to 5,
                "matematica_medio" to 3,
                "matematica_dificil" to 0
            )
        )
        assertEquals(5, progreso.completados("matematica_facil"))
        assertEquals(3, progreso.completados("matematica_medio"))
        assertEquals(0, progreso.completados("matematica_dificil"))
        assertEquals(8, progreso.completados("matematica"))
        assertEquals(53, progreso.porcentaje("matematica"))
    }

    @Test
    fun doceNivelesCompletosRepresentan60EjerciciosY100Porciento() {
        val progresoPorNivel = listOf("lenguaje", "matematica", "ciencias", "historia")
            .flatMap { materia ->
                listOf("facil", "medio", "dificil").map { nivel ->
                    "${materia}_${nivel}" to 5
                }
            }.toMap()
        val progreso = ProgresoEstudiante(progresoPorNivel = progresoPorNivel)
        assertEquals(60, progreso.totalCompletados)
        assertEquals(100, progreso.porcentajeGeneral)
        assertEquals(1f, progreso.progresoGeneral, 0.001f)
        assertEquals(15, progreso.completados("lenguaje"))
    }

    @Test
    fun avanceNuncaSuperaEl100Porciento() {
        val progreso = ProgresoEstudiante(
            progresoPorNivel = mapOf(
                "lenguaje_facil" to 100,
                "lenguaje_medio" to 90,
                "lenguaje_dificil" to 20
            )
        )
        assertEquals(15, progreso.completados("lenguaje"))
        assertEquals(100, progreso.porcentaje("lenguaje"))
        assertEquals(15, progreso.totalCompletados)
    }

    @Test
    fun tresAciertosDeCincoDan60PorcientoDeRendimiento() {
        val progreso = ProgresoEstudiante(
            progresoPorNivel = mapOf("ciencias_medio" to 5),
            resultados = mapOf(
                "ciencias_medio" to (1..5).associateWith { indice ->
                    ResultadoEjercicio(
                        intentos = 1,
                        historial = listOf(indice <= 3)
                    )
                }
            )
        )
        assertEquals(60, progreso.porcentajeRendimiento("ciencias_medio"))
        assertEquals(60, progreso.porcentajeRendimiento("ciencias"))
        assertEquals(5, progreso.rendimiento("ciencias").ejerciciosEvaluados)
        assertEquals(3, progreso.rendimiento("ciencias").respuestasCorrectas)
    }

    @Test
    fun rendimientoSeparaLosNiveles() {
        val progreso = ProgresoEstudiante(
            resultados = mapOf(
                "historia_facil" to mapOf(
                    1 to ResultadoEjercicio(1, listOf(true))
                ),
                "historia_dificil" to mapOf(
                    1 to ResultadoEjercicio(1, listOf(false))
                )
            )
        )
        assertEquals(100, progreso.porcentajeRendimiento("historia_facil"))
        assertEquals(0, progreso.porcentajeRendimiento("historia_dificil"))
        assertEquals(50, progreso.porcentajeRendimiento("historia"))
        assertEquals(50, progreso.porcentajeRendimientoGeneral)
    }

    @Test
    fun sinRespuestasCalificadasRendimientoEsNulo() {
        val progreso = ProgresoEstudiante()
        assertNull(progreso.porcentajeRendimiento("matematica"))
        assertNull(progreso.porcentajeRendimientoGeneral)
    }

    @Test
    fun repetirPreguntaUtilizaLaUltimaRespuesta() {
        val progreso = ProgresoEstudiante(
            resultados = mapOf(
                "lenguaje_dificil" to mapOf(
                    1 to ResultadoEjercicio(
                        intentos = 2,
                        historial = listOf(false, true)
                    )
                )
            )
        )
        val resultado = progreso.resultadoEjercicio("lenguaje_dificil", 1)
        assertEquals(2, resultado.intentos)
        assertTrue(resultado.respondido)
        assertEquals(true, resultado.ultimaRespuestaCorrecta)
        assertEquals(100, progreso.porcentajeRendimiento("lenguaje_dificil"))
    }

    @Test
    fun ejercicioSinIntentosDevuelveResultadoVacio() {
        val resultado = ProgresoEstudiante()
            .resultadoEjercicio("historia_medio", 2)
        assertEquals(0, resultado.intentos)
        assertFalse(resultado.respondido)
        assertTrue(resultado.historial.isEmpty())
        assertNull(resultado.ultimaRespuestaCorrecta)
    }
}