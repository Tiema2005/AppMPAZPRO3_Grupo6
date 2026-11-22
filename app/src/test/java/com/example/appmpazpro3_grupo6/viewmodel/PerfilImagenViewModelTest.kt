package com.example.appmpazpro3_grupo6.viewmodel

import android.app.Application
import android.content.Context
import android.net.Uri

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
import org.junit.Assert.assertArrayEquals
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

import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class PerfilImagenViewModelTest {

    private lateinit var application: Application
    private lateinit var carpeta: File

    @Before
    fun preparar() {
        Dispatchers.setMain(UnconfinedTestDispatcher())

        application = RuntimeEnvironment.getApplication()

        carpeta = File(application.filesDir, "perfil")

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
            "imagen_prueba.jpg"
        ).delete()

        application.getSharedPreferences(
            "imagen_perfil",
            Context.MODE_PRIVATE
        ).edit().clear().commit()

        Dispatchers.resetMain()
    }

    @Test
    fun perfilInicialSinImagen() {

        val viewModel = PerfilImagenViewModel(application)

        assertNull(viewModel.imagenUri.value)
    }

    @Test
    fun recuperaImagenGuardada() {

        val archivo = File(carpeta, "foto_guardada.jpg")

        archivo.writeBytes(
            byteArrayOf(1, 2, 3, 4, 5)
        )

        application.getSharedPreferences(
            "imagen_perfil",
            Context.MODE_PRIVATE
        ).edit()
            .putString("archivo_imagen", archivo.name)
            .commit()

        val viewModel = PerfilImagenViewModel(application)

        assertEquals(
            Uri.fromFile(archivo),
            viewModel.imagenUri.value
        )
    }

    @Test
    fun ignoraImagenVacia() {

        val archivo = File(carpeta, "foto_vacia.jpg")

        archivo.createNewFile()

        application.getSharedPreferences(
            "imagen_perfil",
            Context.MODE_PRIVATE
        ).edit()
            .putString("archivo_imagen", archivo.name)
            .commit()

        val viewModel = PerfilImagenViewModel(application)

        assertNull(viewModel.imagenUri.value)
    }

    @Test
    fun recuperaImagenConNombrePredeterminado() {

        val archivo = File(carpeta, "foto_perfil.jpg")

        archivo.writeBytes(
            byteArrayOf(10, 20, 30)
        )

        val viewModel = PerfilImagenViewModel(application)

        assertEquals(
            Uri.fromFile(archivo),
            viewModel.imagenUri.value
        )
    }

    @Test
    fun cancelarCamaraEliminaArchivoTemporal() {

        val temporal = File(
            carpeta,
            "captura_temporal.jpg"
        )

        temporal.writeBytes(
            byteArrayOf(1, 2, 3)
        )

        assertTrue(temporal.exists())

        val viewModel = PerfilImagenViewModel(application)

        viewModel.cancelarCamara()

        assertFalse(temporal.exists())
    }

    @Test
    fun guardarImagenDesdeGaleria() = runBlocking {

        val origen = File(
            application.cacheDir,
            "imagen_prueba.jpg"
        )

        val contenido = byteArrayOf(
            1, 2, 3, 4, 5, 6
        )

        origen.writeBytes(contenido)

        val viewModel = PerfilImagenViewModel(application)

        viewModel.actualizarDesdeGaleria(
            Uri.fromFile(origen)
        )

        val imagenGuardada = withTimeout(10000) {
            viewModel.imagenUri.filterNotNull().first()
        }

        val archivoGuardado = File(
            requireNotNull(imagenGuardada.path)
        )

        assertTrue(archivoGuardado.exists())

        assertArrayEquals(
            contenido,
            archivoGuardado.readBytes()
        )

        val preferencias = application.getSharedPreferences(
            "imagen_perfil",
            Context.MODE_PRIVATE
        )

        assertEquals(
            archivoGuardado.name,
            preferencias.getString("archivo_imagen", null)
        )

        val nuevoViewModel =
            PerfilImagenViewModel(application)

        assertEquals(
            imagenGuardada,
            nuevoViewModel.imagenUri.value
        )
    }
}