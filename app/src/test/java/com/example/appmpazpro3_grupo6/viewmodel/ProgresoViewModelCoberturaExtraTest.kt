package com.example.appmpazpro3_grupo6.viewmodel

import com.example.appmpazpro3_grupo6.repository.EstadoSincronizacionMPAZ
import com.example.appmpazpro3_grupo6.repository.ProgresoCuentaRepository
import com.example.appmpazpro3_grupo6.repository.ProgresoEstudiante
import com.example.appmpazpro3_grupo6.repository.ProgresoRemotoRepository
import com.example.appmpazpro3_grupo6.repository.SesionPractica
import com.example.appmpazpro3_grupo6.repository.SincronizadorEstadisticasMPAZ
import com.google.gson.Gson

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
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
class ProgresoViewModelCoberturaExtraTest {

    private lateinit var dispatcher: TestDispatcher
    private lateinit var local: ProgresoCuentaRepository
    private lateinit var remoto: ProgresoRemotoRepository
    private lateinit var versiones: EstadoSincronizacionMPAZ
    private lateinit var estadisticas: SincronizadorEstadisticasMPAZ

    private val correo = "alumno@ejemplo.cl"

    private val jsonVacio = Gson().toJson(
        ProgresoEstudiante(
            progresoPorNivel = emptyMap(),
            resultados = emptyMap(),
            sesionesPractica = emptyMap()
        )
    )

    @Before
    fun preparar() {
        dispatcher = StandardTestDispatcher()
        Dispatchers.setMain(dispatcher)

        local = mockk(relaxed = true)
        remoto = mockk(relaxed = true)
        versiones = mockk(relaxed = true)
        estadisticas = mockk(relaxed = true)

        every {
            local.observar(any())
        } returns flowOf(ProgresoEstudiante())

        coEvery {
            local.hayCambiosPendientes(any())
        } returns false

        coEvery {
            local.exportar(any())
        } returns jsonVacio

        coEvery {
            local.obtener(any())
        } returns ProgresoEstudiante()

        coEvery {
            remoto.obtenerDetalle()
        } returns null

        coEvery {
            remoto.guardarDetalle(any())
        } returns jsonVacio

        coEvery {
            versiones.obtenerVersion(any())
        } returns null

        every {
            versiones.calcularHash(any())
        } returns "hash-prueba"
    }

    @After
    fun limpiar() {
        Dispatchers.resetMain()
    }

    private fun crearViewModel(): ProgresoViewModel {
        val application =
            RuntimeEnvironment.getApplication()

        return ProgresoViewModel(
            application = application,
            local = local,
            remoto = remoto,
            versiones = versiones,
            estadisticas = estadisticas
        )
    }

    @Test
    fun normalizaCorreoYDescargaEstadoVacio() =
        runTest(dispatcher) {

            val viewModel = crearViewModel()

            viewModel.activarCuenta(
                "  ALUMNO@EJEMPLO.CL  "
            )

            advanceUntilIdle()

            assertEquals(
                correo,
                viewModel.cuentaActiva.value
            )

            assertFalse(viewModel.cargandoProgreso.value)
            assertFalse(viewModel.sincronizando.value)
            assertNull(viewModel.errorGuardado.value)

            coVerify(exactly = 1) {
                local.importar(correo, any())
            }

            coVerify(exactly = 1) {
                versiones.guardarVersion(correo, null)
            }
        }

    @Test
    fun descargaProgresoExistenteDesdeServidor() =
        runTest(dispatcher) {

            val datosRemotos = Gson().toJson(
                ProgresoEstudiante(
                    progresoPorNivel = mapOf(
                        "matematica_facil" to 3
                    ),
                    resultados = emptyMap(),
                    sesionesPractica = emptyMap()
                )
            )

            coEvery {
                remoto.obtenerDetalle()
            } returns datosRemotos

            val viewModel = crearViewModel()

            viewModel.activarCuenta(correo)

            advanceUntilIdle()

            coVerify(exactly = 1) {
                local.importar(
                    correo,
                    datosRemotos
                )
            }

            coVerify(exactly = 1) {
                versiones.guardarVersion(
                    correo,
                    "hash-prueba"
                )
            }

            assertNull(viewModel.errorGuardado.value)
            assertFalse(viewModel.cargandoProgreso.value)
        }

