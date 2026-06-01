package com.planillapro.backend.concepto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "concepto_planilla")
public class ConceptoPlanilla {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo", nullable = false, unique = true, length = 50)
    private String codigo;

    @Column(name = "nombre", nullable = false, length = 120)
    private String nombre;

    @Column(name = "descripcion", length = 250)
    private String descripcion;

    @Column(name = "tipo", nullable = false, length = 30)
    private String tipo;

    @Column(name = "formula", length = 250)
    private String formula;

    @Column(name = "es_remunerativo", nullable = false)
    private Boolean esRemunerativo = false;

    @Column(name = "afecta_afp_onp", nullable = false)
    private Boolean afectaAfpOnp = false;

    @Column(name = "afecta_essalud", nullable = false)
    private Boolean afectaEssalud = false;

    @Column(name = "estado", nullable = false, length = 20)
    private String estado = "ACTIVO";

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();

        if (this.estado == null || this.estado.isBlank()) {
            this.estado = "ACTIVO";
        }

        if (this.esRemunerativo == null) {
            this.esRemunerativo = false;
        }

        if (this.afectaAfpOnp == null) {
            this.afectaAfpOnp = false;
        }

        if (this.afectaEssalud == null) {
            this.afectaEssalud = false;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}