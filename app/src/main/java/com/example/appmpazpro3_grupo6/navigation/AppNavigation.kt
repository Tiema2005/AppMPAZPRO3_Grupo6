package com.example.appmpazpro3_grupo6.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.example.appmpazpro3_grupo6.ui.screens.BienvenidaScreen
import com.example.appmpazpro3_grupo6.ui.screens.LoginScreen
import com.example.appmpazpro3_grupo6.ui.screens.RegistroScreen
import com.example.appmpazpro3_grupo6.ui.screens.PerfilScreen
import com.example.appmpazpro3_grupo6.ui.screens.AsignaturasScreen
import com.example.appmpazpro3_grupo6.ui.screens.AsignaturaDetalleScreen
import com.example.appmpazpro3_grupo6.ui.screens.ModoEstudioScreen
import com.example.appmpazpro3_grupo6.viewmodel.EstudianteViewModel
import com.example.appmpazpro3_grupo6.viewmodel.ProgresoViewModel

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    val estudianteViewModel: EstudianteViewModel = viewModel()

    val progresoViewModel: ProgresoViewModel = viewModel()

    val progresoActual by progresoViewModel.progreso.collectAsState()

    val errorProgreso by progresoViewModel.errorGuardado.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Routes.BIENVENIDA
    ) {

        composable(Routes.BIENVENIDA) {

            BienvenidaScreen(
                onComenzarClick = {
                    navController.navigate(Routes.LOGIN)
                }
            )
        }

        composable(Routes.LOGIN) {

            LoginScreen(
                onVolverClick = {
                    navController.popBackStack()
                },

                onCrearCuentaClick = {
                    navController.navigate(Routes.REGISTRO)
                },

                onIngresarClick = { nombre, curso ->

                    estudianteViewModel.registrarEstudiante(
                        nombre = nombre,
                        curso = curso
                    )

                    navController.navigate(Routes.PERFIL) {
                        popUpTo(Routes.LOGIN) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(Routes.REGISTRO) {

            RegistroScreen(
                onVolverClick = {
                    navController.popBackStack()
                },
                onRegistroExitoso = { nombre, curso ->

                    estudianteViewModel.registrarEstudiante(
                        nombre = nombre,
                        curso = curso
                    )

                    navController.navigate(Routes.PERFIL) {
                        popUpTo(Routes.REGISTRO) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(Routes.PERFIL) {

            val nombre = estudianteViewModel.nombreEstudiante
                .ifBlank { "Estudiante de prueba" }

            PerfilScreen(
                nombre = nombre,
                curso = estudianteViewModel.cursoEstudiante,

                progreso = progresoActual.progresoGeneral,

                totalEjerciciosRespondidos =
                    progresoActual.totalCompletados,

                progresoAsignaturas =
                    progresoActual.porcentajesPorAsignatura(),

                onContinuarClick = {
                    navController.navigate(Routes.ASIGNATURAS)
                },

                onModoEstudioClick = {
                    navController.navigate(Routes.MODO_ESTUDIO)
                },

                onVolverClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.ASIGNATURAS) {

            AsignaturasScreen(
                onVolverClick = {
                    navController.popBackStack()
                },

                onAsignaturaClick = { nombreAsignatura ->

                    val id = when (nombreAsignatura) {

                        "📖 Lenguaje y Comunicación" ->
                            "lenguaje"

                        "🔢 Matemática" ->
                            "matematica"

                        "🧪 Ciencias Naturales" ->
                            "ciencias"

                        "🌎 Historia, Geografía y Ciencias Sociales" ->
                            "historia"

                        else -> null
                    }

                    if (id != null) {
                        navController.navigate(
                            Routes.detalleAsignatura(id)
                        )
                    }
                }
            )
        }

        composable(
            route = "${Routes.DETALLE_ASIGNATURA}/{asignaturaId}"
        ) { backStackEntry ->

            val asignaturaId = backStackEntry.arguments
                ?.getString("asignaturaId")
                .orEmpty()

            AsignaturaDetalleScreen(
                asignaturaId = asignaturaId,

                porcentajeGuardado =
                    progresoActual.porcentaje(asignaturaId),

                errorGuardado = errorProgreso,

                onEjercicioRespondido = { cantidad ->
                    progresoViewModel.registrarEjercicio(
                        asignaturaId = asignaturaId,
                        cantidadCompletada = cantidad
                    )
                },

                onVolverClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.MODO_ESTUDIO) {

            ModoEstudioScreen(
                onVolverClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}