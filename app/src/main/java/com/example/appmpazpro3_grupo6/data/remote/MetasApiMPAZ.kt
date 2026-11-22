package com.example.appmpazpro3_grupo6.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

data class MetaEstudioMPAZ(
    val id: Long,
    val titulo: String,
    val descripcion: String?,
    val completada: Boolean
)

data class CrearMetaMPAZ(
    val titulo: String,
    val descripcion: String?
)

data class ActualizarMetaMPAZ(
    val titulo: String,
    val descripcion: String?,
    val completada: Boolean
)

interface MetasApiMPAZ {

    @GET("api/metas")
    suspend fun listarMetas(): Response<List<MetaEstudioMPAZ>>

    @GET("api/metas/{id}")
    suspend fun obtenerMeta(
        @Path("id") id: Long
    ): Response<MetaEstudioMPAZ>

    @POST("api/metas")
    suspend fun crearMeta(
        @Body datos: CrearMetaMPAZ
    ): Response<MetaEstudioMPAZ>

    @PUT("api/metas/{id}")
    suspend fun actualizarMeta(
        @Path("id") id: Long,
        @Body datos: ActualizarMetaMPAZ
    ): Response<MetaEstudioMPAZ>

    @DELETE("api/metas/{id}")
    suspend fun eliminarMeta(
        @Path("id") id: Long
    ): Response<Unit>
}