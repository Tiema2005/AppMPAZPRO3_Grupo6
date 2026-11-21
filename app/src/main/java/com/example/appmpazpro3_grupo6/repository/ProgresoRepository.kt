package com.example.appmpazpro3_grupo6.repository

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import kotlin.math.roundToInt

private val Context.progresoDataStore by preferencesDataStore(
    name = "progreso_materias_mpaz_niveles_v2"
)

private val asignaturas = listOf(
    "lenguaje",
    "matematica",
    "ciencias",
    "historia"
)

private val niveles = listOf(
    "facil",
    "medio",
    "dificil"
)

private val clavesPractica = asignaturas.flatMap { materia ->
    niveles.map { nivel ->
        "${materia}_${nivel}"
    }
}

private fun leerHistorial(valor: String?): List<Boolean> =
    valor?.split(",")?.mapNotNull {
        when (it) {
            "1" -> true
            "0" -> false
            else -> null
        }
    }.orEmpty()

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

data class SesionPractica(
    @SerializedName("ejercicio")
    val numeroEjercicio: Int = 0,

    @SerializedName("respuesta")
    val respuestaSeleccionada: String? = null,

    @SerializedName("aciertos")
    val aciertos: Int = 0,

    @SerializedName("terminada")
    val terminado: Boolean = false
)

data class ProgresoEstudiante(
    val progresoPorNivel: Map<String, Int> = emptyMap(),

    val resultados: Map<
            String,
            Map<Int, ResultadoEjercicio>
            > = emptyMap(),

    val sesionesPractica: Map<String, SesionPractica>? = null
) {

    val lenguaje get() = completados("lenguaje")
    val matematica get() = completados("matematica")
    val ciencias get() = completados("ciencias")
    val historia get() = completados("historia")

    fun completados(id: String): Int {
        return if (id in asignaturas) {
            niveles.sumOf { nivel ->
                (
                        progresoPorNivel["${id}_${nivel}"] ?: 0
                        ).coerceIn(0, 5)
            }
        } else {
            (
                    progresoPorNivel[id] ?: 0
                    ).coerceIn(0, 5)
        }
    }

    fun porcentaje(id: String): Int {
        return if (id in asignaturas) {
            (
                    completados(id) * 100f / 15
                    ).roundToInt()
        } else {
            (
                    completados(id) * 100f / 5
                    ).roundToInt()
        }
    }

    val totalCompletados: Int
        get() = asignaturas.sumOf {
            completados(it)
        }

    val progresoGeneral: Float
        get() = totalCompletados / 60f

    val porcentajeGeneral: Int
        get() = (
                progresoGeneral * 100
                ).roundToInt()

    fun porcentajesPorAsignatura(): Map<String, Int> =
        linkedMapOf(
            "Lenguaje y Comunicación" to porcentaje("lenguaje"),
            "Matemática" to porcentaje("matematica"),
            "Ciencias Naturales" to porcentaje("ciencias"),
            "Historia y Geografía" to porcentaje("historia")
        )

    fun resultadoEjercicio(
        id: String,
        numeroEjercicio: Int
    ): ResultadoEjercicio {
        return resultados[id]?.get(numeroEjercicio)
            ?: ResultadoEjercicio()
    }

    fun rendimiento(id: String): RendimientoAsignatura {

        val seleccionadas = if (id in asignaturas) {
            niveles.map {
                "${id}_${it}"
            }
        } else {
            listOf(id)
        }

        val evaluados = seleccionadas
            .flatMap {
                resultados[it]?.values.orEmpty()
            }
            .filter {
                it.ultimaRespuestaCorrecta != null
            }

        return RendimientoAsignatura(
            ejerciciosEvaluados = evaluados.size,
            respuestasCorrectas = evaluados.count {
                it.ultimaRespuestaCorrecta == true
            }
        )
    }

    fun porcentajeRendimiento(id: String): Int? {
        return rendimiento(id).porcentaje
    }

    val rendimientoGeneral: RendimientoAsignatura
        get() {
            val datos = asignaturas.map {
                rendimiento(it)
            }

            return RendimientoAsignatura(
                ejerciciosEvaluados = datos.sumOf {
                    it.ejerciciosEvaluados
                },
                respuestasCorrectas = datos.sumOf {
                    it.respuestasCorrectas
                }
            )
        }

    val porcentajeRendimientoGeneral: Int?
        get() = rendimientoGeneral.porcentaje
}

class ProgresoRepository(context: Context) {

    private val contexto = context.applicationContext

    private val gson = Gson()

    private fun claveContador(id: String) =
        intPreferencesKey("ejercicios_$id")

    private fun claveSesion(id: String) =
        stringPreferencesKey("sesion_practica_$id")

    private fun claveIntentos(
        id: String,
        numero: Int
    ) = intPreferencesKey("intentos_${id}_$numero")

