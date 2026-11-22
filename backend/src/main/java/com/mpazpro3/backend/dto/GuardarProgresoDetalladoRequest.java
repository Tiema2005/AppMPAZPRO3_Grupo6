package com.mpazpro3.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record GuardarProgresoDetalladoRequest(

        @NotBlank(message = "Los datos del progreso son obligatorios")
        @Size(max = 250000, message = "Los datos superan el tamaño permitido")
        String datosJson

) {
}