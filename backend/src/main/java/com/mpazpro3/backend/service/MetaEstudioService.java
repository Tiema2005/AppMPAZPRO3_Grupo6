package com.mpazpro3.backend.service;

import com.mpazpro3.backend.dto.ActualizarMetaRequest;
import com.mpazpro3.backend.dto.CrearMetaRequest;
import com.mpazpro3.backend.dto.MetaEstudioResponse;

import com.mpazpro3.backend.entity.Estudiante;
import com.mpazpro3.backend.entity.MetaEstudio;

import com.mpazpro3.backend.repository.EstudianteRepository;
import com.mpazpro3.backend.repository.MetaEstudioRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class MetaEstudioService {

    private final MetaEstudioRepository metas;
    private final EstudianteRepository estudiantes;

    public MetaEstudioService(
            MetaEstudioRepository metas,
            EstudianteRepository estudiantes
    ) {
        this.metas = metas;
        this.estudiantes = estudiantes;
    }

    @Transactional
    public MetaEstudioResponse crear(
            Long estudianteId,
            CrearMetaRequest datos
    ) {

        Estudiante estudiante = estudiantes.findById(estudianteId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Sesion de estudiante no valida"
                ));

        MetaEstudio meta = new MetaEstudio(
                datos.titulo().trim(),
                datos.descripcion(),
                estudiante
        );

        MetaEstudio guardada = metas.save(meta);

        return convertirRespuesta(guardada);
    }

    @Transactional(readOnly = true)
    public List<MetaEstudioResponse> listar(Long estudianteId) {

        return metas.findByEstudiante_IdOrderByIdDesc(estudianteId)
                .stream()
                .map(this::convertirRespuesta)
                .toList();
    }

    @Transactional(readOnly = true)
    public MetaEstudioResponse obtener(
            Long metaId,
            Long estudianteId
    ) {

        MetaEstudio meta = buscarMetaPropia(metaId, estudianteId);

        return convertirRespuesta(meta);
    }

    @Transactional
    public MetaEstudioResponse actualizar(
            Long metaId,
            Long estudianteId,
            ActualizarMetaRequest datos
    ) {

        MetaEstudio meta = buscarMetaPropia(metaId, estudianteId);

        meta.setTitulo(datos.titulo().trim());
        meta.setDescripcion(datos.descripcion());
        meta.setCompletada(datos.completada());

        return convertirRespuesta(metas.save(meta));
    }

    @Transactional
    public void eliminar(Long metaId, Long estudianteId) {

        MetaEstudio meta = buscarMetaPropia(metaId, estudianteId);

        metas.delete(meta);
    }

    private MetaEstudio buscarMetaPropia(
            Long metaId,
            Long estudianteId
    ) {

        return metas.findByIdAndEstudiante_Id(metaId, estudianteId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Meta no encontrada"
                ));
    }

    private MetaEstudioResponse convertirRespuesta(MetaEstudio meta) {

        return new MetaEstudioResponse(
                meta.getId(),
                meta.getTitulo(),
                meta.getDescripcion(),
                meta.isCompletada()
        );
    }
}