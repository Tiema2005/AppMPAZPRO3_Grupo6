package com.example.appmpazpro3_grupo6.repository

import com.example.appmpazpro3_grupo6.data.remote.WikipediaApi
import java.net.URLEncoder
import kotlinx.coroutines.CancellationException

data class LecturaCiencias(
    val titulo: String,
    val descripcion: String?,
    val urlFuente: String
)

class LecturasCienciasRepository(
    private val api: WikipediaApi = WikipediaApi.servicio
) {
    suspend fun obtenerLectura(articulo: String): LecturaCiencias? {
        val pagina = try {
            val respuesta = api.buscarPaginas(
                consulta = articulo.replace('_', ' '),
                limite = 10
            )
            respuesta.pages.orEmpty().firstOrNull {
                it.key.equals(articulo, ignoreCase = true)
            }
        } catch (error: CancellationException) {
            throw error
        } catch (error: Exception) {
            null
        }
        val paginaBuscada = pagina?.key ?: articulo
        val ruta = URLEncoder.encode(paginaBuscada, "UTF-8").replace("+", "%20")
        return LecturaCiencias(
            titulo = pagina?.title ?: articulo.replace('_', ' '),
            descripcion = pagina?.description,
            urlFuente = "https://es.wikipedia.org/wiki/$ruta"
        )
    }
}