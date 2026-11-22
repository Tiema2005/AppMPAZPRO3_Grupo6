package com.mpazpro3.backend.controller;

import com.mpazpro3.backend.dto.GuardarProgresoDetalladoRequest;
import com.mpazpro3.backend.service.ProgresoDetalladoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/api/progreso/detalle")
public class ProgresoDetalladoController {

    private final ProgresoDetalladoService servicio;

    public ProgresoDetalladoController(
            ProgresoDetalladoService servicio
    ) {
        this.servicio = servicio;
    }

    @GetMapping
    public ResponseEntity<Map<String, String>> obtener(
            HttpServletRequest request
    ) {

        Long estudianteId = obtenerEstudianteId(request);

        String datosJson = servicio.obtener(estudianteId);

        return ResponseEntity.ok(
                Map.of("datosJson", datosJson)
        );
    }

    @PutMapping
    public ResponseEntity<Map<String, String>> guardar(
            @Valid @RequestBody GuardarProgresoDetalladoRequest datos,
            HttpServletRequest request
    ) {

        Long estudianteId = obtenerEstudianteId(request);

        String guardado = servicio.guardar(
                estudianteId,
                datos.datosJson()
        );

        return ResponseEntity.ok(
                Map.of("datosJson", guardado)
        );
    }

    private Long obtenerEstudianteId(
            HttpServletRequest request
    ) {

        HttpSession sesion = request.getSession(false);

        if (sesion == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Debes iniciar sesión"
            );
        }

        Object id = sesion.getAttribute("estudianteId");

        if (!(id instanceof Long)) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Sesión no válida"
            );
        }

        return (Long) id;
    }
}