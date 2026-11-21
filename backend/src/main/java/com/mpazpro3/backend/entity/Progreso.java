package com.mpazpro3.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(
        name = "progreso",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_progreso_estudiante_asignatura",
                columnNames = {"estudiante_id", "asignatura"}
        )
)
public class Progreso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String asignatura;

    @Column(nullable = false)
    private int ejerciciosRespondidos = 0;

    @Column(nullable = false)
    private int respuestasCorrectas = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estudiante_id", nullable = false)
    private Estudiante estudiante;

    public Progreso() {
    }

    public Progreso(
            String asignatura,
            int ejerciciosRespondidos,
            int respuestasCorrectas,
            Estudiante estudiante
    ) {
        this.asignatura = asignatura;
        this.ejerciciosRespondidos = ejerciciosRespondidos;
        this.respuestasCorrectas = respuestasCorrectas;
        this.estudiante = estudiante;
    }

    public Long getId() {
        return id;
    }

    public String getAsignatura() {
        return asignatura;
    }

    public void setAsignatura(String asignatura) {
        this.asignatura = asignatura;
    }

    public int getEjerciciosRespondidos() {
        return ejerciciosRespondidos;
    }

    public void setEjerciciosRespondidos(int ejerciciosRespondidos) {
        this.ejerciciosRespondidos = ejerciciosRespondidos;
    }

    public int getRespuestasCorrectas() {
        return respuestasCorrectas;
    }

    public void setRespuestasCorrectas(int respuestasCorrectas) {
        this.respuestasCorrectas = respuestasCorrectas;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }
}