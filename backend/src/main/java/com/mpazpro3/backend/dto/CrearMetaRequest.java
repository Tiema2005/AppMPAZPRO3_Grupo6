package com.mpazpro3.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CrearMetaRequest(

        @NotBlank(message = "El titulo es obligatorio")
        @Size(max = 120, message = "Maximo 120 caracteres")
        String titulo,

        @Size(max = 500, message = "Maximo 500 caracteres")
        String descripcion

) {
}