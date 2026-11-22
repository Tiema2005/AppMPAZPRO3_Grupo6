package com.example.appmpazpro3_grupo6.viewmodel

import com.example.appmpazpro3_grupo6.data.remote.MetaEstudioMPAZ
import com.example.appmpazpro3_grupo6.repository.ErrorMetasMPAZ
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
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class MetasViewModelCoberturaExtraTest {

    private lateinit var repositorio: MetasRepositoryMPAZ

    @Before
    fun preparar() {
        Dispatchers.setMain(StandardTestDispatcher())
        repositorio = mockk()
    }

    @After
    fun limpiar() {
        Dispatchers.resetMain()
    }

    private fun crearMeta(id: Long): MetaEstudioMPAZ {
        val meta = mockk<MetaEstudioMPAZ>()
        every { meta.id } returns id
        return meta
    }

    @Test
    fun errorAlCargarMuestraMensajeDelRepositorio() =
        runTest {

            coEvery {
                repositorio.listar()
            } throws ErrorMetasMPAZ(
                "Tu sesión ha expirado."
            )

            val viewModel = MetasViewModel(repositorio)

            viewModel.cargarMetas()

            advanceUntilIdle()

            assertEquals(
                "Tu sesión ha expirado.",
                viewModel.mensajeError.value
            )

            assertTrue(viewModel.metas.value.isEmpty())
            assertFalse(viewModel.cargando.value)
        }

    @Test
    fun errorDeValidacionAlGuardarMuestraMensaje() =
        runTest {

            coEvery {
                repositorio.crear(
                    any(),
                    any()
                )
            } throws ErrorMetasMPAZ(
                "Debes escribir el título de la meta."
            )

            val viewModel = MetasViewModel(repositorio)

            var exito = false

            viewModel.guardarMeta(
                id = null,
                titulo = "",
                descripcion = "Detalle",
                completada = false,
                onExito = {
                    exito = true
                }
            )

            advanceUntilIdle()

            assertEquals(
                "Debes escribir el título de la meta.",
                viewModel.mensajeError.value
            )

            assertFalse(exito)
            assertFalse(viewModel.procesando.value)
        }

    @Test
    fun errorDeConexionAlGuardarMuestraMensaje() =
        runTest {

            coEvery {
                repositorio.crear(
                    any(),
                    any()
                )
            } throws IOException("Servidor no disponible")

            val viewModel = MetasViewModel(repositorio)

            viewModel.guardarMeta(
                id = null,
                titulo = "Estudiar",
                descripcion = "Matematicas",
                completada = false
            )

            advanceUntilIdle()

            assertEquals(
                "No se pudo conectar con Spring Boot.",
                viewModel.mensajeError.value
            )

            assertFalse(viewModel.procesando.value)
        }

    @Test
    fun errorAlEliminarMetaMantieneEstadoSinProcesar() =
        runTest {

            coEvery {
                repositorio.eliminar(7L)
            } throws ErrorMetasMPAZ(
                "La meta no existe."
            )

            val viewModel = MetasViewModel(repositorio)

            var exito = false

            viewModel.eliminarMeta(7L) {
                exito = true
            }

            advanceUntilIdle()

            assertEquals(
                "La meta no existe.",
                viewModel.mensajeError.value
            )

            assertFalse(exito)
            assertFalse(viewModel.procesando.value)
        }

    @Test
    fun limpiarDuranteGuardadoDescartaResultadoAntiguo() =
        runTest {

            val pendiente =
                CompletableDeferred<MetaEstudioMPAZ>()

            coEvery {
                repositorio.crear(
                    any(),
                    any()
                )
            } coAnswers {
                pendiente.await()
            }

            val viewModel = MetasViewModel(repositorio)

            var exito = false

            viewModel.guardarMeta(
                id = null,
                titulo = "Leer",
                descripcion = "Libro",
                completada = false,
                onExito = {
                    exito = true
                }
            )

            runCurrent()

            assertTrue(viewModel.procesando.value)

            viewModel.limpiar()

            pendiente.complete(crearMeta(10L))

            advanceUntilIdle()

            assertTrue(viewModel.metas.value.isEmpty())
            assertFalse(viewModel.procesando.value)
            assertNull(viewModel.mensajeError.value)
            assertFalse(exito)
        }

    @Test
    fun limpiarDuranteEliminacionDescartaResultadoAntiguo() =
        runTest {

            val meta = crearMeta(5L)

            coEvery {
                repositorio.listar()
            } returns listOf(meta)

            val pendiente = CompletableDeferred<Unit>()

            coEvery {
                repositorio.eliminar(5L)
            } coAnswers {
                pendiente.await()
            }

            val viewModel = MetasViewModel(repositorio)

            viewModel.cargarMetas()
            advanceUntilIdle()

            assertEquals(1, viewModel.metas.value.size)

            var exito = false

            viewModel.eliminarMeta(5L) {
                exito = true
            }

            runCurrent()

            assertTrue(viewModel.procesando.value)

            viewModel.limpiar()

            pendiente.complete(Unit)

            advanceUntilIdle()

            assertTrue(viewModel.metas.value.isEmpty())
            assertFalse(viewModel.procesando.value)
            assertFalse(exito)
        }

    @Test
    fun impideGuardarMetaMientrasCargaListado() =
        runTest {

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

            viewModel.guardarMeta(
                id = null,
                titulo = "Nueva",
                descripcion = "Descripcion",
                completada = false
            )

            coVerify(exactly = 0) {
                repositorio.crear(
                    any(),
                    any()
                )
            }

            pendiente.complete(emptyList())

            advanceUntilIdle()

            assertFalse(viewModel.cargando.value)
            assertFalse(viewModel.procesando.value)
        }

    @Test
    fun impideEliminarMientrasSeGuardaOtraMeta() =
        runTest {

            val pendiente =
                CompletableDeferred<MetaEstudioMPAZ>()

            coEvery {
                repositorio.crear(
                    any(),
                    any()
                )
            } coAnswers {
                pendiente.await()
            }

            val viewModel = MetasViewModel(repositorio)

            viewModel.guardarMeta(
                id = null,
                titulo = "Meta",
                descripcion = "Detalle",
                completada = false
            )

            runCurrent()

            assertTrue(viewModel.procesando.value)

            viewModel.eliminarMeta(5L)

            coVerify(exactly = 0) {
                repositorio.eliminar(any())
            }

            pendiente.complete(crearMeta(6L))

            advanceUntilIdle()

            assertFalse(viewModel.procesando.value)
            assertEquals(1, viewModel.metas.value.size)
        }
}