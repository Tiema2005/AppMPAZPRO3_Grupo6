package com.example.appmpazpro3_grupo6.repository

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import kotlin.math.roundToInt

private val Context.progresoDataStore by preferencesDataStore(
    name = "progreso_materias_mpaz"
)

private val idsAsignaturas = listOf(
    "lenguaje",
    "matematica",
    "ciencias",
    "historia"
)

private fun leerHistorial(valor: String?): List<Boolean> {
    if (valor.isNullOrBlank()) return emptyList()

    return valor.split(",").mapNotNull { respuesta ->
        when (respuesta) {
            "1" -> true
            "0" -> false
            else -> null
        }
    }
}

data class ResultadoEjercicio(
    val intentos: Int = 0,
    val historial: List<Boolean> = emptyList()
) {
    val respondido: Boolean
        get() = intentos > 0

    val ultimaRespuestaCorrecta: Boolean?
        get() = historial.lastOrNull()

    val aciertosEnHistorial: Int
        get() = historial.count { it }
}

data class RendimientoAsignatura(
    val ejerciciosEvaluados: Int = 0,
    val respuestasCorrectas: Int = 0
) {
    val porcentaje: Int?
        get() = if (ejerciciosEvaluados == 0) {
            null
        } else {
            (
                    respuestasCorrectas * 100f /
                            ejerciciosEvaluados
                    ).roundToInt()
        }
}

data class ProgresoEstudiante(
    val lenguaje: Int = 0,
    val matematica: Int = 0,
    val ciencias: Int = 0,
    val historia: Int = 0,

    val resultados: Map<String, Map<Int, ResultadoEjercicio>> =
        emptyMap()
) {

    fun completados(asignaturaId: String): Int {
        val cantidad = when (asignaturaId) {
            "lenguaje" -> lenguaje
            "matematica" -> matematica
            "ciencias" -> ciencias
            "historia" -> historia
            else -> 0
        }

        return cantidad.coerceIn(0, 3)
    }

    fun porcentaje(asignaturaId: String): Int {
        return (
                completados(asignaturaId) * 100f / 3
                ).toInt()
    }

    val totalCompletados: Int
        get() = lenguaje.coerceIn(0, 3) +
                matematica.coerceIn(0, 3) +
                ciencias.coerceIn(0, 3) +
                historia.coerceIn(0, 3)

    val progresoGeneral: Float
        get() = totalCompletados / 12f

    val porcentajeGeneral: Int
        get() = (progresoGeneral * 100).toInt()

    fun porcentajesPorAsignatura(): Map<String, Int> {
        return linkedMapOf(
            "Lenguaje y Comunicación" to porcentaje("lenguaje"),
            "Matemática" to porcentaje("matematica"),
            "Ciencias Naturales" to porcentaje("ciencias"),
            "Historia y Geografía" to porcentaje("historia")
        )
    }

    fun resultadoEjercicio(
        asignaturaId: String,
        numeroEjercicio: Int
    ): ResultadoEjercicio {
        return resultados[asignaturaId]
            ?.get(numeroEjercicio)
            ?: ResultadoEjercicio()
    }

    fun rendimiento(
        asignaturaId: String
    ): RendimientoAsignatura {

        val ejercicios = resultados[asignaturaId]
            ?.values
            .orEmpty()

        val evaluados = ejercicios.filter {
            it.ultimaRespuestaCorrecta != null
        }

        return RendimientoAsignatura(
            ejerciciosEvaluados = evaluados.size,
            respuestasCorrectas = evaluados.count {
                it.ultimaRespuestaCorrecta == true
            }
        )
    }

    fun porcentajeRendimiento(
        asignaturaId: String
    ): Int? {
        return rendimiento(asignaturaId).porcentaje
    }

    val rendimientoGeneral: RendimientoAsignatura
        get() {
            val resumenes = idsAsignaturas.map { rendimiento(it) }

            return RendimientoAsignatura(
                ejerciciosEvaluados = resumenes.sumOf {
                    it.ejerciciosEvaluados
                },
                respuestasCorrectas = resumenes.sumOf {
                    it.respuestasCorrectas
                }
            )
        }

    val porcentajeRendimientoGeneral: Int?
        get() = rendimientoGeneral.porcentaje
}

