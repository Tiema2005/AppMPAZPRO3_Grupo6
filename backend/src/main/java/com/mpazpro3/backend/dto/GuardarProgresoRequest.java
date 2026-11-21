package com.mpazpro3.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record GuardarProgresoRequest(

        @NotBlank(message = "La asignatura es obligatoria")
        @Size(max = 50, message = "Maximo 50 caracteres")
        String asignatura,

        @NotNull(message = "Indica los ejercicios respondidos")
        @Min(value = 0, message = "No puede ser negativo")
        Integer ejerciciosRespondidos,

        @NotNull(message = "Indica las respuestas correctas")
        @Min(value = 0, message = "No puede ser negativo")
        Integer respuestasCorrectas

) {
}