package com.example.appmpazpro3_grupo6.repository

import com.example.appmpazpro3_grupo6.data.remote.WikipediaApi
import com.example.appmpazpro3_grupo6.data.remote.WikipediaPagina
import com.example.appmpazpro3_grupo6.data.remote.WikipediaRespuesta
import com.example.appmpazpro3_grupo6.viewmodel.EstadoLecturaCiencias
import com.example.appmpazpro3_grupo6.viewmodel.LecturasCienciasViewModel

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LecturasCienciasReutilizacionTest {

    private val articulos = linkedMapOf(
        "Lenguaje" to Pair(
            "Sustantivo",
            "Sustantivo"
        ),
        "Matematica" to Pair(
            "Adición_(matemática)",
            "Adición (matemática)"
        ),
        "Ciencias Naturales" to Pair(
            "Fotosíntesis",
            "Fotosíntesis"
        ),
        "Historia y Geografia" to Pair(
            "Mapa",
            "Mapa"
        )
    )

    @Before
    fun preparar() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun limpiar() {
        Dispatchers.resetMain()
    }

    @Test
    fun repositorioFuncionaEnCuatroAsignaturas() = runTest {

        val api = mockk<WikipediaApi>()

        for ((asignatura, datos) in articulos) {

            val articulo = datos.first
            val titulo = datos.second
            val consulta = articulo.replace('_', ' ')

            coEvery {
                api.buscarPaginas(consulta, 10)
            } returns WikipediaRespuesta(
                listOf(
                    WikipediaPagina(
                        articulo,
                        titulo,
                        "Lectura para $asignatura"
                    )
                )
            )
        }

        val repositorio = LecturasCienciasRepository(api)
        val enlaces = mutableSetOf<String>()

        for ((asignatura, datos) in articulos) {

            val articulo = datos.first
            val titulo = datos.second

            val lectura = repositorio.obtenerLectura(articulo)

            assertNotNull(lectura)

            assertEquals(
                titulo,
                lectura?.titulo
            )

            assertEquals(
                "Lectura para $asignatura",
                lectura?.descripcion
            )

            assertTrue(
                lectura?.urlFuente?.startsWith(
                    "https://es.wikipedia.org/wiki/"
                ) == true
            )

            enlaces.add(requireNotNull(lectura).urlFuente)

            coVerify(exactly = 1) {
                api.buscarPaginas(
                    articulo.replace('_', ' '),
                    10
                )
            }

            println(
                "LT-69: REPOSITORIO OK - $asignatura"
            )
        }

        assertEquals(
            "Cada asignatura debe tener un enlace distinto",
            4,
            enlaces.size
        )

        println("LT-69: REPOSITORIO REUTILIZABLE OK")
    }

    @Test
    fun viewModelFuncionaEnCuatroAsignaturas() = runTest {

        val repositorio = mockk<LecturasCienciasRepository>()

        val lecturas = articulos.map { (asignatura, datos) ->

            val articulo = datos.first
            val titulo = datos.second

            val lectura = LecturaCiencias(
                titulo = titulo,
                descripcion = "Contenido de $asignatura",
                urlFuente = "https://es.wikipedia.org/wiki/" +
                        java.net.URLEncoder.encode(
                            articulo, "UTF-8"
                        ).replace("+", "%20")
            )

            coEvery {
                repositorio.obtenerLectura(articulo)
            } returns lectura

            articulo to lectura
        }.toMap()
        val modelos = articulos.map { (asignatura, datos) ->

            val articulo = datos.first

            val viewModel = LecturasCienciasViewModel(
                articulo = articulo,
                repositorio = repositorio
            )

            assertEquals(
                EstadoLecturaCiencias.Cargando,
                viewModel.estado.value
            )

            asignatura to Pair(articulo, viewModel)
        }

        advanceUntilIdle()

        for ((asignatura, datos) in modelos) {

            val articulo = datos.first
            val viewModel = datos.second

            val lecturaEsperada = lecturas.getValue(articulo)

            assertEquals(
                EstadoLecturaCiencias.Disponible(
                    lecturaEsperada
                ),
                viewModel.estado.value
            )

            coVerify(exactly = 1) {
                repositorio.obtenerLectura(articulo)
            }

            println(
                "LT-69: VIEWMODEL OK - $asignatura"
            )
        }

        assertEquals(4, modelos.size)

        println("LT-69: VIEWMODEL REUTILIZABLE OK")
    }
}