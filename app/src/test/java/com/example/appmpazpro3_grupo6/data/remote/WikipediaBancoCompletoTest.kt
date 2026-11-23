package com.example.appmpazpro3_grupo6.data.remote

import com.example.appmpazpro3_grupo6.ui.screens.DificultadMPAZ
import com.example.appmpazpro3_grupo6.ui.screens.preguntasMPAZ
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assume.assumeTrue
import org.junit.Test

class WikipediaBancoCompletoTest {

    private data class Caso(
        val identificador: String,
        val articulo: String
    )

    private val asignaturas = listOf(
        "lenguaje",
        "matematica",
        "ciencias",
        "historia"
    )

    private fun obtenerCasos(): List<Caso> {
        return asignaturas.flatMap { asignatura ->
            DificultadMPAZ.entries.flatMap { dificultad ->

                val preguntas = preguntasMPAZ(
                    asignatura,
                    dificultad
                )

                assertEquals(
                    "$asignatura - ${dificultad.id}",
                    5,
                    preguntas.size
                )

                preguntas.mapIndexed { indice, pregunta ->
                    Caso(
                        identificador =
                            "$asignatura/${dificultad.id}/${indice + 1}",
                        articulo = pregunta.articuloWikipedia
                    )
                }
            }
        }
    }

    @Test
    fun verificarSesentaEnlacesUnicos() {

        val casos = obtenerCasos()

        assertEquals(60, casos.size)

        val repetidos = casos.groupBy {
            it.articulo.trim().lowercase()
        }.filterValues {
            it.size > 1
        }

        assertTrue(
            "Articulos repetidos: $repetidos",
            repetidos.isEmpty()
        )

        assertEquals(
            "Deben existir 60 articulos diferentes",
            60,
            casos.map { it.articulo }.distinct().size
        )

        println("LT-70: 60 EJERCICIOS")
        println("LT-70: 60 ARTICULOS DIFERENTES")
        println("LT-70: SIN ENLACES DUPLICADOS")
    }

    @Test
    fun verificarSesentaArticulosEnWikipediaReal() = runBlocking {

        assumeTrue(
            "Prueba real bajo demanda",
            System.getenv("MPAZ_REAL_INTEGRATION") == "true"
        )

        val casos = obtenerCasos()
        val errores = mutableListOf<String>()

        for (caso in casos) {
            try {

                val respuesta = withTimeout(20000) {
                    WikipediaApi.servicio.buscarPaginas(
                        consulta = caso.articulo.replace('_', ' '),
                        limite = 10
                    )
                }

                val encontrado = respuesta.pages.orEmpty().any {
                    it.key.equals(
                        caso.articulo,
                        ignoreCase = true
                    )
                }

                if (encontrado) {
                    println("OK: ${caso.identificador} - ${caso.articulo}")
                } else {
                    errores.add(
                        "${caso.identificador}: ${caso.articulo}"
                    )
                }

            } catch (e: Exception) {
                errores.add(
                    "${caso.identificador}: ${caso.articulo} - " +
                            e.javaClass.simpleName
                )
            }
        }

        assertTrue(
            "Articulos sin coincidencia: ${errores.joinToString()}",
            errores.isEmpty()
        )

        println("LT-70: 60 ARTICULOS VERIFICADOS EN WIKIPEDIA")
    }
}