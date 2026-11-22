package com.example.appmpazpro3_grupo6.repository

import com.example.appmpazpro3_grupo6.data.remote.ActualizarMetaMPAZ
import com.example.appmpazpro3_grupo6.data.remote.ClienteEstudiantesMPAZ
import com.example.appmpazpro3_grupo6.data.remote.CrearMetaMPAZ
import com.example.appmpazpro3_grupo6.data.remote.MetaEstudioMPAZ
import com.example.appmpazpro3_grupo6.data.remote.SesionEstudiantesMPAZ

class ErrorMetasMPAZ(
    message: String
) : Exception(message)

class MetasRepositoryMPAZ(
    private val cliente: ClienteEstudiantesMPAZ =
        SesionEstudiantesMPAZ.cliente
) {

    suspend fun listar(): List<MetaEstudioMPAZ> {

        val respuesta = cliente.metasApi.listarMetas()

        if (!respuesta.isSuccessful) {
            throw ErrorMetasMPAZ(mensajeError(respuesta.code()))
        }

        return respuesta.body()
            ?: throw ErrorMetasMPAZ(
                "El servidor no devolvió las metas."
            )
    }

    suspend fun crear(
        titulo: String,
        descripcion: String
    ): MetaEstudioMPAZ {

        validar(titulo, descripcion)

        val respuesta = cliente.metasApi.crearMeta(
            CrearMetaMPAZ(
                titulo = titulo.trim(),
                descripcion = descripcion.trim()
                    .takeIf { it.isNotEmpty() }
            )
        )

        if (!respuesta.isSuccessful) {
            throw ErrorMetasMPAZ(mensajeError(respuesta.code()))
        }

        return respuesta.body()
            ?: throw ErrorMetasMPAZ(
                "No se recibieron los datos de la meta creada."
            )
    }

    suspend fun actualizar(
        id: Long,
        titulo: String,
        descripcion: String,
        completada: Boolean
    ): MetaEstudioMPAZ {

        validar(titulo, descripcion)

        val respuesta = cliente.metasApi.actualizarMeta(
            id = id,
            datos = ActualizarMetaMPAZ(
                titulo = titulo.trim(),
                descripcion = descripcion.trim()
                    .takeIf { it.isNotEmpty() },
                completada = completada
            )
        )

        if (!respuesta.isSuccessful) {
            throw ErrorMetasMPAZ(mensajeError(respuesta.code()))
        }

        return respuesta.body()
            ?: throw ErrorMetasMPAZ(
                "No se recibieron los datos de la meta actualizada."
            )
    }

    suspend fun eliminar(id: Long) {

        val respuesta = cliente.metasApi.eliminarMeta(id)

        if (!respuesta.isSuccessful) {
            throw ErrorMetasMPAZ(mensajeError(respuesta.code()))
        }
    }

    private fun validar(
        titulo: String,
        descripcion: String
    ) {
        if (titulo.isBlank()) {
            throw ErrorMetasMPAZ(
                "Debes escribir el título de la meta."
            )
        }

        if (titulo.trim().length > 120) {
            throw ErrorMetasMPAZ(
                "El título no puede superar 120 caracteres."
            )
        }

        if (descripcion.trim().length > 500) {
            throw ErrorMetasMPAZ(
                "La descripción no puede superar 500 caracteres."
            )
        }
    }

    private fun mensajeError(codigo: Int): String {
        return when (codigo) {
            400 -> "Revisa los datos de la meta."
            401 -> "Tu sesión ha expirado. Inicia sesión nuevamente."
            403 -> "No tienes permiso para modificar esta meta."
            404 -> "La meta no existe o no pertenece a tu cuenta."
            else -> "No se pudo completar la operación. Código $codigo."
        }
    }
}