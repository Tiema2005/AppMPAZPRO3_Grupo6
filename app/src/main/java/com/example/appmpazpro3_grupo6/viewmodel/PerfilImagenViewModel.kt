package com.example.appmpazpro3_grupo6.viewmodel

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.util.UUID

class PerfilImagenViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val contexto = application.applicationContext

    private val carpeta = File(
        contexto.filesDir,
        "perfil"
    ).apply {
        mkdirs()
    }

    private val preferencias = contexto.getSharedPreferences(
        "imagen_perfil",
        Context.MODE_PRIVATE
    )

    private fun recuperarImagen(): Uri? {
        val nombre = preferencias.getString(
            "archivo_imagen",
            null
        ) ?: "foto_perfil.jpg"

        val archivo = File(carpeta, nombre)

        return if (
            archivo.exists() &&
            archivo.length() > 0L
        ) {
            Uri.fromFile(archivo)
        } else {
            null
        }
    }

    private val _imagenUri = MutableStateFlow<Uri?>(
        recuperarImagen()
    )

    val imagenUri: StateFlow<Uri?> =
        _imagenUri.asStateFlow()

    // Crear archivo temporal para la cámara
    fun crearUriCamara(): Uri {
        val archivo = File(
            carpeta,
            "captura_temporal.jpg"
        )

        if (archivo.exists()) {
            archivo.delete()
        }

        return FileProvider.getUriForFile(
            contexto,
            "${contexto.packageName}.fileprovider",
            archivo
        )
    }

    fun actualizarDesdeGaleria(uri: Uri) {
        guardarImagen(
            uri = uri,
            eliminarTemporal = false
        )
    }

    fun actualizarDesdeCamara(uri: Uri) {
        guardarImagen(
            uri = uri,
            eliminarTemporal = true
        )
    }

    fun cancelarCamara() {
        File(
            carpeta,
            "captura_temporal.jpg"
        ).delete()
    }

    private fun guardarImagen(
        uri: Uri,
        eliminarTemporal: Boolean
    ) {
        viewModelScope.launch(Dispatchers.IO) {

            val archivoNuevo = File(
                carpeta,
                "foto_${UUID.randomUUID()}.jpg"
            )

            try {
                val entrada = contexto.contentResolver
                    .openInputStream(uri)
                    ?: error("No se pudo leer la fotografía")

                entrada.use { origen ->
                    archivoNuevo.outputStream().use { destino ->
                        origen.copyTo(destino)
                    }
                }

                check(archivoNuevo.length() > 0L) {
                    "La fotografía está vacía"
                }

                val imagenAnterior = _imagenUri.value

                preferencias.edit()
                    .putString(
                        "archivo_imagen",
                        archivoNuevo.name
                    )
                    .apply()

                _imagenUri.value = Uri.fromFile(archivoNuevo)

                // Eliminar fotografía anterior
                imagenAnterior?.path?.let { ruta ->
                    val anterior = File(ruta)

                    if (
                        anterior.parentFile == carpeta &&
                        anterior != archivoNuevo
                    ) {
                        anterior.delete()
                    }
                }

            } catch (e: Exception) {
                archivoNuevo.delete()

            } finally {
                if (eliminarTemporal) {
                    cancelarCamara()
                }
            }
        }
    }
}