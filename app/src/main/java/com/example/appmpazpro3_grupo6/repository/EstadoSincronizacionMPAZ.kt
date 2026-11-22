package com.example.appmpazpro3_grupo6.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import java.security.MessageDigest
import java.util.Locale

private val Context.estadoSincronizacionDataStore by preferencesDataStore(
    name = "mpaz_estado_sincronizacion_v1"
)

class EstadoSincronizacionMPAZ(context: Context) {

    private val contexto = context.applicationContext

    private fun claveCuenta(correo: String): String {
        val normalizado = correo.trim().lowercase(Locale.ROOT)
        return calcularHash(normalizado)
    }

    private fun claveVersion(correo: String) =
        stringPreferencesKey(
            "version_${claveCuenta(correo)}"
        )

    fun calcularHash(texto: String): String {
        val bytes = MessageDigest.getInstance("SHA-256")
            .digest(texto.toByteArray(Charsets.UTF_8))

        return bytes.joinToString("") {
            (it.toInt() and 255)
                .toString(16)
                .padStart(2, '0')
        }
    }

    suspend fun obtenerVersion(correo: String): String? {
        return contexto.estadoSincronizacionDataStore
            .data
            .first()[claveVersion(correo)]
    }

    suspend fun guardarVersion(
        correo: String,
        version: String?
    ) {
        contexto.estadoSincronizacionDataStore.edit { datos ->
            val clave = claveVersion(correo)

            if (version == null) {
                datos.remove(clave)
            } else {
                datos[clave] = version
            }
        }
    }
}