class ProgresoRepository(context: Context) {

    private val contexto = context.applicationContext

    private val claves = mapOf(
        "lenguaje" to intPreferencesKey("ejercicios_lenguaje"),
        "matematica" to intPreferencesKey("ejercicios_matematica"),
        "ciencias" to intPreferencesKey("ejercicios_ciencias"),
        "historia" to intPreferencesKey("ejercicios_historia")
    )

    private fun claveIntentos(
        asignaturaId: String,
        numeroEjercicio: Int
    ) = intPreferencesKey(
        "intentos_${asignaturaId}_$numeroEjercicio"
    )

    private fun claveHistorial(
        asignaturaId: String,
        numeroEjercicio: Int
    ) = stringPreferencesKey(
        "historial_${asignaturaId}_$numeroEjercicio"
    )

    private fun obtenerResultados(
        preferencias: Preferences
    ): Map<String, Map<Int, ResultadoEjercicio>> {

        return idsAsignaturas.associateWith { asignaturaId ->

            (1..3).associateWith { numeroEjercicio ->

                ResultadoEjercicio(
                    intentos = preferencias[
                        claveIntentos(
                            asignaturaId,
                            numeroEjercicio
                        )
                    ] ?: 0,

                    historial = leerHistorial(
                        preferencias[
                            claveHistorial(
                                asignaturaId,
                                numeroEjercicio
                            )
                        ]
                    )
                )
            }
        }
    }

    val progreso: Flow<ProgresoEstudiante> =
        contexto.progresoDataStore.data
            .catch { error ->
                if (error is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw error
                }
            }
            .map { preferencias ->

                ProgresoEstudiante(
                    lenguaje = preferencias[
                        claves.getValue("lenguaje")
                    ] ?: 0,

                    matematica = preferencias[
                        claves.getValue("matematica")
                    ] ?: 0,

                    ciencias = preferencias[
                        claves.getValue("ciencias")
                    ] ?: 0,

                    historia = preferencias[
                        claves.getValue("historia")
                    ] ?: 0,

                    resultados = obtenerResultados(preferencias)
                )
            }

    suspend fun registrarEjercicioRespondido(
        asignaturaId: String,
        ejerciciosRespondidos: Int
    ) {
        val clave = claves[asignaturaId] ?: return

        val cantidad = ejerciciosRespondidos.coerceIn(0, 3)

        contexto.progresoDataStore.edit { preferencias ->
            val anterior = preferencias[clave] ?: 0

            preferencias[clave] = maxOf(
                anterior,
                cantidad
            )
        }
    }

    suspend fun registrarRespuesta(
        asignaturaId: String,
        numeroEjercicio: Int,
        esCorrecta: Boolean
    ) {
        val claveProgreso = claves[asignaturaId] ?: return

        if (numeroEjercicio !in 1..3) return

        val claveIntentosActual = claveIntentos(
            asignaturaId,
            numeroEjercicio
        )

        val claveHistorialActual = claveHistorial(
            asignaturaId,
            numeroEjercicio
        )

        contexto.progresoDataStore.edit { preferencias ->

            val progresoAnterior =
                preferencias[claveProgreso] ?: 0

            preferencias[claveProgreso] = maxOf(
                progresoAnterior,
                numeroEjercicio
            ).coerceIn(0, 3)

            val intentosAnteriores =
                preferencias[claveIntentosActual] ?: 0

            preferencias[claveIntentosActual] =
                if (intentosAnteriores < Int.MAX_VALUE) {
                    intentosAnteriores + 1
                } else {
                    Int.MAX_VALUE
                }

            val historialAnterior = leerHistorial(
                preferencias[claveHistorialActual]
            )

            val historialNuevo = (
                    historialAnterior + esCorrecta
                    ).takeLast(30)

            preferencias[claveHistorialActual] =
                historialNuevo.joinToString(",") {
                    if (it) "1" else "0"
                }
        }
    }
}