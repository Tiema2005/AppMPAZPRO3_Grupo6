package com.example.appmpazpro3_grupo6.data.remote

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Assume.assumeTrue
import org.junit.Test
import java.util.UUID

class EstudiantesIntegracionRealTest {

    @Test
    fun verificarRegistroLoginPerfilYCierreSesion() = runBlocking {

        assumeTrue(
            "LT-65 requiere Spring Boot activo",
            System.getenv("MPAZ_REAL_INTEGRATION") == "true"
        )

        val cliente = ClienteEstudiantesMPAZ.crear(
            "http://127.0.0.1:8080/"
        )

        val api = cliente.api

        val identificador = UUID.randomUUID()
            .toString()
            .replace("-", "")

        val correo = "lt65_$identificador@example.com"
        val contrasena = "Lt65!" + UUID.randomUUID()
            .toString()
            .replace("-", "")

        val registro = api.registrar(
            RegistroEstudianteMPAZ(
                nombre = "Estudiante Prueba LT65",
                correo = correo,
                contrasena = contrasena
            )
        )

        assertEquals(201, registro.code())
        assertNotNull(registro.body())
        assertEquals(correo, registro.body()?.correo)

        println("LT-65 REGISTRO: OK")

        val perfilSinSesion = api.obtenerPerfil()

        assertEquals(401, perfilSinSesion.code())

        println("LT-65 PROTECCION DE PERFIL: OK")

        val login = api.iniciarSesion(
            LoginEstudianteMPAZ(
                correo = correo,
                contrasena = contrasena
            )
        )

        assertEquals(200, login.code())
        assertNotNull(login.body())
        assertEquals(correo, login.body()?.correo)

        println("LT-65 LOGIN: OK")

        val perfil = api.obtenerPerfil()

        assertEquals(200, perfil.code())
        assertEquals(correo, perfil.body()?.correo)

        assertEquals(
            "Estudiante Prueba LT65",
            perfil.body()?.nombre
        )

        assertEquals(
            "3° Básico",
            perfil.body()?.curso
        )

        println("LT-65 PERFIL: OK")

        val logout = api.cerrarSesion()

        assertEquals(204, logout.code())

        cliente.limpiarSesion()

        println("LT-65 LOGOUT: OK")

        val perfilDespuesLogout = api.obtenerPerfil()

        assertEquals(401, perfilDespuesLogout.code())

        println("LT-65 SESION CERRADA: OK")

        val loginIncorrecto = api.iniciarSesion(
            LoginEstudianteMPAZ(
                correo = correo,
                contrasena = "Incorrecta123!"
            )
        )

        assertEquals(401, loginIncorrecto.code())

        println("LT-65 CREDENCIALES INVALIDAS: OK")

        println("LT-65: INTEGRACION REAL EXITOSA")
        assertTrue(true)
    }
}