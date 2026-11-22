package com.example.appmpazpro3_grupo6.repository

class SincronizadorEstadisticasMPAZ(
    private val remoto: ProgresoRemotoRepository =
        ProgresoRemotoRepository()
) {

    suspend fun sincronizar(
        progreso: ProgresoEstudiante
    ) {

        val registrados = remoto.listar()
            .associateBy { it.asignatura }

        val resumenes = ResumenProgresoMPAZ.calcularTodos(
            progreso
        )

        for (resumen in resumenes) {

            val registrado = registrados[
                resumen.asignaturaNivel
            ]

            val debeGuardar = if (registrado == null) {

                resumen.ejerciciosRespondidos > 0 ||
                        resumen.respuestasCorrectas > 0

            } else {

                registrado.ejerciciosRespondidos !=
                        resumen.ejerciciosRespondidos ||
                        registrado.respuestasCorrectas !=
                        resumen.respuestasCorrectas
            }

            if (debeGuardar) {

                remoto.guardar(
                    asignaturaNivel = resumen.asignaturaNivel,
                    ejerciciosRespondidos =
                        resumen.ejerciciosRespondidos,
                    respuestasCorrectas =
                        resumen.respuestasCorrectas
                )
            }
        }
    }
}