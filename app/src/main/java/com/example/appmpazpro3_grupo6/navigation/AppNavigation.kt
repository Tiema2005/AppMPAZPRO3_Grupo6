package com.example.appmpazpro3_grupo6.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appmpazpro3_grupo6.ui.screens.BienvenidaScreen
import com.example.appmpazpro3_grupo6.ui.screens.LoginScreen
import com.example.appmpazpro3_grupo6.ui.screens.RegistroScreen
import com.example.appmpazpro3_grupo6.ui.screens.PerfilScreen
import com.example.appmpazpro3_grupo6.ui.screens.AsignaturasScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
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
                }
            )
        }

        composable(Routes.PERFIL) {
            PerfilScreen(
                onContinuarClick = {
                    navController.navigate(Routes.ASIGNATURAS)
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
    }
}
