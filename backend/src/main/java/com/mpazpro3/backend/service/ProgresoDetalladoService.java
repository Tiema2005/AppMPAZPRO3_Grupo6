package com.mpazpro3.backend.service;

import com.mpazpro3.backend.entity.Estudiante;
import com.mpazpro3.backend.entity.ProgresoDetallado;
import com.mpazpro3.backend.repository.EstudianteRepository;
import com.mpazpro3.backend.repository.ProgresoDetalladoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ProgresoDetalladoService {

    private final ProgresoDetalladoRepository progresos;
    private final EstudianteRepository estudiantes;

    public ProgresoDetalladoService(
            ProgresoDetalladoRepository progresos,
            EstudianteRepository estudiantes
    ) {
        this.progresos = progresos;
        this.estudiantes = estudiantes;
    }

    @Transactional(readOnly = true)
    public String obtener(Long estudianteId) {

        return progresos.findByEstudiante_Id(estudianteId)
                .map(ProgresoDetallado::getDatosJson)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "El estudiante todavía no tiene progreso detallado"
                ));
    }

    @Transactional
    public String guardar(
            Long estudianteId,
            String datosJson
    ) {

        Estudiante estudiante = estudiantes.findById(estudianteId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "La sesión del estudiante no es válida"
                ));

        ProgresoDetallado progreso = progresos
                .findByEstudiante_Id(estudianteId)
                .orElseGet(() -> new ProgresoDetallado(
                        estudiante,
                        datosJson
                ));

        progreso.setDatosJson(datosJson);

        return progresos.save(progreso).getDatosJson();
    }
}