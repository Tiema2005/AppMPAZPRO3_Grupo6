package com.example.appmpazpro3_grupo6.data.remote

import retrofit2.Response
import retrofit2.http.GET

data class EstadoBackendRespuesta(
    val mensaje: String,
    val aplicacion: String,
    val estado: String
)

interface BackendApi {

    @GET("api/estado")
    suspend fun consultarEstado(): Response<EstadoBackendRespuesta>
}