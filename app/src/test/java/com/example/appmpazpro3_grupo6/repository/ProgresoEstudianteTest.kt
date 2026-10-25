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

        assertEquals(
            0,
            progreso.totalCompletados
        )

        assertEquals(
            0,
            progreso.porcentajeGeneral
        )

        assertEquals(
            0f,
            progreso.progresoGeneral,
            0.001f
        )
    }

    @Test
    fun unEjercicioCompletadoRepresenta33Porciento() {

        val progreso = ProgresoEstudiante(
            matematica = 1
        )

        assertEquals(
            1,
            progreso.completados("matematica")
        )

        assertEquals(
            33,
            progreso.porcentaje("matematica")
        )
    }

    @Test
    fun dosEjerciciosCompletadosRepresentan66Porciento() {

        val progreso = ProgresoEstudiante(
            lenguaje = 2
        )

        assertEquals(
            66,
            progreso.porcentaje("lenguaje")
        )
    }

    @Test
    fun doceEjerciciosCompletadosRepresentan100Porciento() {

        val progreso = ProgresoEstudiante(
            lenguaje = 3,
            matematica = 3,
            ciencias = 3,
            historia = 3
        )

        assertEquals(
            12,
            progreso.totalCompletados
        )

        assertEquals(
            100,
            progreso.porcentajeGeneral
        )

        assertEquals(
            1f,
            progreso.progresoGeneral,
            0.001f
        )
    }

    @Test
    fun avanceNuncaSupera100Porciento() {

        val progreso = ProgresoEstudiante(
            lenguaje = 10,
            matematica = 4,
            ciencias = 3,
            historia = 9
        )

        assertEquals(
            12,
            progreso.totalCompletados
        )

        assertEquals(
            100,
            progreso.porcentajeGeneral
        )

        assertEquals(
            100,
            progreso.porcentaje("lenguaje")
        )
    }

    @Test
    fun dosCorrectasDeTresDan67Porciento() {

        val progreso = ProgresoEstudiante(
            matematica = 3,

            resultados = mapOf(
                "matematica" to mapOf(

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

        assertEquals(
            100,
            progreso.porcentaje("matematica")
        )

        assertEquals(
            67,
            progreso.porcentajeRendimiento("matematica")
        )

        val rendimiento = progreso.rendimiento(
            "matematica"
        )

        assertEquals(
            3,
            rendimiento.ejerciciosEvaluados
        )

        assertEquals(
            2,
            rendimiento.respuestasCorrectas
        )
    }

    @Test
    fun sinRespuestasCalificadasElRendimientoEsNulo() {

        val progreso = ProgresoEstudiante(
            matematica = 3
        )

        assertNull(
            progreso.porcentajeRendimiento("matematica")
        )

        assertNull(
            progreso.porcentajeRendimientoGeneral
        )

        assertEquals(
            100,
            progreso.porcentaje("matematica")
        )
    }

    @Test
    fun rendimientoUsaUltimaRespuestaRegistrada() {

        val progreso = ProgresoEstudiante(
            matematica = 1,

            resultados = mapOf(
                "matematica" to mapOf(

                    1 to ResultadoEjercicio(
                        intentos = 2,
                        historial = listOf(false, true)
                    )
                )
            )
        )

        val resultado = progreso.resultadoEjercicio(
            "matematica",
            1
        )

        assertEquals(
            2,
            resultado.intentos
        )

        assertTrue(
            resultado.respondido
        )

        assertEquals(
            true,
            resultado.ultimaRespuestaCorrecta
        )

        assertEquals(
            100,
            progreso.porcentajeRendimiento("matematica")
        )
    }

    @Test
    fun rendimientoGeneralCombinaLasAsignaturas() {

        val progreso = ProgresoEstudiante(
            lenguaje = 1,
            matematica = 1,

            resultados = mapOf(

                "lenguaje" to mapOf(
                    1 to ResultadoEjercicio(
                        intentos = 1,
                        historial = listOf(true)
                    )
                ),

                "matematica" to mapOf(
                    1 to ResultadoEjercicio(
                        intentos = 1,
                        historial = listOf(false)
                    )
                )
            )
        )

        val resumen = progreso.rendimientoGeneral

        assertEquals(
            2,
            resumen.ejerciciosEvaluados
        )

        assertEquals(
            1,
            resumen.respuestasCorrectas
        )

        assertEquals(
            50,
            progreso.porcentajeRendimientoGeneral
        )
    }

    @Test
    fun ejercicioSinIntentosEntregaResultadoVacio() {

        val progreso = ProgresoEstudiante()

        val resultado = progreso.resultadoEjercicio(
            "historia",
            2
        )

        assertEquals(
            0,
            resultado.intentos
        )

        assertFalse(
            resultado.respondido
        )

        assertTrue(
            resultado.historial.isEmpty()
        )

        assertNull(
            resultado.ultimaRespuestaCorrecta
        )
    }
}