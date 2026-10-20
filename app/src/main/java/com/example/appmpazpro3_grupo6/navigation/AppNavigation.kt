package com.example.appmpazpro3_grupo6.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appmpazpro3_grupo6.ui.screens.BienvenidaScreen
import com.example.appmpazpro3_grupo6.ui.screens.LoginScreen
import com.example.appmpazpro3_grupo6.ui.screens.RegistroScreen
import com.example.appmpazpro3_grupo6.ui.screens.PerfilScreen
import com.example.appmpazpro3_grupo6.ui.screens.AsignaturasScreen
import com.example.appmpazpro3_grupo6.ui.screens.ModoEstudioScreen
import com.example.appmpazpro3_grupo6.viewmodel.EstudianteViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val estudianteViewModel: EstudianteViewModel = viewModel()
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
                onIngresarClick = {
                    navController.navigate(Routes.PERFIL)
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