    private fun claveHistorial(
        id: String,
        numero: Int
    ) = stringPreferencesKey("historial_${id}_$numero")

    private fun leerSesion(
        preferencias: Preferences,
        id: String
    ): SesionPractica {

        val json = preferencias[claveSesion(id)]
            ?: return SesionPractica()

        return runCatching {
            gson.fromJson(
                json,
                SesionPractica::class.java
            )
        }.getOrNull() ?: SesionPractica()
    }

    private fun obtenerResultados(
        preferencias: Preferences
    ): Map<String, Map<Int, ResultadoEjercicio>> {

        return clavesPractica.associateWith { id ->
            (1..5).associateWith { numero ->

                ResultadoEjercicio(
                    intentos = preferencias[
                        claveIntentos(id, numero)
                    ] ?: 0,

                    historial = leerHistorial(
                        preferencias[
                            claveHistorial(id, numero)
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
                    progresoPorNivel =
                        clavesPractica.associateWith { id ->
                            (
                                    preferencias[claveContador(id)]
                                        ?: 0
                                    ).coerceIn(0, 5)
                        },

                    resultados = obtenerResultados(
                        preferencias
                    ),

                    sesionesPractica =
                        clavesPractica.associateWith { id ->
                            leerSesion(preferencias, id)
                        }
                )
            }

    suspend fun registrarEjercicioRespondido(
        id: String,
        ejerciciosRespondidos: Int
    ) {

        if (id !in clavesPractica) return

        contexto.progresoDataStore.edit { preferencias ->

            preferencias[claveContador(id)] = maxOf(
                preferencias[claveContador(id)] ?: 0,
                ejerciciosRespondidos.coerceIn(0, 5)
            )
        }
    }

    suspend fun guardarSesion(
        asignaturaId: String,
        anterior: SesionPractica,
        siguiente: SesionPractica,
        esCorrecta: Boolean?
    ) {

        if (asignaturaId !in clavesPractica) return

        if (esCorrecta != null) {

            registrarRespuesta(
                asignaturaId = asignaturaId,
                numeroEjercicio = anterior.numeroEjercicio + 1,
                esCorrecta = esCorrecta,
                cambioSesion = anterior to siguiente
            )

            return
        }

        contexto.progresoDataStore.edit { preferencias ->

            val guardada = leerSesion(
                preferencias,
                asignaturaId
            )

            if (
                anterior.terminado &&
                siguiente == SesionPractica()
            ) {

                if (!guardada.terminado) {
                    return@edit
                }

                preferencias[claveSesion(asignaturaId)] =
                    gson.toJson(SesionPractica())

                preferencias[claveContador(asignaturaId)] = 0

                for (numero in 1..5) {

                    preferencias.remove(
                        claveIntentos(
                            asignaturaId,
                            numero
                        )
                    )

                    preferencias.remove(
                        claveHistorial(
                            asignaturaId,
                            numero
                        )
                    )
                }

                return@edit
            }

            if (guardada != anterior) {
                return@edit
            }

            preferencias[claveSesion(asignaturaId)] =
                gson.toJson(siguiente)
        }
    }

    suspend fun registrarRespuesta(
        asignaturaId: String,
        numeroEjercicio: Int,
        esCorrecta: Boolean,
        cambioSesion: Pair<
                SesionPractica,
                SesionPractica
                >? = null
    ) {

        if (
            asignaturaId !in clavesPractica ||
            numeroEjercicio !in 1..5
        ) {
            return
        }

        contexto.progresoDataStore.edit { preferencias ->

            if (cambioSesion != null) {

                val (anterior, siguiente) = cambioSesion

                if (
                    leerSesion(preferencias, asignaturaId) != anterior ||
                    anterior.terminado ||
                    anterior.respuestaSeleccionada != null ||
                    anterior.numeroEjercicio + 1 != numeroEjercicio ||
                    siguiente.respuestaSeleccionada == null
                ) {
                    return@edit
                }

                preferencias[claveSesion(asignaturaId)] =
                    gson.toJson(siguiente)
            }

            preferencias[claveContador(asignaturaId)] =
                maxOf(
                    preferencias[claveContador(asignaturaId)]
                        ?: 0,
                    numeroEjercicio
                ).coerceIn(0, 5)

            val intentos = claveIntentos(
                asignaturaId,
                numeroEjercicio
            )

            preferencias[intentos] =
                (preferencias[intentos] ?: 0).let { cantidad ->
                    if (cantidad == Int.MAX_VALUE) {
                        cantidad
                    } else {
                        cantidad + 1
                    }
                }

            val historial = claveHistorial(
                asignaturaId,
                numeroEjercicio
            )

            preferencias[historial] =
                (
                        leerHistorial(
                            preferencias[historial]
                        ) + esCorrecta
                        )
                    .takeLast(30)
                    .joinToString(",") {
                        if (it) "1" else "0"
                    }
        }
    }
}