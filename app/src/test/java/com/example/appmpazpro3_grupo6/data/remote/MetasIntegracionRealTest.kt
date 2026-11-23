package com.example.appmpazpro3_grupo6.data.remote

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Assume.assumeTrue
import org.junit.Test
import java.util.UUID

class MetasIntegracionRealTest {

    @Test
    fun verificarCrudMetasConSpringBootReal() = runBlocking {

        assumeTrue(
            "LT-64 requiere Spring Boot activo",
            System.getenv("MPAZ_REAL_INTEGRATION") == "true"
        )

        val cliente = ClienteEstudiantesMPAZ.crear(
            "http://127.0.0.1:8080/"
        )

        val estudiantesApi = cliente.api
        val metasApi = cliente.metasApi

        val identificador = UUID.randomUUID()
            .toString()
            .replace("-", "")

        val correo = "lt64_$identificador@example.com"
        val contrasena = "ClaveLT64_$identificador"

        val sinSesion = metasApi.listarMetas()

        assertEquals(401, sinSesion.code())

        println("LT-64: PROTECCION DE METAS OK")

        val registro = estudiantesApi.registrar(
            RegistroEstudianteMPAZ(
                nombre = "Estudiante Prueba LT64",
                correo = correo,
                contrasena = contrasena
            )
        )

        assertEquals(201, registro.code())
        assertNotNull(registro.body())

        val login = estudiantesApi.iniciarSesion(
            LoginEstudianteMPAZ(
                correo = correo,
                contrasena = contrasena
            )
        )

        assertEquals(200, login.code())

        println("LT-64: AUTENTICACION OK")

        var idMeta: Long? = null
        var metaEliminada = false

        try {
            val listadoInicial = metasApi.listarMetas()

            assertEquals(200, listadoInicial.code())

            println("LT-64: LISTADO INICIAL OK")

            val respuestaCrear = metasApi.crearMeta(
                CrearMetaMPAZ(
                    titulo = "Practicar Matematica LT64",
                    descripcion = "Resolver cinco ejercicios"
                )
            )

            assertEquals(201, respuestaCrear.code())

            val creada = respuestaCrear.body()
            assertNotNull(creada)

            val metaCreada = requireNotNull(creada)
            idMeta = metaCreada.id

            assertEquals(
                "Practicar Matematica LT64",
                metaCreada.titulo
            )

            assertFalse(metaCreada.completada)

            println("LT-64: CREAR META OK")

            val respuestaObtener = metasApi.obtenerMeta(
                metaCreada.id
            )

            assertEquals(200, respuestaObtener.code())

            assertEquals(
                metaCreada.id,
                respuestaObtener.body()?.id
            )

            println("LT-64: CONSULTAR META OK")

            val respuestaListar = metasApi.listarMetas()

            assertEquals(200, respuestaListar.code())

            assertTrue(
                respuestaListar.body().orEmpty().any {
                    it.id == metaCreada.id
                }
            )

            println("LT-64: LISTAR META CREADA OK")

            val respuestaActualizar = metasApi.actualizarMeta(
                id = metaCreada.id,
                datos = ActualizarMetaMPAZ(
                    titulo = "Practicar Matematica LT64",
                    descripcion = "Resolver diez ejercicios",
                    completada = true
                )
            )

            assertEquals(200, respuestaActualizar.code())

            val actualizada = respuestaActualizar.body()

            assertNotNull(actualizada)

            assertEquals(
                "Resolver diez ejercicios",
                actualizada?.descripcion
            )

            assertTrue(actualizada?.completada == true)

            println("LT-64: ACTUALIZAR META OK")

            val respuestaPersistencia = metasApi.obtenerMeta(
                metaCreada.id
            )

            assertEquals(200, respuestaPersistencia.code())

            assertTrue(
                respuestaPersistencia.body()?.completada == true
            )

            println("LT-64: PERSISTENCIA OK")

            val respuestaEliminar = metasApi.eliminarMeta(
                metaCreada.id
            )

            assertEquals(204, respuestaEliminar.code())

            metaEliminada = true

            println("LT-64: ELIMINAR META OK")
            val respuestaEliminada = metasApi.obtenerMeta(
                metaCreada.id
            )

            assertEquals(404, respuestaEliminada.code())

            val listadoFinal = metasApi.listarMetas()

            assertEquals(200, listadoFinal.code())

            assertFalse(
                listadoFinal.body().orEmpty().any {
                    it.id == metaCreada.id
                }
            )

            println("LT-64: ELIMINACION VERIFICADA")

            println("LT-64: CRUD REAL EXITOSO")

        } finally {
            if (!metaEliminada) {
                idMeta?.let { id ->
                    try {
                        metasApi.eliminarMeta(id)
                    } catch (_: Exception) {
                    }
                }
            }

            try {
                estudiantesApi.cerrarSesion()
            } catch (_: Exception) {
            }

            cliente.limpiarSesion()
        }
    }
}