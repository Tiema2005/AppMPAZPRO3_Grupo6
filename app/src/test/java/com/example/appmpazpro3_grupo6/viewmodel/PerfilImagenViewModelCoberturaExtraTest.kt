package com.example.appmpazpro3_grupo6.viewmodel

import android.app.Application
import android.content.Context
import android.net.Uri

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.withTimeout

import org.junit.After
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class PerfilImagenViewModelCoberturaExtraTest {

    private lateinit var application: Application
    private lateinit var carpeta: File

    @Before
    fun preparar() {
        Dispatchers.setMain(UnconfinedTestDispatcher())

        application = RuntimeEnvironment.getApplication()

        carpeta = File(
            application.filesDir,
            "perfil"
        )

        application.getSharedPreferences(
            "imagen_perfil",
            Context.MODE_PRIVATE
        ).edit().clear().commit()

        carpeta.deleteRecursively()
        carpeta.mkdirs()
    }

    @After
    fun limpiar() {
        carpeta.deleteRecursively()

        File(
            application.cacheDir,
            "foto_prueba_1.jpg"
        ).delete()

        File(
            application.cacheDir,
            "foto_prueba_2.jpg"
        ).delete()

        application.getSharedPreferences(
            "imagen_perfil",
            Context.MODE_PRIVATE
        ).edit().clear().commit()

        Dispatchers.resetMain()
    }

    @Test
    fun reemplazarFotografiaEliminaImagenAnterior() =
        runBlocking {

            val origen1 = File(
                application.cacheDir,
                "foto_prueba_1.jpg"
            )

            val origen2 = File(
                application.cacheDir,
                "foto_prueba_2.jpg"
            )

            origen1.writeBytes(
                byteArrayOf(1, 2, 3)
            )

            origen2.writeBytes(
                byteArrayOf(4, 5, 6, 7)
            )

            val viewModel =
                PerfilImagenViewModel(application)

            viewModel.actualizarDesdeGaleria(
                Uri.fromFile(origen1)
            )

            val primeraImagen = withTimeout(10000) {
                viewModel.imagenUri
                    .filterNotNull()
                    .first()
            }

            val archivoAnterior = File(
                requireNotNull(primeraImagen.path)
            )

            assertTrue(archivoAnterior.exists())

            viewModel.actualizarDesdeGaleria(
                Uri.fromFile(origen2)
            )

            val segundaImagen = withTimeout(10000) {
                viewModel.imagenUri
                    .filterNotNull()
                    .first {
                        it != primeraImagen
                    }
            }

            val archivoNuevo = File(
                requireNotNull(segundaImagen.path)
            )

            withTimeout(10000) {
                while (archivoAnterior.exists()) {
                    delay(10)
                }
            }

            assertTrue(archivoNuevo.exists())
            assertFalse(archivoAnterior.exists())

            assertArrayEquals(
                byteArrayOf(4, 5, 6, 7),
                archivoNuevo.readBytes()
            )

            val preferencias =
                application.getSharedPreferences(
                    "imagen_perfil",
                    Context.MODE_PRIVATE
                )

            assertEquals(
                archivoNuevo.name,
                preferencias.getString(
                    "archivo_imagen",
                    null
                )
            )
        }

    @Test
    fun imagenDesdeCamaraEliminaCapturaTemporal() =
        runBlocking {

            val temporal = File(
                carpeta,
                "captura_temporal.jpg"
            )

            val contenido = byteArrayOf(
                10, 20, 30, 40, 50
            )

            temporal.writeBytes(contenido)

            assertTrue(temporal.exists())

            val viewModel =
                PerfilImagenViewModel(application)

            viewModel.actualizarDesdeCamara(
                Uri.fromFile(temporal)
            )

            val imagenGuardada = withTimeout(10000) {
                viewModel.imagenUri
                    .filterNotNull()
                    .first()
            }

            val guardado = File(
                requireNotNull(imagenGuardada.path)
            )

            withTimeout(10000) {
                while (temporal.exists()) {
                    delay(10)
                }
            }

            assertTrue(guardado.exists())
            assertFalse(temporal.exists())

            assertArrayEquals(
                contenido,
                guardado.readBytes()
            )
        }
}