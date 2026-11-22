package com.example.appmpazpro3_grupo6.data.remote

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CookiesSesionMPAZ : CookieJar {

    private val cookiesGuardadas = mutableListOf<Cookie>()

    override fun saveFromResponse(
        url: HttpUrl,
        cookies: List<Cookie>
    ) {
        synchronized(this) {
            val ahora = System.currentTimeMillis()

            cookiesGuardadas.removeAll { guardada ->
                guardada.expiresAt <= ahora ||
                        cookies.any { nueva ->
                            nueva.name == guardada.name &&
                                    nueva.domain == guardada.domain &&
                                    nueva.path == guardada.path
                        }
            }

            cookiesGuardadas.addAll(
                cookies.filter { it.expiresAt > ahora }
            )
        }
    }

    override fun loadForRequest(
        url: HttpUrl
    ): List<Cookie> {
        return synchronized(this) {
            val ahora = System.currentTimeMillis()

            cookiesGuardadas.removeAll {
                it.expiresAt <= ahora
            }

            cookiesGuardadas.filter {
                it.matches(url)
            }
        }
    }

    fun limpiar() {
        synchronized(this) {
            cookiesGuardadas.clear()
        }
    }
}

class ClienteEstudiantesMPAZ private constructor(
    baseUrl: String
) {

    private val cookies = CookiesSesionMPAZ()

    private val clienteHttp = OkHttpClient.Builder()
        .cookieJar(cookies)
        .build()

    val api: EstudiantesApiMPAZ = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(clienteHttp)
        .addConverterFactory(
            GsonConverterFactory.create()
        )
        .build()
        .create(EstudiantesApiMPAZ::class.java)

    fun limpiarSesion() {
        cookies.limpiar()
    }

    companion object {

        fun crear(
            baseUrl: String
        ): ClienteEstudiantesMPAZ {
            return ClienteEstudiantesMPAZ(baseUrl)
        }
    }
}

object SesionEstudiantesMPAZ {

    private const val BASE_URL =
        "http://10.0.2.2:8080/"

    val cliente: ClienteEstudiantesMPAZ by lazy {
        ClienteEstudiantesMPAZ.crear(BASE_URL)
    }
}