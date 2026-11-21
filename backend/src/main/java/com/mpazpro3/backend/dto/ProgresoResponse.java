package com.mpazpro3.backend.dto;

public record ProgresoResponse(
        Long id,
        String asignatura,
        int ejerciciosRespondidos,
        int respuestasCorrectas,
        double porcentajeAcierto
) {
}