package com.example.appmpazpro3_grupo6.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(
    name = "preferencias_estudiante"
)

class EstadoDataStore(
    private val context: Context
) {
    private val MODO_ESTUDIO = booleanPreferencesKey(
        "modo_estudio"
    )

    suspend fun guardarEstado(activado: Boolean) {
        context.dataStore.edit { preferencias ->
            preferencias[MODO_ESTUDIO] = activado
        }
    }

    fun obtenerEstado(): Flow<Boolean> {
        return context.dataStore.data.map { preferencias ->
            preferencias[MODO_ESTUDIO] ?: false
        }
    }
}
