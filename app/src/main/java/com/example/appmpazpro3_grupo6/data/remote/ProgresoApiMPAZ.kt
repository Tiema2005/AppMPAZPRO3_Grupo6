package com.example.appmpazpro3_grupo6.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

data class GuardarProgresoMPAZ(
    val asignatura: String,
    val ejerciciosRespondidos: Int,
    val respuestasCorrectas: Int
)

data class ProgresoRemotoMPAZ(
    val id: Long,
    val asignatura: String,
    val ejerciciosRespondidos: Int,
    val respuestasCorrectas: Int,
    val porcentajeAcierto: Double
)

data class ProgresoDetalleMPAZ(
    val datosJson: String
)

interface ProgresoApiMPAZ {

    @GET("api/progreso")
    suspend fun listarProgreso():
            Response<List<ProgresoRemotoMPAZ>>

    @GET("api/progreso/asignatura/{asignatura}")
    suspend fun obtenerProgreso(
        @Path("asignatura") asignatura: String
    ): Response<ProgresoRemotoMPAZ>

    @POST("api/progreso")
    suspend fun guardarProgreso(
        @Body datos: GuardarProgresoMPAZ
    ): Response<ProgresoRemotoMPAZ>

    @GET("api/progreso/detalle")
    suspend fun obtenerProgresoDetallado():
            Response<ProgresoDetalleMPAZ>

    @PUT("api/progreso/detalle")
    suspend fun guardarProgresoDetallado(
        @Body datos: ProgresoDetalleMPAZ
    ): Response<ProgresoDetalleMPAZ>
}