package com.planillapro.backend.planilla;

import com.planillapro.backend.concepto.ConceptoPlanilla;
import com.planillapro.backend.periodo.PeriodoPlanilla;
import com.planillapro.backend.trabajador.Trabajador;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(
        name = "detalle_planilla",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_detalle_planilla_periodo_trabajador_concepto",
                        columnNames = {"periodo_planilla_id", "trabajador_id", "concepto_planilla_id"}
                )
        }
)
public class DetallePlanilla {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "periodo_planilla_id", nullable = false)
    private PeriodoPlanilla periodoPlanilla;

    @ManyToOne
    @JoinColumn(name = "trabajador_id", nullable = false)
    private Trabajador trabajador;

    @ManyToOne
    @JoinColumn(name = "concepto_planilla_id", nullable = false)
    private ConceptoPlanilla conceptoPlanilla;

    @Column(name = "tipo", nullable = false, length = 30)
    private String tipo;

    @Column(name = "monto", nullable = false, precision = 12, scale = 2)
    private BigDecimal monto = BigDecimal.ZERO;

    @Column(name = "observacion", length = 250)
    private String observacion;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();

        if (this.monto == null) {
            this.monto = BigDecimal.ZERO;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}