package com.example.appmpazpro3_grupo6.data.remote

import com.example.appmpazpro3_grupo6.repository.LecturasCienciasRepository
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Assume.assumeTrue
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WikipediaActividadesTest {

    private fun crearApiSimulada(
        servidor: MockWebServer
    ): WikipediaApi {
        return Retrofit.Builder()
            .baseUrl(servidor.url("/w/rest.php/v1/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WikipediaApi::class.java)
    }

    @Test
    fun consultaWikipediaYRecuperaLectura() = runBlocking {
        val servidor = MockWebServer()

        try {
            servidor.start()

            servidor.enqueue(
                MockResponse()
                    .setResponseCode(200)
                    .setHeader("Content-Type", "application/json")
                    .setBody(
                        """
                        {
                          "pages": [
                            {
                              "key": "Fotosíntesis",
                              "title": "Fotosíntesis",
                              "description": "Proceso biológico de las plantas"
                            }
                          ]
                        }
                        """.trimIndent()
                    )
            )

            val repositorio = LecturasCienciasRepository(
                crearApiSimulada(servidor)
            )

            val lectura = repositorio.obtenerLectura(
                "Fotosíntesis"
            )

            assertNotNull(lectura)

            assertEquals(
                "Fotosíntesis",
                lectura?.titulo
            )

            assertEquals(
                "Proceso biológico de las plantas",
                lectura?.descripcion
            )

            assertEquals(
                "https://es.wikipedia.org/wiki/Fotos%C3%ADntesis",
                lectura?.urlFuente
            )

            val peticion = servidor.takeRequest()

            assertEquals(
                "/w/rest.php/v1/search/page",
                peticion.requestUrl?.encodedPath
            )

            assertEquals(
                "Fotosíntesis",
                peticion.requestUrl?.queryParameter("q")
            )

            assertEquals(
                "10",
                peticion.requestUrl?.queryParameter("limit")
            )

            assertTrue(
                peticion.getHeader("User-Agent")
                    .orEmpty()
                    .contains("MPAZPro3")
            )

            println("LT-70: CONSULTA Y RESPUESTA OK")

        } finally {
            servidor.shutdown()
        }
    }

    @Test
    fun sinResultadosEntregaEnlaceAlternativo() = runBlocking {
        val servidor = MockWebServer()

        try {
            servidor.start()

            servidor.enqueue(
                MockResponse()
                    .setResponseCode(200)
                    .setHeader("Content-Type", "application/json")
                    .setBody("""{"pages": []}""")
            )

            val repositorio = LecturasCienciasRepository(
                crearApiSimulada(servidor)
            )

            val lectura = repositorio.obtenerLectura(
                "Puntos_cardinales"
            )

            assertNotNull(lectura)

            assertEquals(
                "Puntos cardinales",
                lectura?.titulo
            )

            assertNull(lectura?.descripcion)

            assertEquals(
                "https://es.wikipedia.org/wiki/Puntos_cardinales",
                lectura?.urlFuente
            )

            println("LT-70: RESPUESTA ALTERNATIVA OK")

        } finally {
            servidor.shutdown()
        }
    }

    @Test
    fun errorDelServidorNoBloqueaLaLectura() = runBlocking {
        val servidor = MockWebServer()

        try {
            servidor.start()

            servidor.enqueue(
                MockResponse()
                    .setResponseCode(503)
                    .setBody("Servicio no disponible")
            )

            val repositorio = LecturasCienciasRepository(
                crearApiSimulada(servidor)
            )

            val lectura = repositorio.obtenerLectura(
                "Historia_de_Chile"
            )

            assertNotNull(lectura)

            assertEquals(
                "Historia de Chile",
                lectura?.titulo
            )

            assertNull(lectura?.descripcion)

            assertEquals(
                "https://es.wikipedia.org/wiki/Historia_de_Chile",
                lectura?.urlFuente
            )

            println("LT-70: ERROR CONTROLADO OK")

        } finally {
            servidor.shutdown()
        }
    }

    @Test
    fun verificarWikipediaRealEnCuatroAsignaturas() = runBlocking {

        assumeTrue(
            "Prueba real habilitada solo bajo demanda",
            System.getenv("MPAZ_REAL_INTEGRATION") == "true"
        )

        val temas = mapOf(
            "Lenguaje" to "Sustantivo",
            "Matemática" to "Adición",
            "Ciencias Naturales" to "Fotosíntesis",
            "Historia y Geografía" to "Puntos cardinales"
        )

        for ((asignatura, articulo) in temas) {

            val respuesta = withTimeout(30000) {
                WikipediaApi.servicio.buscarPaginas(
                    consulta = articulo,
                    limite = 10
                )
            }

            val paginas = respuesta.pages.orEmpty()

            assertTrue(
                "Wikipedia no devolvió resultados para $asignatura",
                paginas.isNotEmpty()
            )

            assertTrue(
                "Wikipedia devolvió claves vacías para $asignatura",
                paginas.all { it.key.isNotBlank() }
            )

            println(
                "LT-70: $asignatura - " +
                        "$articulo - " +
                        "${paginas.size} resultados OK"
            )
        }

        println("LT-70: API REAL VERIFICADA EN CUATRO ASIGNATURAS")
    }
}