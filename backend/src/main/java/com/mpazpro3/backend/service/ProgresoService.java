package com.mpazpro3.backend.service;

import com.mpazpro3.backend.dto.ActualizarProgresoRequest;
import com.mpazpro3.backend.dto.GuardarProgresoRequest;
import com.mpazpro3.backend.dto.ProgresoResponse;
import com.mpazpro3.backend.entity.Estudiante;
import com.mpazpro3.backend.entity.Progreso;
import com.mpazpro3.backend.repository.EstudianteRepository;
import com.mpazpro3.backend.repository.ProgresoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Locale;

@Service
public class ProgresoService {

    private final ProgresoRepository progresos;
    private final EstudianteRepository estudiantes;

    public ProgresoService(
            ProgresoRepository progresos,
            EstudianteRepository estudiantes
    ) {
        this.progresos = progresos;
        this.estudiantes = estudiantes;
    }

    @Transactional
    public ProgresoResponse guardar(
            Long estudianteId,
            GuardarProgresoRequest datos
    ) {

        validarResultados(
                datos.ejerciciosRespondidos(),
                datos.respuestasCorrectas()
        );

        String asignatura = normalizarAsignatura(
                datos.asignatura()
        );

        Estudiante estudiante = estudiantes.findById(estudianteId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Sesion de estudiante no valida"
                ));

        Progreso progreso = progresos
                .findByEstudianteIdAndAsignatura(
                        estudianteId,
                        asignatura
                )
                .orElseGet(() -> new Progreso(
                        asignatura,
                        0,
                        0,
                        estudiante
                ));

        progreso.setEjerciciosRespondidos(
                datos.ejerciciosRespondidos()
        );

        progreso.setRespuestasCorrectas(
                datos.respuestasCorrectas()
        );

        Progreso guardado = progresos.save(progreso);

        return convertirRespuesta(guardado);
    }

    @Transactional(readOnly = true)
    public List<ProgresoResponse> listar(Long estudianteId) {

        return progresos
                .findByEstudianteIdOrderByAsignaturaAsc(estudianteId)
                .stream()
                .map(this::convertirRespuesta)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProgresoResponse obtenerPorAsignatura(
            Long estudianteId,
            String asignatura
    ) {

        String nombre = normalizarAsignatura(asignatura);

        Progreso progreso = progresos
                .findByEstudianteIdAndAsignatura(
                        estudianteId,
                        nombre
                )
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No existe progreso para esta asignatura"
                ));

        return convertirRespuesta(progreso);
    }

    @Transactional(readOnly = true)
    public ProgresoResponse obtener(
            Long id,
            Long estudianteId
    ) {

        Progreso progreso = buscarProgresoPropio(
                id,
                estudianteId
        );

        return convertirRespuesta(progreso);
    }

    @Transactional
    public ProgresoResponse actualizar(
            Long id,
            Long estudianteId,
            ActualizarProgresoRequest datos
    ) {

        validarResultados(
                datos.ejerciciosRespondidos(),
                datos.respuestasCorrectas()
        );

        Progreso progreso = buscarProgresoPropio(
                id,
                estudianteId
        );

        progreso.setEjerciciosRespondidos(
                datos.ejerciciosRespondidos()
        );

        progreso.setRespuestasCorrectas(
                datos.respuestasCorrectas()
        );

        return convertirRespuesta(progresos.save(progreso));
    }

    private Progreso buscarProgresoPropio(
            Long id,
            Long estudianteId
    ) {

        return progresos
                .findByIdAndEstudianteId(id, estudianteId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Progreso no encontrado"
                ));
    }

    private void validarResultados(
            Integer respondidos,
            Integer correctos
    ) {

        if (respondidos == null || correctos == null ||
                respondidos < 0 || correctos < 0 ||
                correctos > respondidos) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Los resultados ingresados no son validos"
            );
        }
    }

    private String normalizarAsignatura(String asignatura) {

        String normalizada = asignatura
                .trim()
                .toLowerCase(Locale.ROOT);

        if (normalizada.isBlank() ||
                normalizada.length() > 50) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Asignatura no valida"
            );
        }

        return normalizada;
    }

    private ProgresoResponse convertirRespuesta(
            Progreso progreso
    ) {

        int respondidos = progreso.getEjerciciosRespondidos();
        int correctos = progreso.getRespuestasCorrectas();

        double porcentaje = respondidos == 0
                ? 0.0
                : Math.round(correctos * 10000.0 / respondidos)
                  / 100.0;

        return new ProgresoResponse(
                progreso.getId(),
                progreso.getAsignatura(),
                respondidos,
                correctos,
                porcentaje
        );
    }
}