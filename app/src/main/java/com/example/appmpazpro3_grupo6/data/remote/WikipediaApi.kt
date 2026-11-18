package com.example.appmpazpro3_grupo6.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface WikipediaApi {
    @Headers("User-Agent: MPAZPro3/1.0 (https://github.com/Tiema2005/AppMPAZPRO3_Grupo6)")
    @GET("search/page")
    suspend fun buscarPaginas(
        @Query("q") consulta: String,
        @Query("limit") limite: Int
    ): WikipediaRespuesta

    companion object {
        val servicio: WikipediaApi by lazy {
            Retrofit.Builder()
                .baseUrl("https://es.wikipedia.org/w/rest.php/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WikipediaApi::class.java)
        }
    }
}