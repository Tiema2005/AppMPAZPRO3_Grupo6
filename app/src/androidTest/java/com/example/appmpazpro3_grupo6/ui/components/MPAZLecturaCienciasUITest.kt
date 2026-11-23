package com.example.appmpazpro3_grupo6.ui.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.appmpazpro3_grupo6.repository.LecturaCiencias
import com.example.appmpazpro3_grupo6.viewmodel.EstadoLecturaCiencias
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MPAZLecturaCienciasUITest {

    @get:Rule
    val composeRule = createComposeRule()

    private fun crearLectura(): LecturaCiencias {
        return LecturaCiencias(
            titulo = "Fotosíntesis",
            descripcion = "Proceso mediante el cual las plantas producen alimento",
            urlFuente = "https://es.wikipedia.org/wiki/Fotos%C3%ADntesis"
        )
    }

    @Test
    fun muestraEstadoCargando() {
        composeRule.setContent {
            MPAZLecturaCienciasContenido(
                estado = EstadoLecturaCiencias.Cargando,
                onAbrirWikipedia = {},
                onReintentar = {}
            )
        }

        composeRule.onNodeWithText(
            "Para aprender más"
        ).assertIsDisplayed()

        composeRule.onNodeWithText(
            "Cargando lectura…"
        ).assertIsDisplayed()

        composeRule.onNodeWithText(
            "Leer en Wikipedia"
        ).assertDoesNotExist()
    }

    @Test
    fun muestraLecturaDisponible() {
        composeRule.setContent {
            MPAZLecturaCienciasContenido(
                estado = EstadoLecturaCiencias.Disponible(
                    crearLectura()
                ),
                onAbrirWikipedia = {},
                onReintentar = {}
            )
        }

        composeRule.onNodeWithText(
            "Fotosíntesis"
        ).assertIsDisplayed()

        composeRule.onNodeWithText(
            "Proceso mediante el cual las plantas producen alimento"
        ).assertIsDisplayed()

        composeRule.onNodeWithText(
            "Leer en Wikipedia"
        ).assertIsDisplayed()
    }

    @Test
    fun botonWikipediaEntregaUrlCorrecta() {
        var urlAbierta: String? = null

        composeRule.setContent {
            MPAZLecturaCienciasContenido(
                estado = EstadoLecturaCiencias.Disponible(
                    crearLectura()
                ),
                onAbrirWikipedia = { url ->
                    urlAbierta = url
                },
                onReintentar = {}
            )
        }

        composeRule.onNodeWithText(
            "Leer en Wikipedia"
        ).performClick()

        assertEquals(
            "https://es.wikipedia.org/wiki/Fotos%C3%ADntesis",
            urlAbierta
        )
    }

    @Test
    fun lecturaSinDescripcionNoMuestraTextoAdicional() {
        val lectura = LecturaCiencias(
            titulo = "Mapa",
            descripcion = null,
            urlFuente = "https://es.wikipedia.org/wiki/Mapa"
        )

        composeRule.setContent {
            MPAZLecturaCienciasContenido(
                estado = EstadoLecturaCiencias.Disponible(lectura),
                onAbrirWikipedia = {},
                onReintentar = {}
            )
        }

        composeRule.onNodeWithText(
            "Mapa"
        ).assertIsDisplayed()

        composeRule.onNodeWithText(
            "Leer en Wikipedia"
        ).assertIsDisplayed()

        composeRule.onNodeWithText(
            "Cargando lectura…"
        ).assertDoesNotExist()
    }

    @Test
    fun sinResultadosPermiteReintentar() {
        var intentos = 0

        composeRule.setContent {
            MPAZLecturaCienciasContenido(
                estado = EstadoLecturaCiencias.SinResultados,
                onAbrirWikipedia = {},
                onReintentar = {
                    intentos++
                }
            )
        }

        composeRule.onNodeWithText(
            "La lectura no está disponible por ahora."
        ).assertIsDisplayed()

        composeRule.onNodeWithText(
            "Reintentar"
        ).performClick()

        assertEquals(1, intentos)
    }

    @Test
    fun errorDeConexionPermiteReintentar() {
        var intentos = 0
        var urlAbierta: String? = null

        composeRule.setContent {
            MPAZLecturaCienciasContenido(
                estado = EstadoLecturaCiencias.Error,
                onAbrirWikipedia = { url ->
                    urlAbierta = url
                },
                onReintentar = {
                    intentos++
                }
            )
        }

        composeRule.onNodeWithText(
            "No pudimos cargar la lectura. Revisa tu conexión."
        ).assertIsDisplayed()

        composeRule.onNodeWithText(
            "Reintentar"
        ).performClick()

        assertEquals(1, intentos)
        assertNull(urlAbierta)
    }
}