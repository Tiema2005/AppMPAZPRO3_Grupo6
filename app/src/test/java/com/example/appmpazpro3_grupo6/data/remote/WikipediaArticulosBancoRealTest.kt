package com.example.appmpazpro3_grupo6.data.remote

import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertTrue
import org.junit.Assume.assumeTrue
import org.junit.Test

class WikipediaArticulosBancoRealTest {

    @Test
    fun validarArticulosDeTodasLasAsignaturas() = runBlocking {

        assumeTrue(
            "Prueba real de Wikipedia bajo demanda",
            System.getenv("MPAZ_REAL_INTEGRATION") == "true"
        )

        val articulos = mapOf(
            "Lenguaje" to listOf(
                "Sustantivo",
                "Adjetivo",
                "Inferencia"
            ),
            "Matemática" to listOf(
                "Adición_(matemática)",
                "Notación_posicional",
                "Resta"
            ),
            "Ciencias Naturales" to listOf(
                "Fotosíntesis",
                "Raíz_(botánica)",
                "Evaporación"
            ),
            "Historia y Geografía" to listOf(
                "Mapa",
                "Este",
                "Rosa_de_los_vientos"
            )
        )

        val problemas = mutableListOf<String>()
        var correctos = 0

        for ((asignatura, temas) in articulos) {

            println("ASIGNATURA: $asignatura")

            for (articulo in temas) {

                try {
                    val respuesta = withTimeout(30000) {
                        WikipediaApi.servicio.buscarPaginas(
                            consulta = articulo.replace('_', ' '),
                            limite = 10
                        )
                    }

                    val encontrado = respuesta.pages.orEmpty().any {
                        it.key.equals(
                            articulo,
                            ignoreCase = true
                        )
                    }

                    if (encontrado) {
                        correctos++
                        println("OK: $articulo")
                    } else {
                        problemas.add("$asignatura: $articulo")
                        println("SIN COINCIDENCIA: $articulo")
                    }

                } catch (e: Exception) {
                    problemas.add(
                        "$asignatura: $articulo (${e.javaClass.simpleName})"
                    )
                    println("ERROR: $articulo - ${e.message}")
                }
            }
        }

        println("ARTICULOS VERIFICADOS: $correctos / 12")

        assertTrue(
            "Artículos con problemas: ${problemas.joinToString()}",
            problemas.isEmpty()
        )
    }
}