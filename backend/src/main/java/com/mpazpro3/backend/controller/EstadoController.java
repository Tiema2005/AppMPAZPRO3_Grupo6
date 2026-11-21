package com.mpazpro3.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class EstadoController {

    @GetMapping("/estado")
    public Map<String, String> obtenerEstado() {
        return Map.of(
                "aplicacion", "MPAZ PRO 3",
                "estado", "activo",
                "mensaje", "Backend funcionando correctamente"
        );
    }
}
