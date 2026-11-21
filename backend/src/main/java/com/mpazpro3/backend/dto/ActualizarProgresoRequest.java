package com.mpazpro3.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ActualizarProgresoRequest(

        @NotNull(message = "Indica los ejercicios respondidos")
        @Min(value = 0, message = "No puede ser negativo")
        Integer ejerciciosRespondidos,

        @NotNull(message = "Indica las respuestas correctas")
        @Min(value = 0, message = "No puede ser negativo")
        Integer respuestasCorrectas

) {
}