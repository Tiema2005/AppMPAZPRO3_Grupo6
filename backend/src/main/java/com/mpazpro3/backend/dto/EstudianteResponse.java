package com.mpazpro3.backend.dto;

public record EstudianteResponse(
        Long id,
        String nombre,
        String correo,
        String curso
) {
}