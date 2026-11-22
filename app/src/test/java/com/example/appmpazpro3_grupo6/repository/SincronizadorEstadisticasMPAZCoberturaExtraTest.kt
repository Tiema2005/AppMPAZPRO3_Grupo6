package com.example.appmpazpro3_grupo6.repository

import com.example.appmpazpro3_grupo6.data.remote.ProgresoRemotoMPAZ
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class SincronizadorEstadisticasMPAZCoberturaExtraTest {

    @Test
    fun noGuardaCuandoEstadisticasYaCoinciden() = runBlocking {
        val remoto = mockk<ProgresoRemotoRepository>()
        coEvery { remoto.listar() } returns listOf(
            ProgresoRemotoMPAZ(
                id = 1L,
                asignatura = "historia_facil",
                ejerciciosRespondidos = 1,
                respuestasCorrectas = 1,
                porcentajeAcierto = 100.0
            )
        )

        val progreso = ProgresoEstudiante(
            progresoPorNivel = mapOf("historia_facil" to 1),
            resultados = mapOf(
                "historia_facil" to mapOf(
                    1 to ResultadoEjercicio(intentos = 1, historial = listOf(true))
                )
            )
        )

        SincronizadorEstadisticasMPAZ(remoto).sincronizar(progreso)

        coVerify(exactly = 1) { remoto.listar() }
        coVerify(exactly = 0) { remoto.guardar(any(), any(), any()) }
        assertEquals(1, progreso.completados("historia_facil"))
    }

    @Test
    fun noEnviaNivelesVaciosSinEstadisticasPrevias() = runBlocking {
        val remoto = mockk<ProgresoRemotoRepository>()
        coEvery { remoto.listar() } returns emptyList()

        SincronizadorEstadisticasMPAZ(remoto).sincronizar(ProgresoEstudiante())

        coVerify(exactly = 1) { remoto.listar() }
        coVerify(exactly = 0) { remoto.guardar(any(), any(), any()) }
    }
}