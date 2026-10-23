package com.example.appmpazpro3_grupo6.repository

import android.content.Context
import android.util.Base64
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

private val Context.cuentaDataStore by preferencesDataStore(
    name = "cuenta_local_estudiante"
)

data class CuentaEstudiante(
    val nombre: String,
    val correo: String,
    val curso: String
)

class CuentaLocalRepository(context: Context) {

    private val contexto = context.applicationContext

    private object Claves {
        val NOMBRE = stringPreferencesKey("nombre")
        val CORREO = stringPreferencesKey("correo")
        val CURSO = stringPreferencesKey("curso")
        val SALT = stringPreferencesKey("salt")
        val HASH = stringPreferencesKey("hash")
    }

    suspend fun existeCuenta(): Boolean {

        val preferencias = contexto.cuentaDataStore
            .data.first()

        return preferencias[Claves.CORREO] != null
    }

    suspend fun registrarCuenta(
        nombre: String,
        correo: String,
        contrasena: String,
        curso: String
    ): Boolean {

        val salt = ByteArray(16)

        SecureRandom().nextBytes(salt)

        val hash = generarHash(
            contrasena = contrasena,
            salt = salt
        )

        var registrada = false

        contexto.cuentaDataStore.edit { preferencias ->

            if (preferencias[Claves.CORREO] == null) {

                preferencias[Claves.NOMBRE] = nombre.trim()

                preferencias[Claves.CORREO] =
                    correo.trim().lowercase()

                preferencias[Claves.CURSO] = curso

                preferencias[Claves.SALT] =
                    Base64.encodeToString(
                        salt,
                        Base64.NO_WRAP
                    )

                preferencias[Claves.HASH] =
                    Base64.encodeToString(
                        hash,
                        Base64.NO_WRAP
                    )

                registrada = true
            }
        }

        return registrada
    }

    suspend fun iniciarSesion(
        correo: String,
        contrasena: String
    ): CuentaEstudiante? {

        val preferencias = contexto.cuentaDataStore
            .data.first()

        val correoGuardado =
            preferencias[Claves.CORREO]
                ?: return null

        val saltGuardado =
            preferencias[Claves.SALT]
                ?: return null

        val hashGuardado =
            preferencias[Claves.HASH]
                ?: return null

        if (
            !correoGuardado.equals(
                correo.trim(),
                ignoreCase = true
            )
        ) {
            return null
        }

        val salt = Base64.decode(
            saltGuardado,
            Base64.NO_WRAP
        )

        val hashEsperado = Base64.decode(
            hashGuardado,
            Base64.NO_WRAP
        )

        val hashIngresado = generarHash(
            contrasena = contrasena,
            salt = salt
        )

        val contrasenaCorrecta =
            MessageDigest.isEqual(
                hashIngresado,
                hashEsperado
            )

        if (!contrasenaCorrecta) {
            return null
        }

        return CuentaEstudiante(
            nombre = preferencias[Claves.NOMBRE]
                .orEmpty(),
            correo = correoGuardado,
            curso = preferencias[Claves.CURSO]
                ?: "3° Básico"
        )
    }

    private suspend fun generarHash(
        contrasena: String,
        salt: ByteArray
    ): ByteArray = withContext(Dispatchers.Default) {

        val especificacion = PBEKeySpec(
            contrasena.toCharArray(),
            salt,
            120_000,
            256
        )

        try {
            val generador = SecretKeyFactory.getInstance(
                "PBKDF2WithHmacSHA256"
            )

            generador.generateSecret(
                especificacion
            ).encoded

        } finally {
            especificacion.clearPassword()
        }
    }
}