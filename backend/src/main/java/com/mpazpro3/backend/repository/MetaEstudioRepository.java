package com.mpazpro3.backend.repository;

import com.mpazpro3.backend.entity.MetaEstudio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MetaEstudioRepository
        extends JpaRepository<MetaEstudio, Long> {

    List<MetaEstudio> findByEstudianteId(Long estudianteId);

    List<MetaEstudio> findByEstudiante_IdOrderByIdDesc(
            Long estudianteId
    );

    Optional<MetaEstudio> findByIdAndEstudiante_Id(
            Long id,
            Long estudianteId
    );
}