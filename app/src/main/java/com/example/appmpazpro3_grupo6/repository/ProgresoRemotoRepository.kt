package com.example.appmpazpro3_grupo6.repository

import com.example.appmpazpro3_grupo6.data.remote.ClienteEstudiantesMPAZ
import com.example.appmpazpro3_grupo6.data.remote.GuardarProgresoMPAZ
import com.example.appmpazpro3_grupo6.data.remote.ProgresoDetalleMPAZ
import com.example.appmpazpro3_grupo6.data.remote.ProgresoRemotoMPAZ
import com.example.appmpazpro3_grupo6.data.remote.SesionEstudiantesMPAZ

class ErrorProgresoRemoto(
    message: String
) : Exception(message)

class ProgresoRemotoRepository(
    private val cliente: ClienteEstudiantesMPAZ =
        SesionEstudiantesMPAZ.cliente
) {

    private val clavesValidas = listOf(
        "lenguaje",
        "matematica",
        "ciencias",
        "historia"
    ).flatMap { asignatura ->
        listOf(
            "facil",
            "medio",
            "dificil"
        ).map { dificultad ->
            "${asignatura}_${dificultad}"
        }
    }.toSet()

    suspend fun listar(): List<ProgresoRemotoMPAZ> {

        val respuesta = cliente.progresoApi.listarProgreso()

        if (!respuesta.isSuccessful) {
            throw ErrorProgresoRemoto(
                mensajeError(respuesta.code())
            )
        }

        return respuesta.body()
            ?: throw ErrorProgresoRemoto(
                "El servidor no devolvió los datos de progreso."
            )
    }

    suspend fun obtener(
        asignaturaNivel: String
    ): ProgresoRemotoMPAZ? {

        validarAsignatura(asignaturaNivel)

        val respuesta = cliente.progresoApi.obtenerProgreso(
            asignaturaNivel
        )

        if (respuesta.code() == 404) {
            return null
        }

        if (!respuesta.isSuccessful) {
            throw ErrorProgresoRemoto(
                mensajeError(respuesta.code())
            )
        }

        return respuesta.body()
            ?: throw ErrorProgresoRemoto(
                "El servidor no devolvió el progreso solicitado."
            )
    }

    suspend fun guardar(
        asignaturaNivel: String,
        ejerciciosRespondidos: Int,
        respuestasCorrectas: Int
    ): ProgresoRemotoMPAZ {

        validarAsignatura(asignaturaNivel)

        if (
            ejerciciosRespondidos !in 0..5 ||
            respuestasCorrectas !in 0..ejerciciosRespondidos
        ) {
            throw ErrorProgresoRemoto(
                "Los resultados deben estar entre 0 y 5 y " +
                        "los aciertos no pueden superar las respuestas."
            )
        }

        val respuesta = cliente.progresoApi.guardarProgreso(
            GuardarProgresoMPAZ(
                asignatura = asignaturaNivel,
                ejerciciosRespondidos = ejerciciosRespondidos,
                respuestasCorrectas = respuestasCorrectas
            )
        )

        if (!respuesta.isSuccessful) {
            throw ErrorProgresoRemoto(
                mensajeError(respuesta.code())
            )
        }

        return respuesta.body()
            ?: throw ErrorProgresoRemoto(
                "El servidor no devolvió el progreso guardado."
            )
    }

    suspend fun obtenerDetalle(): String? {

        val respuesta =
            cliente.progresoApi.obtenerProgresoDetallado()

        if (respuesta.code() == 404) {
            return null
        }

        if (!respuesta.isSuccessful) {
            throw ErrorProgresoRemoto(
                mensajeError(respuesta.code())
            )
        }

        return respuesta.body()?.datosJson
            ?: throw ErrorProgresoRemoto(
                "El servidor no devolvió el progreso detallado."
            )
    }

    suspend fun guardarDetalle(
        datosJson: String
    ): String {

        if (
            datosJson.isBlank() ||
            datosJson.length > 250000
        ) {
            throw ErrorProgresoRemoto(
                "Los datos del progreso están vacíos o " +
                        "superan el tamaño permitido."
            )
        }

        val respuesta =
            cliente.progresoApi.guardarProgresoDetallado(
                ProgresoDetalleMPAZ(
                    datosJson = datosJson
                )
            )

        if (!respuesta.isSuccessful) {
            throw ErrorProgresoRemoto(
                mensajeError(respuesta.code())
            )
        }

        return respuesta.body()?.datosJson
            ?: throw ErrorProgresoRemoto(
                "El servidor no confirmó el guardado."
            )
    }

    private fun validarAsignatura(
        asignaturaNivel: String
    ) {

        if (asignaturaNivel !in clavesValidas) {
            throw ErrorProgresoRemoto(
                "Asignatura o dificultad no válida."
            )
        }
    }

    private fun mensajeError(codigo: Int): String {

        return when (codigo) {
            400 -> "El servidor rechazó los datos del progreso."
            401 -> "Tu sesión ha expirado. Inicia sesión nuevamente."
            403 -> "No tienes permiso para consultar este progreso."
            404 -> "No se encontró el progreso solicitado."
            else -> "No se pudo gestionar el progreso. Código $codigo."
        }
    }
}