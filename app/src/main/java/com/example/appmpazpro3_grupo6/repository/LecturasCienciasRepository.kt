package com.example.appmpazpro3_grupo6.repository

import com.example.appmpazpro3_grupo6.data.remote.WikipediaApi
import java.net.URLEncoder

data class LecturaCiencias(
    val titulo: String,
    val descripcion: String?,
    val urlFuente: String
)

class LecturasCienciasRepository(
    private val api: WikipediaApi = WikipediaApi.servicio
) {
    suspend fun obtenerFotosintesis(): LecturaCiencias? {
        val respuesta = api.buscarPaginas(
            consulta = "fotosíntesis",
            limite = 5
        )

        val paginas = checkNotNull(respuesta.pages) {
            "La respuesta de Wikipedia no contiene el campo pages."
        }

        val pagina = paginas.firstOrNull { it.key == "Fotosíntesis" }
            ?: return null

        val claveCodificada = URLEncoder.encode(pagina.key, "UTF-8")
            .replace("+", "%20")

        return LecturaCiencias(
            titulo = pagina.title,
            descripcion = pagina.description,
            urlFuente = "https://es.wikipedia.org/wiki/$claveCodificada"
        )
    }
}