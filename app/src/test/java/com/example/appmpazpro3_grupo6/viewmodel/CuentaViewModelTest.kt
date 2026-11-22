package com.example.appmpazpro3_grupo6.viewmodel

import com.example.appmpazpro3_grupo6.repository.CuentaEstudiante
import com.example.appmpazpro3_grupo6.repository.CuentaRemotaRepository

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
import org.junit.Assert.assertSame
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
class CuentaViewModelTest {

    private lateinit var dispatcher: TestDispatcher
    private lateinit var repositorio: CuentaRemotaRepository

    @Before
    fun preparar() {
        dispatcher = StandardTestDispatcher()
        Dispatchers.setMain(dispatcher)

        repositorio = mockk()
    }

    @After
    fun limpiarDispatcher() {
        Dispatchers.resetMain()
    }

    private fun crearViewModel(): CuentaViewModel {
        val application = RuntimeEnvironment.getApplication()

        return CuentaViewModel(
            application,
            repositorio
        )
    }

    @Test
    fun estadoInicialSinCuenta() = runTest(dispatcher) {

        val viewModel = crearViewModel()

        assertNull(viewModel.cuentaActual.value)
        assertFalse(viewModel.procesando.value)
        assertNull(viewModel.mensajeError.value)
    }

    @Test
    fun registrarCuentaCorrectamente() = runTest(dispatcher) {

        val estudiante = mockk<CuentaEstudiante>()

        coEvery {
            repositorio.registrarCuenta(
                nombre = "Emanuel",
                correo = "alumno@ejemplo.cl",
                contrasena = "Clave123"
            )
        } returns estudiante

        val viewModel = crearViewModel()

        var cuentaRegistrada: CuentaEstudiante? = null

        viewModel.registrar(
            nombre = "Emanuel",
            correo = "alumno@ejemplo.cl",
            contrasena = "Clave123",
            curso = "3° Básico",
            onExito = {
                cuentaRegistrada = it
            }
        )

        advanceUntilIdle()

        assertSame(estudiante, viewModel.cuentaActual.value)
        assertSame(estudiante, cuentaRegistrada)
        assertFalse(viewModel.procesando.value)
        assertNull(viewModel.mensajeError.value)

        coVerify(exactly = 1) {
            repositorio.registrarCuenta(
                "Emanuel",
                "alumno@ejemplo.cl",
                "Clave123"
            )
        }
    }

    @Test
    fun iniciarSesionCorrectamente() = runTest(dispatcher) {

        val estudiante = mockk<CuentaEstudiante>()

        coEvery {
            repositorio.iniciarSesion(
                correo = "alumno@ejemplo.cl",
                contrasena = "Clave123"
            )
        } returns estudiante

        val viewModel = crearViewModel()

        var inicioCorrecto = false

        viewModel.iniciarSesion(
            correo = "alumno@ejemplo.cl",
            contrasena = "Clave123",
            onExito = {
                inicioCorrecto = true
            }
        )

        advanceUntilIdle()

        assertTrue(inicioCorrecto)
        assertSame(estudiante, viewModel.cuentaActual.value)
        assertFalse(viewModel.procesando.value)
        assertNull(viewModel.mensajeError.value)
    }

    @Test
    fun cargarPerfilCorrectamente() = runTest(dispatcher) {

        val estudiante = mockk<CuentaEstudiante>()

        coEvery {
            repositorio.obtenerPerfil()
        } returns estudiante

        val viewModel = crearViewModel()

        var perfilCargado: CuentaEstudiante? = null

        viewModel.cargarPerfil {
            perfilCargado = it
        }

        advanceUntilIdle()

        assertSame(estudiante, perfilCargado)
        assertSame(estudiante, viewModel.cuentaActual.value)
        assertFalse(viewModel.procesando.value)

        coVerify(exactly = 1) {
            repositorio.obtenerPerfil()
        }
    }

    @Test
    fun errorConexionDuranteLogin() = runTest(dispatcher) {

        coEvery {
            repositorio.iniciarSesion(any(), any())
        } throws IOException("Servidor no disponible")

        val viewModel = crearViewModel()

        var exito = false

        viewModel.iniciarSesion(
            correo = "alumno@ejemplo.cl",
            contrasena = "Clave123",
            onExito = {
                exito = true
            }
        )

        advanceUntilIdle()

        assertFalse(exito)
        assertNull(viewModel.cuentaActual.value)
        assertFalse(viewModel.procesando.value)

        assertEquals(
            "No se pudo conectar con el servidor. " +
                    "Verifica que Spring Boot esté funcionando.",
            viewModel.mensajeError.value
        )
    }

