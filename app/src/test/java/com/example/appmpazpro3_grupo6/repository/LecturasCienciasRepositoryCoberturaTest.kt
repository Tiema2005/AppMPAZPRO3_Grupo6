package com.example.appmpazpro3_grupo6.repository

import com.example.appmpazpro3_grupo6.data.remote.WikipediaApi
import com.example.appmpazpro3_grupo6.data.remote.WikipediaPagina
import com.example.appmpazpro3_grupo6.data.remote.WikipediaRespuesta
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException

class LecturasCienciasRepositoryCoberturaTest {

    @Test
    fun buscaPaginaExactaIgnorandoMayusculas() = runBlocking {
        val api = mockk<WikipediaApi>()
        coEvery { api.buscarPaginas("Sistema solar", 10) } returns
                WikipediaRespuesta(
                    listOf(
                        WikipediaPagina("Luna", "La Luna", "Satélite natural"),
                        WikipediaPagina("sistema_solar", "Sistema solar", "Planetas")
                    )
                )

        val resultado = LecturasCienciasRepository(api)
            .obtenerLectura("Sistema_solar")

        assertEquals("Sistema solar", resultado?.titulo)
        assertEquals("Planetas", resultado?.descripcion)
        assertEquals(
            "https://es.wikipedia.org/wiki/sistema_solar",
            resultado?.urlFuente
        )
        coVerify(exactly = 1) { api.buscarPaginas("Sistema solar", 10) }
    }

    @Test
    fun sinCoincidenciasDevuelveLecturaDeRespaldo() = runBlocking {
        val api = mockk<WikipediaApi>()
        coEvery { api.buscarPaginas(any(), any()) } returns
                WikipediaRespuesta(emptyList())

        val resultado = LecturasCienciasRepository(api)
            .obtenerLectura("Materia_y_energia")

        assertEquals("Materia y energia", resultado?.titulo)
        assertNull(resultado?.descripcion)
        assertEquals(
            "https://es.wikipedia.org/wiki/Materia_y_energia",
            resultado?.urlFuente
        )
    }

    @Test
    fun respuestaSinPaginasUsaRespaldo() = runBlocking {
        val api = mockk<WikipediaApi>()
        coEvery { api.buscarPaginas(any(), any()) } returns
                WikipediaRespuesta(null)

        val resultado = LecturasCienciasRepository(api)
            .obtenerLectura("Ciencias_naturales")

        assertEquals("Ciencias naturales", resultado?.titulo)
        assertNull(resultado?.descripcion)
    }

    @Test
    fun errorDeRedConservaLecturaDeRespaldo() = runBlocking {
        val api = mockk<WikipediaApi>()
        coEvery { api.buscarPaginas(any(), any()) } throws IOException("Sin conexión")

        val resultado = LecturasCienciasRepository(api)
            .obtenerLectura("La_Tierra")

        assertEquals("La Tierra", resultado?.titulo)
        assertEquals("https://es.wikipedia.org/wiki/La_Tierra", resultado?.urlFuente)
    }

    @Test
    fun cancelacionDeCorrutinaSePropaga() = runBlocking {
        val api = mockk<WikipediaApi>()
        coEvery { api.buscarPaginas(any(), any()) } throws
                CancellationException("Cancelado")

        val error = runCatching {
            LecturasCienciasRepository(api).obtenerLectura("Planeta")
        }.exceptionOrNull()

        assertTrue(error is CancellationException)
    }
}