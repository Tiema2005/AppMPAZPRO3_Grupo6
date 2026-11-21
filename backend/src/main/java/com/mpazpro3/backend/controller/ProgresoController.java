package com.mpazpro3.backend.controller;

import com.mpazpro3.backend.dto.ActualizarProgresoRequest;
import com.mpazpro3.backend.dto.GuardarProgresoRequest;
import com.mpazpro3.backend.dto.ProgresoResponse;
import com.mpazpro3.backend.service.ProgresoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/api/progreso")
public class ProgresoController {

    private final ProgresoService servicio;

    public ProgresoController(ProgresoService servicio) {
        this.servicio = servicio;
    }

    @PostMapping
    public ProgresoResponse guardar(
            @Valid @RequestBody GuardarProgresoRequest datos,
            HttpServletRequest request
    ) {

        Long estudianteId = obtenerEstudianteId(request);

        return servicio.guardar(estudianteId, datos);
    }

    @GetMapping
    public List<ProgresoResponse> listar(
            HttpServletRequest request
    ) {

        Long estudianteId = obtenerEstudianteId(request);

        return servicio.listar(estudianteId);
    }

    @GetMapping("/asignatura/{asignatura}")
    public ProgresoResponse obtenerPorAsignatura(
            @PathVariable String asignatura,
            HttpServletRequest request
    ) {

        Long estudianteId = obtenerEstudianteId(request);

        return servicio.obtenerPorAsignatura(
                estudianteId,
                asignatura
        );
    }

    @GetMapping("/{id}")
    public ProgresoResponse obtener(
            @PathVariable Long id,
            HttpServletRequest request
    ) {

        Long estudianteId = obtenerEstudianteId(request);

        return servicio.obtener(id, estudianteId);
    }

    @PutMapping("/{id}")
    public ProgresoResponse actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarProgresoRequest datos,
            HttpServletRequest request
    ) {

        Long estudianteId = obtenerEstudianteId(request);

        return servicio.actualizar(
                id,
                estudianteId,
                datos
        );
    }

    private Long obtenerEstudianteId(
            HttpServletRequest request
    ) {

        HttpSession sesion = request.getSession(false);

        if (sesion == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Debes iniciar sesion"
            );
        }

        Object id = sesion.getAttribute("estudianteId");

        if (!(id instanceof Long)) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Sesion no valida"
            );
        }

        return (Long) id;
    }
}