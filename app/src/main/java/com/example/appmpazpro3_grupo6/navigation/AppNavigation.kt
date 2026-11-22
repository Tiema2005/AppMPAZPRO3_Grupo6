
package com.example.appmpazpro3_grupo6.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appmpazpro3_grupo6.ui.screens.AsignaturaDetalleScreen
import com.example.appmpazpro3_grupo6.ui.screens.AsignaturasScreen
import com.example.appmpazpro3_grupo6.ui.screens.BienvenidaScreen
import com.example.appmpazpro3_grupo6.ui.screens.LoginScreen
import com.example.appmpazpro3_grupo6.ui.screens.ModoEstudioScreen
import com.example.appmpazpro3_grupo6.ui.screens.PerfilScreen
import com.example.appmpazpro3_grupo6.ui.screens.RegistroScreen
import com.example.appmpazpro3_grupo6.ui.screens.SeleccionDificultadScreen
import com.example.appmpazpro3_grupo6.viewmodel.CuentaViewModel
import com.example.appmpazpro3_grupo6.viewmodel.EstadoViewModel
import com.example.appmpazpro3_grupo6.viewmodel.ProgresoViewModel
import kotlinx.coroutines.flow.first

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    val cuentaViewModel: CuentaViewModel = viewModel()
    val progresoViewModel: ProgresoViewModel = viewModel()
    val estadoViewModel: EstadoViewModel = viewModel()

    val cuentaActual by cuentaViewModel.cuentaActual.collectAsState()
    val errorCuenta by cuentaViewModel.mensajeError.collectAsState()
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
                onIngresarClick = { _, _ ->
                    navController.navigate(Routes.PERFIL) {
                        popUpTo(Routes.LOGIN) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                cuentaViewModel = cuentaViewModel
            )
        }

        composable(Routes.REGISTRO) {
            RegistroScreen(
                onVolverClick = {
                    navController.popBackStack()
                },
                onRegistroExitoso = { _, _ ->
                    navController.navigate(Routes.PERFIL) {
                        popUpTo(Routes.LOGIN) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                cuentaViewModel = cuentaViewModel
            )
        }

        composable(Routes.PERFIL) {

            LaunchedEffect(Unit) {
                cuentaViewModel.procesando.first { !it }

                if (cuentaViewModel.cuentaActual.value != null) {
                    cuentaViewModel.cargarPerfil()
                }
            }

            val cuenta = cuentaActual

            if (cuenta == null) {

                LaunchedEffect(Unit) {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.BIENVENIDA) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                }

            } else {

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
                    nombre = cuenta.nombre,
                    curso = cuenta.curso,
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
                    mensajeErrorCuenta = errorCuenta,
                    onContinuarClick = {
                        navController.navigate(Routes.ASIGNATURAS)
                    },
                    onModoEstudioClick = {
                        navController.navigate(Routes.MODO_ESTUDIO)
                    },
                    onVolverClick = {
                        navController.popBackStack()
                    },
                    onCerrarSesionClick = {
                        cuentaViewModel.cerrarSesion()
                    }
                )
            }
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
                            Routes.elegirDificultad(id)
                        )
                    }
                }
            )
        }

        composable(
            route = "${Routes.ELEGIR_DIFICULTAD}/{asignaturaId}"
        ) { backStackEntry ->

            val asignaturaId = backStackEntry.arguments
                ?.getString("asignaturaId")
                .orEmpty()

            SeleccionDificultadScreen(
                asignaturaId = asignaturaId,
                progreso = progresoActual,
                onSeleccionar = { dificultad ->
                    navController.navigate(
                        Routes.detalleAsignatura(
                            asignaturaId,
                            dificultad.id
                        )
                    )
                },
                onVolverClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = "${Routes.DETALLE_ASIGNATURA}/{asignaturaId}/{nivelId}"
        ) { backStackEntry ->

            val asignaturaId = backStackEntry.arguments
                ?.getString("asignaturaId")
                .orEmpty()

            val nivelId = backStackEntry.arguments
                ?.getString("nivelId")
                .orEmpty()

            val clave = "${asignaturaId}_${nivelId}"

            val sesionesGuardando by progresoViewModel
                .sesionesGuardando
                .collectAsState()

            val sesion = progresoActual
                .sesionesPractica
                ?.get(clave)

            AsignaturaDetalleScreen(
                asignaturaId = asignaturaId,
                nivelId = nivelId,
                sesionPractica = sesion,
                guardandoPractica = clave in sesionesGuardando,
                porcentajeGuardado =
                    progresoActual.porcentaje(clave),
                errorGuardado = errorProgreso,
                modoEstudioActivo = modoEstudioActivo == true,
                onCambiarPractica = {
                        anterior,
                        siguiente,
                        esCorrecta ->

                    progresoViewModel.guardarSesion(
                        asignaturaId = clave,
                        anterior = anterior,
                        siguiente = siguiente,
                        esCorrecta = esCorrecta
                    )
                },
                onVolverClick = {
                    navController.popBackStack()
                },
                onVolverAsignaturasClick = {
                    navController.popBackStack(
                        Routes.ASIGNATURAS,
                        inclusive = false
                    )
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