    @Test
    fun detectaConflictoYNoSobrescribeProgresoRemoto() =
        runTest(dispatcher) {

            val datosRemotos = Gson().toJson(
                ProgresoEstudiante(
                    progresoPorNivel = mapOf(
                        "ciencias_facil" to 2
                    ),
                    resultados = emptyMap(),
                    sesionesPractica = emptyMap()
                )
            )

            coEvery {
                local.hayCambiosPendientes(any())
            } returns true

            coEvery {
                remoto.obtenerDetalle()
            } returns datosRemotos

            coEvery {
                versiones.obtenerVersion(any())
            } returns "version-anterior"

            every {
                versiones.calcularHash(datosRemotos)
            } returns "version-remota"

            every {
                versiones.calcularHash(jsonVacio)
            } returns "version-local"

            val viewModel = crearViewModel()

            viewModel.activarCuenta(correo)

            advanceUntilIdle()

            assertEquals(
                "Hay cambios diferentes en el servidor. " +
                        "No sobrescribimos tu progreso local.",
                viewModel.errorGuardado.value
            )

            coVerify(exactly = 0) {
                remoto.guardarDetalle(any())
            }

            assertFalse(viewModel.sincronizando.value)
            assertFalse(viewModel.cargandoProgreso.value)
        }

    @Test
    fun progresoConHashIgualSoloConfirmaSincronizacion() =
        runTest(dispatcher) {

            coEvery {
                local.hayCambiosPendientes(any())
            } returns true

            coEvery {
                remoto.obtenerDetalle()
            } returns jsonVacio

            every {
                versiones.calcularHash(any())
            } returns "hash-igual"

            val viewModel = crearViewModel()

            viewModel.activarCuenta(correo)

            advanceUntilIdle()

            coVerify(exactly = 1) {
                local.confirmarSincronizacion(
                    correo,
                    jsonVacio
                )
            }

            coVerify(exactly = 0) {
                remoto.guardarDetalle(any())
            }

            coVerify(exactly = 1) {
                estadisticas.sincronizar(any())
            }

            assertNull(viewModel.errorGuardado.value)
        }

    @Test
    fun subeCambiosCuandoServidorNoTieneDetalle() =
        runTest(dispatcher) {

            coEvery {
                local.hayCambiosPendientes(any())
            } returns true

            coEvery {
                remoto.obtenerDetalle()
            } returns null

            coEvery {
                versiones.obtenerVersion(any())
            } returns null

            val viewModel = crearViewModel()

            viewModel.activarCuenta(correo)

            advanceUntilIdle()

            coVerify(exactly = 1) {
                remoto.guardarDetalle(jsonVacio)
            }

            coVerify(exactly = 1) {
                local.confirmarSincronizacion(
                    correo,
                    jsonVacio
                )
            }

            assertNull(viewModel.errorGuardado.value)
            assertFalse(viewModel.sincronizando.value)
        }

    @Test
    fun errorConexionDescargaMuestraMensaje() =
        runTest(dispatcher) {

            coEvery {
                remoto.obtenerDetalle()
            } throws IOException("Sin conexion")

            val viewModel = crearViewModel()

            viewModel.activarCuenta(correo)

            advanceUntilIdle()

            assertEquals(
                "El progreso está guardado localmente, " +
                        "pero no se pudo sincronizar con el servidor.",
                viewModel.errorGuardado.value
            )

            assertFalse(viewModel.sincronizando.value)
            assertFalse(viewModel.cargandoProgreso.value)
        }

    @Test
    fun registrarRespuestaConCuentaUsaRepositorioLocal() =
        runTest(dispatcher) {

            val viewModel = crearViewModel()

            viewModel.activarCuenta(correo)
            advanceUntilIdle()

            viewModel.registrarRespuesta(
                asignaturaId = "matematica_facil",
                numeroEjercicio = 2,
                esCorrecta = true
            )

            advanceUntilIdle()

            coVerify(exactly = 1) {
                local.registrarRespuesta(
                    correo,
                    "matematica_facil",
                    2,
                    true
                )
            }

            assertNull(viewModel.errorGuardado.value)
        }

