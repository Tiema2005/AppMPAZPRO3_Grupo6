package com.example.appmpazpro3_grupo6.data.remote

import com.example.appmpazpro3_grupo6.repository.ProgresoRemotoRepository
import com.example.appmpazpro3_grupo6.repository.ProgresoEstudiante
import com.example.appmpazpro3_grupo6.repository.ResultadoEjercicio
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Assume.assumeTrue
import org.junit.Test
import java.util.UUID

class ProgresoIntegracionRealTest {

    @Test
    fun verificarProgresoRemotoConSpringBootReal() = runBlocking {

        assumeTrue(
            "LT-63 requiere Spring Boot activo",
            System.getenv("MPAZ_REAL_INTEGRATION") == "true"
        )

        val urlBackend = "http://127.0.0.1:8080/"

        val cliente = ClienteEstudiantesMPAZ.crear(urlBackend)
        val estudiantesApi = cliente.api
        val progresoApi = cliente.progresoApi

        val repositorio = ProgresoRemotoRepository(cliente)

        val identificador = UUID.randomUUID()
            .toString()
            .replace("-", "")

        val correo = "lt63_$identificador@example.com"
        val contrasena = "ClaveLT63_" + identificador.take(12)

        var segundoCliente: ClienteEstudiantesMPAZ? = null

        try {
            val sinSesion = progresoApi.listarProgreso()

            assertEquals(401, sinSesion.code())

            println("LT-63: PROTECCION DE PROGRESO OK")

            val registro = estudiantesApi.registrar(
                RegistroEstudianteMPAZ(
                    nombre = "Estudiante Prueba LT63",
                    correo = correo,
                    contrasena = contrasena
                )
            )

            assertEquals(201, registro.code())
            assertNotNull(registro.body())

            println("LT-63: REGISTRO OK")

            val login = estudiantesApi.iniciarSesion(
                LoginEstudianteMPAZ(
                    correo = correo,
                    contrasena = contrasena
                )
            )

            assertEquals(200, login.code())

            println("LT-63: INICIO DE SESION OK")

            val guardado = repositorio.guardar(
                asignaturaNivel = "matematica_facil",
                ejerciciosRespondidos = 3,
                respuestasCorrectas = 2
            )

            assertEquals(
                "matematica_facil",
                guardado.asignatura
            )

            assertEquals(3, guardado.ejerciciosRespondidos)
            assertEquals(2, guardado.respuestasCorrectas)

            assertEquals(
                66.67,
                guardado.porcentajeAcierto,
                1.0
            )

            println("LT-63: GUARDAR PROGRESO OK")

            val recuperado = repositorio.obtener(
                "matematica_facil"
            )

            assertNotNull(recuperado)

            val progreso = requireNotNull(recuperado)

            assertEquals(guardado.id, progreso.id)

            assertEquals(3, progreso.ejerciciosRespondidos)
            assertEquals(2, progreso.respuestasCorrectas)

            println("LT-63: CONSULTAR PROGRESO OK")

            val listado = repositorio.listar()

            assertTrue(
                listado.any {
                    it.asignatura == "matematica_facil" &&
                            it.ejerciciosRespondidos == 3 &&
                            it.respuestasCorrectas == 2
                }
            )

            println("LT-63: LISTAR PROGRESO OK")

            val progresoDetallado = ProgresoEstudiante(
                progresoPorNivel = mapOf(
                    "matematica_facil" to 3
                ),
                resultados = mapOf(
                    "matematica_facil" to mapOf(
                        1 to ResultadoEjercicio(
                            intentos = 1,
                            historial = listOf(true)
                        ),
                        2 to ResultadoEjercicio(
                            intentos = 1,
                            historial = listOf(false)
                        ),
                        3 to ResultadoEjercicio(
                            intentos = 1,
                            historial = listOf(true)
                        )
                    )
                )
            )

            val datosJson = Gson().toJson(progresoDetallado)

            val detalleGuardado = repositorio.guardarDetalle(
                datosJson
            )

            assertEquals(datosJson, detalleGuardado)

            println("LT-63: GUARDAR DETALLE OK")

            val detalleRecuperado = repositorio.obtenerDetalle()

            assertNotNull(detalleRecuperado)

            assertEquals(datosJson, detalleRecuperado)

            println("LT-63: RECUPERAR DETALLE OK")

            val logout = estudiantesApi.cerrarSesion()

            assertEquals(204, logout.code())

            cliente.limpiarSesion()

            println("LT-63: CIERRE DE SESION OK")

            val nuevoCliente = ClienteEstudiantesMPAZ.crear(
                urlBackend
            )

            segundoCliente = nuevoCliente

            val nuevoLogin = nuevoCliente.api.iniciarSesion(
                LoginEstudianteMPAZ(
                    correo = correo,
                    contrasena = contrasena
                )
            )

            assertEquals(200, nuevoLogin.code())

            val nuevoRepositorio = ProgresoRemotoRepository(
                nuevoCliente
            )
            val progresoPersistido = nuevoRepositorio.obtener(
                "matematica_facil"
            )

            assertNotNull(progresoPersistido)

            assertEquals(
                3,
                progresoPersistido?.ejerciciosRespondidos
            )

            assertEquals(
                2,
                progresoPersistido?.respuestasCorrectas
            )

            val detallePersistido =
                nuevoRepositorio.obtenerDetalle()

            assertEquals(datosJson, detallePersistido)

            println("LT-63: PERSISTENCIA ENTRE SESIONES OK")

            println("LT-63: INTEGRACION REAL EXITOSA")

        } finally {
            try {
                segundoCliente?.api?.cerrarSesion()
            } catch (_: Exception) {
            }

            segundoCliente?.limpiarSesion()

            try {
                estudiantesApi.cerrarSesion()
            } catch (_: Exception) {
            }

            cliente.limpiarSesion()
        }
    }
}