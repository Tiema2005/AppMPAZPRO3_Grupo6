package com.example.appmpazpro3_grupo6.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appmpazpro3_grupo6.ui.components.MPAZBackground
import com.example.appmpazpro3_grupo6.ui.screens.AsignaturaDetalleScreen
import com.example.appmpazpro3_grupo6.ui.screens.AsignaturasScreen
import com.example.appmpazpro3_grupo6.ui.screens.BienvenidaScreen
import com.example.appmpazpro3_grupo6.ui.screens.LoginScreen
import com.example.appmpazpro3_grupo6.ui.screens.MetasScreen
import com.example.appmpazpro3_grupo6.ui.screens.ModoEstudioScreen
import com.example.appmpazpro3_grupo6.ui.screens.PerfilScreen
import com.example.appmpazpro3_grupo6.ui.screens.RegistroScreen
import com.example.appmpazpro3_grupo6.ui.screens.SeleccionDificultadScreen
import com.example.appmpazpro3_grupo6.ui.theme.MPAZCoral
import com.example.appmpazpro3_grupo6.ui.theme.MPAZTextoSecundario
import com.example.appmpazpro3_grupo6.viewmodel.CuentaViewModel
import com.example.appmpazpro3_grupo6.viewmodel.EstadoViewModel
import com.example.appmpazpro3_grupo6.viewmodel.MetasViewModel
import com.example.appmpazpro3_grupo6.viewmodel.ProgresoViewModel
import kotlinx.coroutines.flow.first
import java.util.Locale

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    val cuentaViewModel: CuentaViewModel = viewModel()
    val progresoViewModel: ProgresoViewModel = viewModel()
    val estadoViewModel: EstadoViewModel = viewModel()
    val metasViewModel: MetasViewModel = viewModel()

    val cuentaActual by cuentaViewModel.cuentaActual.collectAsState()
    val errorCuenta by cuentaViewModel.mensajeError.collectAsState()

    val progresoActual by progresoViewModel.progreso.collectAsState()
    val errorProgreso by progresoViewModel.errorGuardado.collectAsState()
    val cuentaProgreso by progresoViewModel.cuentaActiva.collectAsState()
    val cargandoProgreso by progresoViewModel.cargandoProgreso.collectAsState()

    val modoEstudioActivo by estadoViewModel.activo.collectAsState()

    val metasActuales by metasViewModel.metas.collectAsState()
    val metasCargando by metasViewModel.cargando.collectAsState()
    val metasProcesando by metasViewModel.procesando.collectAsState()
    val errorMetas by metasViewModel.mensajeError.collectAsState()

    LaunchedEffect(cuentaActual?.correo) {
        progresoViewModel.activarCuenta(cuentaActual?.correo)
    }

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

                    metasViewModel.limpiar()

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

                    metasViewModel.limpiar()

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

                val correoNormalizado = cuenta.correo
                    .trim()
                    .lowercase(Locale.ROOT)

                val progresoListo =
                    cuentaProgreso == correoNormalizado &&
                            !cargandoProgreso

                if (!progresoListo) {

                    PantallaCargandoProgresoMPAZ()

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

                        mensajeErrorCuenta =
                            errorCuenta ?: errorProgreso,

                        onContinuarClick = {
                            navController.navigate(Routes.ASIGNATURAS)
                        },

                        onMisMetasClick = {
                            navController.navigate(Routes.METAS)
                        },

                        onModoEstudioClick = {
                            navController.navigate(Routes.MODO_ESTUDIO)
                        },

                        onVolverClick = {
                            navController.popBackStack()
                        },

                        onCerrarSesionClick = {
                            progresoViewModel.activarCuenta(null)
                            metasViewModel.limpiar()
                            cuentaViewModel.cerrarSesion()
                        }
                    )
                }
            }
        }

        composable(Routes.METAS) {

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

                LaunchedEffect(cuenta.correo) {
                    metasViewModel.cargarMetas()
                }

                MetasScreen(
                    metas = metasActuales,
                    cargando = metasCargando,
                    procesando = metasProcesando,
                    mensajeError = errorMetas,

                    onVolverClick = {
                        navController.popBackStack()
                    },

                    onRecargar = {
                        metasViewModel.cargarMetas()
                    },

                    onGuardar = {
                            id,
                            titulo,
                            descripcion,
                            completada,
                            onExito ->

                        metasViewModel.guardarMeta(
                            id = id,
                            titulo = titulo,
                            descripcion = descripcion,
                            completada = completada,
                            onExito = onExito
                        )
                    },

                    onEliminar = { id, onExito ->

                        metasViewModel.eliminarMeta(
                            id = id,
                            onExito = onExito
                        )
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

            } else if (
                cuentaProgreso != cuenta.correo.trim()
                    .lowercase(Locale.ROOT) ||
                cargandoProgreso
            ) {

                PantallaCargandoProgresoMPAZ()

            } else {

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

            } else if (
                cuentaProgreso != cuenta.correo.trim()
                    .lowercase(Locale.ROOT) ||
                cargandoProgreso
            ) {

                PantallaCargandoProgresoMPAZ()

            } else {

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

                    guardandoPractica =
                        clave in sesionesGuardando,

                    porcentajeGuardado =
                        progresoActual.porcentaje(clave),

                    errorGuardado = errorProgreso,

                    modoEstudioActivo =
                        modoEstudioActivo == true,

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

@Composable
private fun PantallaCargandoProgresoMPAZ() {

    MPAZBackground {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),

            horizontalAlignment = Alignment.CenterHorizontally,

            verticalArrangement = Arrangement.spacedBy(
                18.dp,
                Alignment.CenterVertically
            )
        ) {

            Text(
                text = "🦉",
                textAlign = TextAlign.Center
            )

            CircularProgressIndicator(
                color = MPAZCoral
            )

            Text(
                text = "Preparando tu progreso...",
                color = MPAZTextoSecundario,
                textAlign = TextAlign.Center
            )
        }
    }
}