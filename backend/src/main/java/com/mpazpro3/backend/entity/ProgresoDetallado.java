package com.mpazpro3.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "progreso_detallado")
public class ProgresoDetallado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "estudiante_id",
            nullable = false,
            unique = true
    )
    private Estudiante estudiante;

    @Column(
            name = "datos_json",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String datosJson;

    public ProgresoDetallado() {
    }

    public ProgresoDetallado(
            Estudiante estudiante,
            String datosJson
    ) {
        this.estudiante = estudiante;
        this.datosJson = datosJson;
    }

    public Long getId() {
        return id;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public String getDatosJson() {
        return datosJson;
    }

    public void setDatosJson(String datosJson) {
        this.datosJson = datosJson;
    }
}