package com.example.appmpazpro3_grupo6.viewmodel

import com.example.appmpazpro3_grupo6.data.remote.MetaEstudioMPAZ
import com.example.appmpazpro3_grupo6.repository.MetasRepositoryMPAZ

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class MetasViewModelTest {

    private lateinit var repositorio: MetasRepositoryMPAZ

    @Before
    fun preparar() {
        Dispatchers.setMain(StandardTestDispatcher())
        repositorio = mockk()
    }

    @After
    fun limpiarDispatcher() {
        Dispatchers.resetMain()
    }

    private fun crearMeta(idMeta: Long): MetaEstudioMPAZ {
        val meta = mockk<MetaEstudioMPAZ>()
        every { meta.id } returns idMeta
        return meta
    }

    @Test
    fun estadoInicialEstaVacio() = runTest {

        val viewModel = MetasViewModel(repositorio)

        assertTrue(viewModel.metas.value.isEmpty())
        assertFalse(viewModel.cargando.value)
        assertFalse(viewModel.procesando.value)
        assertNull(viewModel.mensajeError.value)
    }

    @Test
    fun cargarMetasActualizaLista() = runTest {

        val meta1 = crearMeta(1L)
        val meta2 = crearMeta(2L)

        coEvery {
            repositorio.listar()
        } returns listOf(meta1, meta2)

        val viewModel = MetasViewModel(repositorio)

        viewModel.cargarMetas()

        advanceUntilIdle()

        assertEquals(2, viewModel.metas.value.size)
        assertSame(meta1, viewModel.metas.value[0])
        assertSame(meta2, viewModel.metas.value[1])
        assertFalse(viewModel.cargando.value)

        coVerify(exactly = 1) {
            repositorio.listar()
        }
    }

    @Test
    fun cargarMetasSinResultados() = runTest {

        coEvery {
            repositorio.listar()
        } returns emptyList()

        val viewModel = MetasViewModel(repositorio)

        viewModel.cargarMetas()

        advanceUntilIdle()

        assertTrue(viewModel.metas.value.isEmpty())
        assertFalse(viewModel.cargando.value)
        assertNull(viewModel.mensajeError.value)
    }

    @Test
    fun errorDeConexionMuestraMensaje() = runTest {

        coEvery {
            repositorio.listar()
        } throws IOException("Conexion simulada")

        val viewModel = MetasViewModel(repositorio)

        viewModel.cargarMetas()

        advanceUntilIdle()

        assertEquals(
            "No se pudo conectar con Spring Boot.",
            viewModel.mensajeError.value
        )

        assertFalse(viewModel.cargando.value)
    }

    @Test
    fun crearMetaAgregaElementoALaLista() = runTest {

        val nuevaMeta = crearMeta(10L)

        coEvery {
            repositorio.crear(
                titulo = "Estudiar matematicas",
                descripcion = "Practicar ejercicios"
            )
        } returns nuevaMeta

        val viewModel = MetasViewModel(repositorio)

        var exito = false

        viewModel.guardarMeta(
            id = null,
            titulo = "Estudiar matematicas",
            descripcion = "Practicar ejercicios",
            completada = false,
            onExito = { exito = true }
        )

        advanceUntilIdle()

        assertEquals(1, viewModel.metas.value.size)
        assertSame(nuevaMeta, viewModel.metas.value.first())
        assertTrue(exito)
        assertFalse(viewModel.procesando.value)
        assertNull(viewModel.mensajeError.value)

        coVerify(exactly = 1) {
            repositorio.crear(
                "Estudiar matematicas",
                "Practicar ejercicios"
            )
        }
    }

    @Test
    fun actualizarMetaReemplazaLaAnterior() = runTest {

        val original = crearMeta(5L)
        val otraMeta = crearMeta(6L)
        val actualizada = crearMeta(5L)

        coEvery {
            repositorio.listar()
        } returns listOf(original, otraMeta)

        coEvery {
            repositorio.actualizar(
                id = 5L,
                titulo = "Nueva meta",
                descripcion = "Nueva descripcion",
                completada = true
            )
        } returns actualizada

        val viewModel = MetasViewModel(repositorio)

        viewModel.cargarMetas()
        advanceUntilIdle()

        viewModel.guardarMeta(
            id = 5L,
            titulo = "Nueva meta",
            descripcion = "Nueva descripcion",
            completada = true
        )

        advanceUntilIdle()

        assertEquals(2, viewModel.metas.value.size)
        assertSame(actualizada, viewModel.metas.value[0])
        assertSame(otraMeta, viewModel.metas.value[1])
        assertFalse(viewModel.procesando.value)
    }

    @Test
    fun eliminarMetaQuitaElementoDeLaLista() = runTest {

        val meta1 = crearMeta(1L)
        val meta2 = crearMeta(2L)

        coEvery {
            repositorio.listar()
        } returns listOf(meta1, meta2)

        coEvery {
            repositorio.eliminar(1L)
        } returns Unit

        val viewModel = MetasViewModel(repositorio)

        viewModel.cargarMetas()
        advanceUntilIdle()

        var exito = false

        viewModel.eliminarMeta(
            id = 1L,
            onExito = { exito = true }
        )

        advanceUntilIdle()

        assertEquals(1, viewModel.metas.value.size)
        assertSame(meta2, viewModel.metas.value.first())
        assertTrue(exito)
        assertFalse(viewModel.procesando.value)

        coVerify(exactly = 1) {
            repositorio.eliminar(1L)
        }
    }

    @Test
    fun errorAlGuardarMetaMuestraMensaje() = runTest {

        coEvery {
            repositorio.crear(any(), any())
        } throws IllegalStateException("Error simulado")

        val viewModel = MetasViewModel(repositorio)

        var exito = false

        viewModel.guardarMeta(
            id = null,
            titulo = "Meta",
            descripcion = "Descripcion",
            completada = false,
            onExito = { exito = true }
        )

        advanceUntilIdle()

        assertEquals(
            "Ocurrió un error al gestionar las metas.",
            viewModel.mensajeError.value
        )

        assertFalse(exito)
        assertTrue(viewModel.metas.value.isEmpty())
        assertFalse(viewModel.procesando.value)
    }

    @Test
    fun evitaCargasSimultaneas() = runTest {

        val pendiente =
            CompletableDeferred<List<MetaEstudioMPAZ>>()

        coEvery {
            repositorio.listar()
        } coAnswers {
            pendiente.await()
        }

        val viewModel = MetasViewModel(repositorio)

        viewModel.cargarMetas()

        runCurrent()

        assertTrue(viewModel.cargando.value)

        viewModel.cargarMetas()

        pendiente.complete(emptyList())

        advanceUntilIdle()

        coVerify(exactly = 1) {
            repositorio.listar()
        }

        assertFalse(viewModel.cargando.value)
    }

    @Test
    fun limpiarDescartaCargaDeSesionAnterior() = runTest {

        val pendiente =
            CompletableDeferred<List<MetaEstudioMPAZ>>()

        coEvery {
            repositorio.listar()
        } coAnswers {
            pendiente.await()
        }

        val viewModel = MetasViewModel(repositorio)

        viewModel.cargarMetas()

        runCurrent()

        assertTrue(viewModel.cargando.value)

        viewModel.limpiar()

        pendiente.complete(listOf(crearMeta(3L)))

        advanceUntilIdle()

        assertTrue(viewModel.metas.value.isEmpty())
        assertFalse(viewModel.cargando.value)
        assertFalse(viewModel.procesando.value)
        assertNull(viewModel.mensajeError.value)
    }

    @Test
    fun limpiarErrorEliminaMensaje() = runTest {

        coEvery {
            repositorio.listar()
        } throws IOException("Error simulado")

        val viewModel = MetasViewModel(repositorio)

        viewModel.cargarMetas()

        advanceUntilIdle()

        assertEquals(
            "No se pudo conectar con Spring Boot.",
            viewModel.mensajeError.value
        )

        viewModel.limpiarError()

        assertNull(viewModel.mensajeError.value)
    }
}