package com.mpazpro3.backend.repository;

import com.mpazpro3.backend.entity.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EstudianteRepository
        extends JpaRepository<Estudiante, Long> {

    Optional<Estudiante> findByCorreoIgnoreCase(String correo);
}