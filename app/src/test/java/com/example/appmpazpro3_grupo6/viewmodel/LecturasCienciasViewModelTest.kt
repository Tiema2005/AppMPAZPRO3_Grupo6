package com.example.appmpazpro3_grupo6.viewmodel

import com.example.appmpazpro3_grupo6.repository.LecturaCiencias
import com.example.appmpazpro3_grupo6.repository.LecturasCienciasRepository

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
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LecturasCienciasViewModelTest {

    @Before
    fun preparar() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun limpiar() {
        Dispatchers.resetMain()
    }

    private fun crearLectura(): LecturaCiencias {
        return LecturaCiencias(
            titulo = "Sistema solar",
            descripcion = "Conjunto de planetas",
            urlFuente = "https://es.wikipedia.org/wiki/Sistema_solar"
        )
    }

    @Test
    fun estadoInicialEsCargando() = runTest {

        val repositorio = mockk<LecturasCienciasRepository>()

        coEvery {
            repositorio.obtenerLectura(any())
        } returns crearLectura()

        val viewModel = LecturasCienciasViewModel(
            articulo = "Sistema_solar",
            repositorio = repositorio
        )

        assertEquals(
            EstadoLecturaCiencias.Cargando,
            viewModel.estado.value
        )

        advanceUntilIdle()
    }

    @Test
    fun lecturaDisponibleSeCargaCorrectamente() = runTest {

        val repositorio = mockk<LecturasCienciasRepository>()
        val lectura = crearLectura()

        coEvery {
            repositorio.obtenerLectura("Sistema_solar")
        } returns lectura

        val viewModel = LecturasCienciasViewModel(
            articulo = "Sistema_solar",
            repositorio = repositorio
        )

        advanceUntilIdle()

        assertEquals(
            EstadoLecturaCiencias.Disponible(lectura),
            viewModel.estado.value
        )

        coVerify(exactly = 1) {
            repositorio.obtenerLectura("Sistema_solar")
        }
    }

    @Test
    fun lecturaNulaMuestraSinResultados() = runTest {

        val repositorio = mockk<LecturasCienciasRepository>()

        coEvery {
            repositorio.obtenerLectura(any())
        } returns null

        val viewModel = LecturasCienciasViewModel(
            articulo = "Articulo_inexistente",
            repositorio = repositorio
        )

        advanceUntilIdle()

        assertEquals(
            EstadoLecturaCiencias.SinResultados,
            viewModel.estado.value
        )
    }

    @Test
    fun errorDelRepositorioMuestraError() = runTest {

        val repositorio = mockk<LecturasCienciasRepository>()

        coEvery {
            repositorio.obtenerLectura(any())
        } throws IllegalStateException("Error simulado")

        val viewModel = LecturasCienciasViewModel(
            articulo = "Sistema_solar",
            repositorio = repositorio
        )

        advanceUntilIdle()

        assertEquals(
            EstadoLecturaCiencias.Error,
            viewModel.estado.value
        )
    }

    @Test
    fun permiteReintentarDespuesDeUnaLecturaNula() = runTest {

        val repositorio = mockk<LecturasCienciasRepository>()

        coEvery {
            repositorio.obtenerLectura(any())
        } returns null

        val viewModel = LecturasCienciasViewModel(
            articulo = "Sistema_solar",
            repositorio = repositorio
        )

        advanceUntilIdle()

        assertEquals(
            EstadoLecturaCiencias.SinResultados,
            viewModel.estado.value
        )

        val lectura = crearLectura()

        coEvery {
            repositorio.obtenerLectura(any())
        } returns lectura

        viewModel.cargarLectura()

        advanceUntilIdle()

        assertEquals(
            EstadoLecturaCiencias.Disponible(lectura),
            viewModel.estado.value
        )

        coVerify(exactly = 2) {
            repositorio.obtenerLectura("Sistema_solar")
        }
    }

    @Test
    fun evitaCargasSimultaneas() = runTest {

        val repositorio = mockk<LecturasCienciasRepository>()

        coEvery {
            repositorio.obtenerLectura(any())
        } returns crearLectura()

        val viewModel = LecturasCienciasViewModel(
            articulo = "Sistema_solar",
            repositorio = repositorio
        )

        viewModel.cargarLectura()

        advanceUntilIdle()

        assertTrue(
            viewModel.estado.value is EstadoLecturaCiencias.Disponible
        )

        coVerify(exactly = 1) {
            repositorio.obtenerLectura("Sistema_solar")
        }
    }
}