package com.mpazpro3.backend.dto;

public record MetaEstudioResponse(
        Long id,
        String titulo,
        String descripcion,
        boolean completada
) {
}