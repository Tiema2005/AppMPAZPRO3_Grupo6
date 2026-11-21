package com.mpazpro3.backend.controller;

import com.mpazpro3.backend.dto.EstudianteResponse;
import com.mpazpro3.backend.dto.LoginEstudianteRequest;
import com.mpazpro3.backend.dto.RegistroEstudianteRequest;
import com.mpazpro3.backend.service.EstudianteService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteController {

    private final EstudianteService servicio;

    public EstudianteController(EstudianteService servicio) {
        this.servicio = servicio;
    }

    @PostMapping("/registro")
    public ResponseEntity<EstudianteResponse> registrar(
            @Valid @RequestBody RegistroEstudianteRequest datos
    ) {

        EstudianteResponse respuesta = servicio.registrar(datos);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(respuesta);
    }


    @PostMapping("/login")
    public ResponseEntity<EstudianteResponse> login(
            @Valid @RequestBody LoginEstudianteRequest datos,
            HttpServletRequest request
    ) {

        EstudianteResponse estudiante = servicio.iniciarSesion(datos);

        HttpSession sesion = request.getSession(true);

        request.changeSessionId();

        sesion.setAttribute("estudianteId", estudiante.id());

        return ResponseEntity.ok(estudiante);
    }


    @GetMapping("/perfil")
    public ResponseEntity<EstudianteResponse> perfil(
            HttpServletRequest request
    ) {

        HttpSession sesion = request.getSession(false);

        if (sesion == null ||
                !(sesion.getAttribute("estudianteId") instanceof Long)) {

            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Debes iniciar sesion"
            );
        }

        Long estudianteId =
                (Long) sesion.getAttribute("estudianteId");

        return ResponseEntity.ok(
                servicio.obtenerPerfil(estudianteId)
        );
    }


    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            HttpServletRequest request
    ) {

        HttpSession sesion = request.getSession(false);

        if (sesion != null) {
            sesion.invalidate();
        }

        return ResponseEntity.noContent().build();
    }
}