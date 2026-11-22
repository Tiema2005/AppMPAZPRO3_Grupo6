package com.example.appmpazpro3_grupo6.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ClienteBackendMPAZ {

    fun crear(baseUrl: String): BackendApi {

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
            .create(BackendApi::class.java)
    }
}