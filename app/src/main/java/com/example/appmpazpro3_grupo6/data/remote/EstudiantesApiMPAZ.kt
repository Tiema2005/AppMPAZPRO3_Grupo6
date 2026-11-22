package com.example.appmpazpro3_grupo6.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

data class RegistroEstudianteMPAZ(
    val nombre: String,
    val correo: String,
    val contrasena: String
)

data class LoginEstudianteMPAZ(
    val correo: String,
    val contrasena: String
)

data class EstudianteRespuestaMPAZ(
    val id: Long,
    val nombre: String,
    val correo: String,
    val curso: String
)

interface EstudiantesApiMPAZ {

    @POST("api/estudiantes/registro")
    suspend fun registrar(
        @Body datos: RegistroEstudianteMPAZ
    ): Response<EstudianteRespuestaMPAZ>

    @POST("api/estudiantes/login")
    suspend fun iniciarSesion(
        @Body datos: LoginEstudianteMPAZ
    ): Response<EstudianteRespuestaMPAZ>

    @GET("api/estudiantes/perfil")
    suspend fun obtenerPerfil(): Response<EstudianteRespuestaMPAZ>

    @POST("api/estudiantes/logout")
    suspend fun cerrarSesion(): Response<Unit>
}
