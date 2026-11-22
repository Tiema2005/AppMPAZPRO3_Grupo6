package com.example.appmpazpro3_grupo6.viewmodel

import org.junit.Assert.assertEquals
import org.junit.Test

class EstudianteViewModelTest {

    @Test
    fun nombreInicialEstaVacio() {
        val viewModel = EstudianteViewModel()

        assertEquals(
            "",
            viewModel.nombreEstudiante
        )
    }

    @Test
    fun cursoInicialEsTerceroBasico() {
        val viewModel = EstudianteViewModel()

        assertEquals(
            "3° Básico",
            viewModel.cursoEstudiante
        )
    }

    @Test
    fun registraNombreCorrectamente() {
        val viewModel = EstudianteViewModel()

        viewModel.registrarEstudiante(
            "Emanuel",
            "3° Básico"
        )

        assertEquals(
            "Emanuel",
            viewModel.nombreEstudiante
        )
    }

    @Test
    fun registraCursoCorrectamente() {
        val viewModel = EstudianteViewModel()

        viewModel.registrarEstudiante(
            "Camilo",
            "4° Básico"
        )

        assertEquals(
            "4° Básico",
            viewModel.cursoEstudiante
        )
    }

    @Test
    fun actualizaDatosDelEstudiante() {
        val viewModel = EstudianteViewModel()

        viewModel.registrarEstudiante(
            "Emanuel",
            "3° Básico"
        )

        viewModel.registrarEstudiante(
            "Ervin",
            "4° Básico"
        )

        assertEquals(
            "Ervin",
            viewModel.nombreEstudiante
        )

        assertEquals(
            "4° Básico",
            viewModel.cursoEstudiante
        )
    }

    @Test
    fun dosEstudiantesMantienenDatosIndependientes() {
        val estudiante1 = EstudianteViewModel()
        val estudiante2 = EstudianteViewModel()

        estudiante1.registrarEstudiante(
            "Emanuel",
            "3° Básico"
        )

        estudiante2.registrarEstudiante(
            "Camilo",
            "4° Básico"
        )

        assertEquals(
            "Emanuel",
            estudiante1.nombreEstudiante
        )

        assertEquals(
            "Camilo",
            estudiante2.nombreEstudiante
        )
    }
}