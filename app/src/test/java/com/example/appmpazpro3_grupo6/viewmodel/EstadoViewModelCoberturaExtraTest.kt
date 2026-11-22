package com.example.appmpazpro3_grupo6.viewmodel

import com.example.appmpazpro3_grupo6.repository.EstadoDataStore

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.withTimeout

import org.junit.After
import org.junit.Assert.assertFalse
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
class EstadoViewModelCoberturaExtraTest {

    @Before
    fun preparar() {
        Dispatchers.setMain(
            UnconfinedTestDispatcher()
        )
    }

    @After
    fun limpiar() {
        Dispatchers.resetMain()
    }

    @Test
    fun alternarEstadoGuardaCambioEnDataStore() = runBlocking {

        val application =
            RuntimeEnvironment.getApplication()

        val dataStore = EstadoDataStore(application)

        dataStore.guardarEstado(false)

        val viewModel = EstadoViewModel(application)

        val estadoInicial = withTimeout(10000) {
            viewModel.activo
                .filterNotNull()
                .first()
        }

        assertFalse(estadoInicial)

        viewModel.alternarEstado()

        val nuevoEstado = withTimeout(10000) {
            viewModel.activo
                .filterNotNull()
                .first { it }
        }

        assertTrue(nuevoEstado)

        val estadoGuardado = withTimeout(10000) {
            dataStore.obtenerEstado().first()
        }

        assertTrue(estadoGuardado)
    }
}
