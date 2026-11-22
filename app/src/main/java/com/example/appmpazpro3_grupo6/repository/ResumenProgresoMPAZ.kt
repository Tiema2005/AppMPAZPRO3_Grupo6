package com.example.appmpazpro3_grupo6.repository

data class ResumenNivelMPAZ(
    val asignaturaNivel: String,
    val ejerciciosRespondidos: Int,
    val respuestasCorrectas: Int,
    val porcentajeAvance: Int,
    val porcentajeAcierto: Double
)

object ResumenProgresoMPAZ {

    private val asignaturas = listOf(
        "lenguaje",
        "matematica",
        "ciencias",
        "historia"
    )

    private val dificultades = listOf(
        "facil",
        "medio",
        "dificil"
    )

    val clavesValidas: List<String> =
        asignaturas.flatMap { asignatura ->
            dificultades.map { dificultad ->
                "${asignatura}_${dificultad}"
            }
        }

    fun calcular(
        progreso: ProgresoEstudiante,
        asignaturaNivel: String
    ): ResumenNivelMPAZ {

        require(asignaturaNivel in clavesValidas) {
            "Asignatura o dificultad no válida."
        }

        val respondidos = (
                progreso.progresoPorNivel[asignaturaNivel] ?: 0
                ).coerceIn(0, 5)

        val correctos = (1..respondidos).count { numero ->
            progreso.resultadoEjercicio(
                asignaturaNivel,
                numero
            ).ultimaRespuestaCorrecta == true
        }

        val avance = respondidos * 20

        val acierto = if (respondidos == 0) {
            0.0
        } else {
            kotlin.math.round(
                correctos * 10000.0 / respondidos
            ) / 100.0
        }

        return ResumenNivelMPAZ(
            asignaturaNivel = asignaturaNivel,
            ejerciciosRespondidos = respondidos,
            respuestasCorrectas = correctos,
            porcentajeAvance = avance,
            porcentajeAcierto = acierto
        )
    }

    fun calcularTodos(
        progreso: ProgresoEstudiante
    ): List<ResumenNivelMPAZ> {

        return clavesValidas.map { clave ->
            calcular(progreso, clave)
        }
    }
}