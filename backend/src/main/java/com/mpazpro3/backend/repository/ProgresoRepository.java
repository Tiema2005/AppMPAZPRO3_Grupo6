package com.mpazpro3.backend.repository;

import com.mpazpro3.backend.entity.Progreso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProgresoRepository
        extends JpaRepository<Progreso, Long> {

    List<Progreso> findByEstudianteId(Long estudianteId);

    Optional<Progreso> findByEstudianteIdAndAsignatura(
            Long estudianteId,
            String asignatura
    );
}