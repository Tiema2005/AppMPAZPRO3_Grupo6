package com.example.appmpazpro3_grupo6.viewmodel

import com.example.appmpazpro3_grupo6.repository.CuentaEstudiante
import com.example.appmpazpro3_grupo6.repository.CuentaRemotaRepository
import com.example.appmpazpro3_grupo6.repository.ErrorCuentaRemota

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
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
import org.junit.runner.RunWith

import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class CuentaViewModelCoberturaExtraTest {

    private lateinit var dispatcher: TestDispatcher
    private lateinit var repositorio: CuentaRemotaRepository

    @Before
    fun preparar() {
        dispatcher = StandardTestDispatcher()
        Dispatchers.setMain(dispatcher)
        repositorio = mockk()
    }

    @After
    fun limpiar() {
        Dispatchers.resetMain()
    }

    private fun crearViewModel(): CuentaViewModel {
        return CuentaViewModel(
            RuntimeEnvironment.getApplication(),
            repositorio
        )
    }

    @Test
    fun registroDuplicadoMuestraErrorRemoto() =
        runTest(dispatcher) {

            coEvery {
                repositorio.registrarCuenta(
                    any(),
                    any(),
                    any()
                )
            } throws ErrorCuentaRemota(
                "Este correo ya está registrado."
            )

            val viewModel = crearViewModel()
            var exito = false

            viewModel.registrar(
                "Alumno",
                "alumno@ejemplo.cl",
                "Clave123",
                "3° Básico"
            ) {
                exito = true
            }

            advanceUntilIdle()

            assertEquals(
                "Este correo ya está registrado.",
                viewModel.mensajeError.value
            )

            assertNull(viewModel.cuentaActual.value)
            assertFalse(viewModel.procesando.value)
            assertFalse(exito)
        }

    @Test
    fun registroSinConexionMuestraMensaje() =
        runTest(dispatcher) {

            coEvery {
                repositorio.registrarCuenta(
                    any(),
                    any(),
                    any()
                )
            } throws IOException("Servidor desconectado")

            val viewModel = crearViewModel()

            viewModel.registrar(
                "Alumno",
                "alumno@ejemplo.cl",
                "Clave123",
                "3° Básico"
            ) {}

            advanceUntilIdle()

            assertEquals(
                "No se pudo conectar con el servidor. " +
                        "Verifica que Spring Boot esté funcionando.",
                viewModel.mensajeError.value
            )

            assertFalse(viewModel.procesando.value)
        }

    @Test
    fun loginConCredencialesInvalidasMuestraErrorRemoto() =
        runTest(dispatcher) {

            coEvery {
                repositorio.iniciarSesion(
                    any(),
                    any()
                )
            } throws ErrorCuentaRemota(
                "Correo o contraseña incorrectos."
            )

            val viewModel = crearViewModel()

            var exito = false

            viewModel.iniciarSesion(
                "alumno@ejemplo.cl",
                "Incorrecta"
            ) {
                exito = true
            }

            advanceUntilIdle()

            assertEquals(
                "Correo o contraseña incorrectos.",
                viewModel.mensajeError.value
            )

            assertNull(viewModel.cuentaActual.value)
            assertFalse(viewModel.procesando.value)
            assertFalse(exito)
        }

    @Test
    fun loginConErrorInesperadoMuestraMensajeGeneral() =
        runTest(dispatcher) {

            coEvery {
                repositorio.iniciarSesion(
                    any(),
                    any()
                )
            } throws IllegalStateException(
                "Error inesperado"
            )

            val viewModel = crearViewModel()

            viewModel.iniciarSesion(
                "alumno@ejemplo.cl",
                "Clave123"
            ) {}

            advanceUntilIdle()

            assertEquals(
                "Ocurrió un error al iniciar sesión.",
                viewModel.mensajeError.value
            )

            assertFalse(viewModel.procesando.value)
        }

    @Test
    fun perfilConSesionExpiradaMuestraError() =
        runTest(dispatcher) {

            coEvery {
                repositorio.obtenerPerfil()
            } throws ErrorCuentaRemota(
                "La sesión ha expirado."
            )

            val viewModel = crearViewModel()

            viewModel.cargarPerfil()

            advanceUntilIdle()

            assertEquals(
                "La sesión ha expirado.",
                viewModel.mensajeError.value
            )

            assertNull(viewModel.cuentaActual.value)
            assertFalse(viewModel.procesando.value)
        }

    @Test
    fun perfilSinConexionMuestraMensaje() =
        runTest(dispatcher) {

            coEvery {
                repositorio.obtenerPerfil()
            } throws IOException("Sin conexion")

            val viewModel = crearViewModel()

            viewModel.cargarPerfil()

            advanceUntilIdle()

            assertEquals(
                "No fue posible consultar el perfil en el servidor.",
                viewModel.mensajeError.value
            )

            assertFalse(viewModel.procesando.value)
        }

    @Test
    fun errorInesperadoEnPerfilMuestraMensajeGeneral() =
        runTest(dispatcher) {

            coEvery {
                repositorio.obtenerPerfil()
            } throws IllegalStateException("Error")

            val viewModel = crearViewModel()

            viewModel.cargarPerfil()

            advanceUntilIdle()

            assertEquals(
                "Ocurrió un error al cargar el perfil.",
                viewModel.mensajeError.value
            )
        }

    @Test
    fun noCierraSesionMientrasLoginEstaProcesando() =
        runTest(dispatcher) {

            val pendiente =
                CompletableDeferred<CuentaEstudiante>()

            val estudiante = mockk<CuentaEstudiante>()

            coEvery {
                repositorio.iniciarSesion(
                    any(),
                    any()
                )
            } coAnswers {
                pendiente.await()
            }

            val viewModel = crearViewModel()

            viewModel.iniciarSesion(
                "alumno@ejemplo.cl",
                "Clave123"
            ) {}

            runCurrent()

            assertTrue(viewModel.procesando.value)

            viewModel.cerrarSesion()

            coVerify(exactly = 0) {
                repositorio.cerrarSesion()
            }

            pendiente.complete(estudiante)

            advanceUntilIdle()

            assertFalse(viewModel.procesando.value)
        }

    @Test
    fun noConsultaPerfilMientrasRegistroEstaProcesando() =
        runTest(dispatcher) {

            val pendiente =
                CompletableDeferred<CuentaEstudiante>()

            val estudiante = mockk<CuentaEstudiante>()

            coEvery {
                repositorio.registrarCuenta(
                    any(),
                    any(),
                    any()
                )
            } coAnswers {
                pendiente.await()
            }

            val viewModel = crearViewModel()

            viewModel.registrar(
                "Alumno",
                "alumno@ejemplo.cl",
                "Clave123",
                "3° Básico"
            ) {}

            runCurrent()

            assertTrue(viewModel.procesando.value)

            viewModel.cargarPerfil()

            coVerify(exactly = 0) {
                repositorio.obtenerPerfil()
            }

            pendiente.complete(estudiante)

            advanceUntilIdle()

            assertFalse(viewModel.procesando.value)
        }
}