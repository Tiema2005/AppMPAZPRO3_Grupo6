package com.mpazpro3.backend.controller;

import com.mpazpro3.backend.dto.ActualizarMetaRequest;
import com.mpazpro3.backend.dto.CrearMetaRequest;
import com.mpazpro3.backend.dto.MetaEstudioResponse;
import com.mpazpro3.backend.service.MetaEstudioService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/metas")
public class MetaEstudioController {

    private final MetaEstudioService servicio;

    public MetaEstudioController(MetaEstudioService servicio) {
        this.servicio = servicio;
    }

    @PostMapping
    public ResponseEntity<MetaEstudioResponse> crear(
            @Valid @RequestBody CrearMetaRequest datos,
            HttpServletRequest request
    ) {

        Long estudianteId = obtenerEstudianteId(request);

        MetaEstudioResponse respuesta =
                servicio.crear(estudianteId, datos);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(respuesta);
    }

    @GetMapping
    public List<MetaEstudioResponse> listar(
            HttpServletRequest request
    ) {

        Long estudianteId = obtenerEstudianteId(request);

        return servicio.listar(estudianteId);
    }

    @GetMapping("/{id}")
    public MetaEstudioResponse obtener(
            @PathVariable Long id,
            HttpServletRequest request
    ) {

        Long estudianteId = obtenerEstudianteId(request);

        return servicio.obtener(id, estudianteId);
    }

    @PutMapping("/{id}")
    public MetaEstudioResponse actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarMetaRequest datos,
            HttpServletRequest request
    ) {

        Long estudianteId = obtenerEstudianteId(request);

        return servicio.actualizar(id, estudianteId, datos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id,
            HttpServletRequest request
    ) {

        Long estudianteId = obtenerEstudianteId(request);

        servicio.eliminar(id, estudianteId);

        return ResponseEntity.noContent().build();
    }

    private Long obtenerEstudianteId(HttpServletRequest request) {

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