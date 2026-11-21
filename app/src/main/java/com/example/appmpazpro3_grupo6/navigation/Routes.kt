package com.example.appmpazpro3_grupo6.navigation

object Routes {
    const val BIENVENIDA = "bienvenida"
    const val LOGIN = "login"
    const val REGISTRO = "registro"
    const val PERFIL = "perfil"
    const val ASIGNATURAS = "asignaturas"
    const val MODO_ESTUDIO = "modo_estudio"
    const val ELEGIR_DIFICULTAD = "elegir_dificultad"
    const val DETALLE_ASIGNATURA = "detalle_asignatura"

    fun elegirDificultad(asignaturaId: String) = "$ELEGIR_DIFICULTAD/$asignaturaId"

    fun detalleAsignatura(asignaturaId: String, nivelId: String) =
        "$DETALLE_ASIGNATURA/$asignaturaId/$nivelId"
}