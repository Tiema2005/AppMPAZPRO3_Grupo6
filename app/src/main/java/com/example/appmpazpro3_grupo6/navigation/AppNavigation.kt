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
import com.example.appmpazpro3_grupo6.viewmodel.EstadoViewModel

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    val estudianteViewModel: EstudianteViewModel = viewModel()
    val progresoViewModel: ProgresoViewModel = viewModel()
    val estadoViewModel: EstadoViewModel = viewModel()

    val progresoActual by progresoViewModel.progreso.collectAsState()
    val errorProgreso by progresoViewModel.errorGuardado.collectAsState()
    val modoEstudioActivo by estadoViewModel.activo.collectAsState()

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

            val rendimientoGeneral =
                progresoActual.rendimientoGeneral

            val rendimientoPorAsignatura = linkedMapOf(
                "Lenguaje y Comunicación" to
                        progresoActual.porcentajeRendimiento("lenguaje"),

                "Matemática" to
                        progresoActual.porcentajeRendimiento("matematica"),

                "Ciencias Naturales" to
                        progresoActual.porcentajeRendimiento("ciencias"),

                "Historia y Geografía" to
                        progresoActual.porcentajeRendimiento("historia")
            )

            PerfilScreen(
                nombre = nombre,

                curso = estudianteViewModel.cursoEstudiante,

                progreso = progresoActual.progresoGeneral,

                totalEjerciciosRespondidos =
                    progresoActual.totalCompletados,

                progresoAsignaturas =
                    progresoActual.porcentajesPorAsignatura(),

                rendimientoGeneral =
                    progresoActual.porcentajeRendimientoGeneral,

                aciertosGeneral =
                    rendimientoGeneral.respuestasCorrectas,

                ejerciciosEvaluados =
                    rendimientoGeneral.ejerciciosEvaluados,

                rendimientoAsignaturas =
                    rendimientoPorAsignatura,

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

                    val id = when {
                        nombreAsignatura.contains(
                            "Lenguaje",
                            ignoreCase = true
                        ) -> "lenguaje"

                        nombreAsignatura.contains(
                            "Matemática",
                            ignoreCase = true
                        ) -> "matematica"

                        nombreAsignatura.contains(
                            "Historia",
                            ignoreCase = true
                        ) -> "historia"

                        nombreAsignatura.contains(
                            "Ciencias",
                            ignoreCase = true
                        ) -> "ciencias"

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


                onRespuestaRegistrada = {
                        numeroEjercicio, esCorrecta ->

                    progresoViewModel.registrarRespuesta(
                        asignaturaId = asignaturaId,
                        numeroEjercicio = numeroEjercicio,
                        esCorrecta = esCorrecta
                    )
                },

                modoEstudioActivo = modoEstudioActivo == true,

                onVolverClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.MODO_ESTUDIO) {
            ModoEstudioScreen(
                onVolverClick = {
                    navController.popBackStack()
                },

                estadoViewModel = estadoViewModel
            )
        }
    }
}