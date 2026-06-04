package com.planillapro.backend.planilla;

import com.planillapro.backend.empresa.Empresa;
import com.planillapro.backend.periodo.PeriodoPlanilla;
import com.planillapro.backend.trabajador.Trabajador;
import com.planillapro.backend.usuario.Usuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "auditoria_planilla")
public class AuditoriaPlanilla {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @ManyToOne
    @JoinColumn(name = "periodo_planilla_id")
    private PeriodoPlanilla periodoPlanilla;

    @ManyToOne
    @JoinColumn(name = "trabajador_id")
    private Trabajador trabajador;

    @ManyToOne
    @JoinColumn(name = "detalle_planilla_id")
    private DetallePlanilla detallePlanilla;

    @Column(name = "accion", nullable = false, length = 60)
    private String accion;

    @Column(name = "descripcion", length = 500)
    private String descripcion;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @PrePersist
    public void prePersist() {
        this.fechaHora = LocalDateTime.now();
    }
}