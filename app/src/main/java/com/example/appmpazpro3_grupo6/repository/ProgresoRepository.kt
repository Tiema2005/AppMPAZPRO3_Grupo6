package com.example.appmpazpro3_grupo6.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.progresoDataStore by preferencesDataStore(
    name = "progreso_materias_mpaz"
)

data class ProgresoEstudiante(
    val lenguaje: Int = 0,
    val matematica: Int = 0,
    val ciencias: Int = 0,
    val historia: Int = 0
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
        return (completados(asignaturaId) * 100f / 3).toInt()
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
}

class ProgresoRepository(context: Context) {

    private val contexto = context.applicationContext

    private val claves = mapOf(
        "lenguaje" to intPreferencesKey("ejercicios_lenguaje"),
        "matematica" to intPreferencesKey("ejercicios_matematica"),
        "ciencias" to intPreferencesKey("ejercicios_ciencias"),
        "historia" to intPreferencesKey("ejercicios_historia")
    )

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
                    ] ?: 0
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

            preferencias[clave] = maxOf(anterior, cantidad)
        }
    }
}