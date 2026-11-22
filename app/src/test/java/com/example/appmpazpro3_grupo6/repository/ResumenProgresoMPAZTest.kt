package com.example.appmpazpro3_grupo6.repository

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class ResumenProgresoMPAZTest {

    @Test
    fun estudianteNuevoComienzaEnCero() {

        val progreso = ProgresoEstudiante()

        val resumen = ResumenProgresoMPAZ.calcular(
            progreso,
            "matematica_facil"
        )

        assertEquals(0, resumen.ejerciciosRespondidos)
        assertEquals(0, resumen.respuestasCorrectas)
        assertEquals(0, resumen.porcentajeAvance)
        assertEquals(0.0, resumen.porcentajeAcierto, 0.01)
    }

    @Test
    fun tresPreguntasDosCorrectas() {

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

        val resumen = ResumenProgresoMPAZ.calcular(
            progreso,
            "matematica_facil"
        )

        assertEquals(3, resumen.ejerciciosRespondidos)
        assertEquals(2, resumen.respuestasCorrectas)
        assertEquals(60, resumen.porcentajeAvance)
        assertEquals(66.67, resumen.porcentajeAcierto, 0.01)
    }

    @Test
    fun repetirPreguntaActualizaUltimoResultado() {

        val progreso = ProgresoEstudiante(
            progresoPorNivel = mapOf(
                "lenguaje_facil" to 1
            ),
            resultados = mapOf(
                "lenguaje_facil" to mapOf(
                    1 to ResultadoEjercicio(
                        intentos = 2,
                        historial = listOf(false, true)
                    )
                )
            )
        )

        val resumen = ResumenProgresoMPAZ.calcular(
            progreso,
            "lenguaje_facil"
        )

        assertEquals(1, resumen.ejerciciosRespondidos)
        assertEquals(1, resumen.respuestasCorrectas)
        assertEquals(20, resumen.porcentajeAvance)
        assertEquals(100.0, resumen.porcentajeAcierto, 0.01)
    }

    @Test
    fun reiniciarNivelDevuelveCero() {

        val progreso = ProgresoEstudiante(
            progresoPorNivel = mapOf(
                "matematica_facil" to 0,
                "matematica_medio" to 2
            )
        )

        val facil = ResumenProgresoMPAZ.calcular(
            progreso,
            "matematica_facil"
        )

        val medio = ResumenProgresoMPAZ.calcular(
            progreso,
            "matematica_medio"
        )

        assertEquals(0, facil.ejerciciosRespondidos)
        assertEquals(0, facil.respuestasCorrectas)
        assertEquals(2, medio.ejerciciosRespondidos)
    }

    @Test
    fun existenDoceNiveles() {

        val resumenes = ResumenProgresoMPAZ.calcularTodos(
            ProgresoEstudiante()
        )

        assertEquals(12, resumenes.size)

        assertTrue(
            resumenes.any {
                it.asignaturaNivel == "matematica_facil"
            }
        )

        assertTrue(
            resumenes.any {
                it.asignaturaNivel == "historia_dificil"
            }
        )
    }

    @Test
    fun rechazaDificultadNoValida() {

        assertThrows(
            IllegalArgumentException::class.java
        ) {
            ResumenProgresoMPAZ.calcular(
                ProgresoEstudiante(),
                "matematica_experto"
            )
        }
    }
}