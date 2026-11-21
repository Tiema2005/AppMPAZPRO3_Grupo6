package com.mpazpro3.backend.service;

import com.mpazpro3.backend.dto.EstudianteResponse;
import com.mpazpro3.backend.dto.LoginEstudianteRequest;
import com.mpazpro3.backend.dto.RegistroEstudianteRequest;
import com.mpazpro3.backend.entity.Estudiante;
import com.mpazpro3.backend.repository.EstudianteRepository;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;

@Service
public class EstudianteService {

    private final EstudianteRepository repositorio;

    private final BCryptPasswordEncoder encoder =
            new BCryptPasswordEncoder();

    public EstudianteService(EstudianteRepository repositorio) {
        this.repositorio = repositorio;
    }


    @Transactional
    public EstudianteResponse registrar(
            RegistroEstudianteRequest datos
    ) {

        String correo = datos.correo()
                .trim()
                .toLowerCase(Locale.ROOT);

        if (repositorio.findByCorreoIgnoreCase(correo).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El correo ya esta registrado"
            );
        }

        Estudiante estudiante = new Estudiante(
                datos.nombre().trim(),
                correo,
                "3° Básico"
        );

        estudiante.setPasswordHash(
                encoder.encode(datos.contrasena())
        );

        try {
            Estudiante guardado =
                    repositorio.saveAndFlush(estudiante);

            return convertirRespuesta(guardado);

        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No se pudo registrar al estudiante"
            );
        }
    }

    @Transactional(readOnly = true)
    public EstudianteResponse iniciarSesion(
            LoginEstudianteRequest datos
    ) {

        String correo = datos.correo()
                .trim()
                .toLowerCase(Locale.ROOT);

        Estudiante estudiante = repositorio
                .findByCorreoIgnoreCase(correo)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Correo o contrasena incorrectos"
                ));

        String hash = estudiante.getPasswordHash();

        if (hash == null ||
                !encoder.matches(datos.contrasena(), hash)) {

            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Correo o contrasena incorrectos"
            );
        }

        return convertirRespuesta(estudiante);
    }


    @Transactional(readOnly = true)
    public EstudianteResponse obtenerPerfil(Long id) {

        Estudiante estudiante = repositorio.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Estudiante no encontrado"
                ));

        return convertirRespuesta(estudiante);
    }

    private EstudianteResponse convertirRespuesta(
            Estudiante estudiante
    ) {
        return new EstudianteResponse(
                estudiante.getId(),
                estudiante.getNombre(),
                estudiante.getCorreo(),
                estudiante.getCurso()
        );
    }
}