    @Test
    fun errorGenericoDuranteRegistro() = runTest(dispatcher) {

        coEvery {
            repositorio.registrarCuenta(any(), any(), any())
        } throws IllegalStateException("Error simulado")

        val viewModel = crearViewModel()

        var exito = false

        viewModel.registrar(
            nombre = "Emanuel",
            correo = "alumno@ejemplo.cl",
            contrasena = "Clave123",
            curso = "3° Básico",
            onExito = {
                exito = true
            }
        )

        advanceUntilIdle()

        assertFalse(exito)
        assertNull(viewModel.cuentaActual.value)
        assertFalse(viewModel.procesando.value)

        assertEquals(
            "Ocurrió un error al crear la cuenta.",
            viewModel.mensajeError.value
        )
    }

    @Test
    fun cerrarSesionLimpiaCuenta() = runTest(dispatcher) {

        val estudiante = mockk<CuentaEstudiante>()

        coEvery {
            repositorio.iniciarSesion(any(), any())
        } returns estudiante

        coEvery {
            repositorio.cerrarSesion()
        } returns Unit

        val viewModel = crearViewModel()

        viewModel.iniciarSesion(
            correo = "alumno@ejemplo.cl",
            contrasena = "Clave123",
            onExito = {}
        )

        advanceUntilIdle()

        assertSame(estudiante, viewModel.cuentaActual.value)

        viewModel.cerrarSesion()

        advanceUntilIdle()

        assertNull(viewModel.cuentaActual.value)
        assertFalse(viewModel.procesando.value)

        coVerify(exactly = 1) {
            repositorio.cerrarSesion()
        }
    }

    @Test
    fun limpiarErrorEliminaMensaje() = runTest(dispatcher) {

        coEvery {
            repositorio.iniciarSesion(any(), any())
        } throws IOException("Error simulado")

        val viewModel = crearViewModel()

        viewModel.iniciarSesion(
            correo = "alumno@ejemplo.cl",
            contrasena = "Clave123",
            onExito = {}
        )

        advanceUntilIdle()

        assertTrue(viewModel.mensajeError.value != null)

        viewModel.limpiarError()

        assertNull(viewModel.mensajeError.value)
    }

    @Test
    fun evitaRegistrosSimultaneos() = runTest(dispatcher) {

        val estudiante = mockk<CuentaEstudiante>()

        val pendiente = CompletableDeferred<CuentaEstudiante>()

        coEvery {
            repositorio.registrarCuenta(any(), any(), any())
        } coAnswers {
            pendiente.await()
        }

        val viewModel = crearViewModel()

        viewModel.registrar(
            nombre = "Emanuel",
            correo = "alumno@ejemplo.cl",
            contrasena = "Clave123",
            curso = "3° Básico",
            onExito = {}
        )

        runCurrent()

        assertTrue(viewModel.procesando.value)

        viewModel.registrar(
            nombre = "Camilo",
            correo = "otro@ejemplo.cl",
            contrasena = "Clave456",
            curso = "3° Básico",
            onExito = {}
        )

        pendiente.complete(estudiante)

        advanceUntilIdle()

        coVerify(exactly = 1) {
            repositorio.registrarCuenta(any(), any(), any())
        }

        assertFalse(viewModel.procesando.value)
        assertSame(estudiante, viewModel.cuentaActual.value)
    }

    @Test
    fun errorAlCerrarSesionLimpiaCuenta() = runTest(dispatcher) {

        val estudiante = mockk<CuentaEstudiante>()

        coEvery {
            repositorio.iniciarSesion(any(), any())
        } returns estudiante

        coEvery {
            repositorio.cerrarSesion()
        } throws IOException("Error de conexion")

        val viewModel = crearViewModel()

        viewModel.iniciarSesion(
            correo = "alumno@ejemplo.cl",
            contrasena = "Clave123",
            onExito = {}
        )

        advanceUntilIdle()

        viewModel.cerrarSesion()

        advanceUntilIdle()

        assertNull(viewModel.cuentaActual.value)
        assertFalse(viewModel.procesando.value)

        assertEquals(
            "No se pudo confirmar el cierre de sesión en el servidor.",
            viewModel.mensajeError.value
        )
    }
}