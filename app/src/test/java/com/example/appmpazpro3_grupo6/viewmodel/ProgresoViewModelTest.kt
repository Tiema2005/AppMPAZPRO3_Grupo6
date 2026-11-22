package com.example.appmpazpro3_grupo6.viewmodel

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class ProgresoViewModelTest {

    private lateinit var dispatcher: TestDispatcher

    @Before
    fun preparar() {
        dispatcher = StandardTestDispatcher()
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun limpiar() {
        Dispatchers.resetMain()
    }

    private fun crearViewModel(): ProgresoViewModel {
        val application =
            RuntimeEnvironment.getApplication()

        return ProgresoViewModel(application)
    }

    @Test
    fun estadoInicialSinCuentaActiva() = runTest(dispatcher) {

        val viewModel = crearViewModel()

        runCurrent()

        assertNull(viewModel.cuentaActiva.value)
        assertNull(viewModel.errorGuardado.value)

        assertFalse(viewModel.cargandoProgreso.value)
        assertFalse(viewModel.sincronizando.value)

        assertTrue(viewModel.sesionesGuardando.value.isEmpty())
    }

    @Test
    fun progresoInicialEstaVacio() = runTest(dispatcher) {

        val viewModel = crearViewModel()

        runCurrent()

        val progreso = viewModel.progreso.value

        assertTrue(progreso.progresoPorNivel.isEmpty())
        assertTrue(progreso.resultados.isEmpty())
    }

    @Test
    fun cuentaNulaNoIniciaSincronizacion() = runTest(dispatcher) {

        val viewModel = crearViewModel()

        viewModel.activarCuenta(null)

        runCurrent()

        assertNull(viewModel.cuentaActiva.value)
        assertFalse(viewModel.sincronizando.value)
        assertFalse(viewModel.cargandoProgreso.value)
    }

    @Test
    fun correoEnBlancoNoActivaCuenta() = runTest(dispatcher) {

        val viewModel = crearViewModel()

        viewModel.activarCuenta("   ")

        runCurrent()

        assertNull(viewModel.cuentaActiva.value)
        assertFalse(viewModel.cargandoProgreso.value)
        assertFalse(viewModel.sincronizando.value)
    }

    @Test
    fun sincronizarSinCuentaNoRealizaCambios() = runTest(dispatcher) {

        val viewModel = crearViewModel()

        viewModel.sincronizarAhora()

        runCurrent()

        assertNull(viewModel.cuentaActiva.value)
        assertNull(viewModel.errorGuardado.value)
        assertFalse(viewModel.sincronizando.value)
    }

    @Test
    fun registrarEjercicioSinCuentaNoCambiaProgreso() = runTest(dispatcher) {

        val viewModel = crearViewModel()

        viewModel.registrarEjercicio(
            asignaturaId = "matematica_facil",
            cantidadCompletada = 3
        )

        runCurrent()

        assertTrue(
            viewModel.progreso.value.progresoPorNivel.isEmpty()
        )

        assertNull(viewModel.errorGuardado.value)
    }

    @Test
    fun registrarRespuestaSinCuentaNoCambiaProgreso() = runTest(dispatcher) {

        val viewModel = crearViewModel()

        viewModel.registrarRespuesta(
            asignaturaId = "matematica_facil",
            numeroEjercicio = 1,
            esCorrecta = true
        )

        runCurrent()

        assertTrue(
            viewModel.progreso.value.resultados.isEmpty()
        )

        assertFalse(viewModel.sincronizando.value)
    }

    @Test
    fun limpiarErrorMantieneEstadoSinError() = runTest(dispatcher) {

        val viewModel = crearViewModel()

        viewModel.limpiarError()

        runCurrent()

        assertNull(viewModel.errorGuardado.value)
    }
}