package com.mpazpro3.backend.repository;

import com.mpazpro3.backend.entity.ProgresoDetallado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProgresoDetalladoRepository
        extends JpaRepository<ProgresoDetallado, Long> {

    Optional<ProgresoDetallado> findByEstudiante_Id(
            Long estudianteId
    );
}