    @Test
    fun registrarEjercicioConCuentaUsaRepositorioLocal() =
        runTest(dispatcher) {

            val viewModel = crearViewModel()

            viewModel.activarCuenta(correo)
            advanceUntilIdle()

            viewModel.registrarEjercicio(
                asignaturaId = "lenguaje_facil",
                cantidadCompletada = 4
            )

            advanceUntilIdle()

            coVerify(exactly = 1) {
                local.registrarEjercicioRespondido(
                    correo,
                    "lenguaje_facil",
                    4
                )
            }

            assertNull(viewModel.errorGuardado.value)
        }

    @Test
    fun evitaGuardarDosVecesLaMismaSesion() =
        runTest(dispatcher) {

            val pendiente = CompletableDeferred<Unit>()

            coEvery {
                local.guardarSesion(
                    any(),
                    any(),
                    any(),
                    any(),
                    any()
                )
            } coAnswers {
                pendiente.await()
            }

            val viewModel = crearViewModel()

            viewModel.activarCuenta(correo)
            advanceUntilIdle()

            val anterior = SesionPractica()

            val siguiente = SesionPractica(
                respuestaSeleccionada = "A"
            )

            viewModel.guardarSesion(
                "historia_facil",
                anterior,
                siguiente,
                true
            )

            runCurrent()

            assertTrue(
                "historia_facil" in
                        viewModel.sesionesGuardando.value
            )

            viewModel.guardarSesion(
                "historia_facil",
                anterior,
                siguiente,
                true
            )

            pendiente.complete(Unit)

            advanceUntilIdle()

            coVerify(exactly = 1) {
                local.guardarSesion(
                    correo,
                    "historia_facil",
                    anterior,
                    siguiente,
                    true
                )
            }

            assertTrue(
                viewModel.sesionesGuardando.value.isEmpty()
            )
        }

    @Test
    fun errorGuardadoSesionMuestraMensajeLocal() =
        runTest(dispatcher) {

            coEvery {
                local.guardarSesion(
                    any(),
                    any(),
                    any(),
                    any(),
                    any()
                )
            } throws IOException("Error local")

            val viewModel = crearViewModel()

            viewModel.activarCuenta(correo)
            advanceUntilIdle()

            viewModel.guardarSesion(
                "ciencias_facil",
                SesionPractica(),
                SesionPractica(
                    respuestaSeleccionada = "B"
                ),
                false
            )

            advanceUntilIdle()

            assertEquals(
                "No se pudo guardar la práctica local.",
                viewModel.errorGuardado.value
            )

            assertTrue(
                viewModel.sesionesGuardando.value.isEmpty()
            )
        }

    @Test
    fun errorAlRegistrarEjercicioMuestraMensaje() =
        runTest(dispatcher) {

            coEvery {
                local.registrarEjercicioRespondido(
                    any(),
                    any(),
                    any()
                )
            } throws IOException("No se pudo guardar")

            val viewModel = crearViewModel()

            viewModel.activarCuenta(correo)
            advanceUntilIdle()

            viewModel.registrarEjercicio(
                "matematica_facil",
                3
            )

            advanceUntilIdle()

            assertEquals(
                "El progreso está guardado localmente, " +
                        "pero no se pudo sincronizar con el servidor.",
                viewModel.errorGuardado.value
            )
        }

    @Test
    fun desactivarCuentaLimpiaEstadoDeSesion() =
        runTest(dispatcher) {

            val viewModel = crearViewModel()

            viewModel.activarCuenta(correo)
            advanceUntilIdle()

            assertEquals(
                correo,
                viewModel.cuentaActiva.value
            )

            viewModel.activarCuenta(null)

            advanceUntilIdle()

            assertNull(viewModel.cuentaActiva.value)
            assertNull(viewModel.errorGuardado.value)

            assertFalse(viewModel.cargandoProgreso.value)
            assertFalse(viewModel.sincronizando.value)

            assertTrue(
                viewModel.sesionesGuardando.value.isEmpty()
            )
        }
}