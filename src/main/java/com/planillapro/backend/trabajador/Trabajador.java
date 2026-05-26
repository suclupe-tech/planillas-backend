package com.planillapro.backend.trabajador;

import com.planillapro.backend.empresa.Empresa;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(
        name = "trabajador",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_trabajador_empresa_documento",
                        columnNames = {"empresa_id", "tipo_documento", "numero_documento"}
                )
        }
)
public class Trabajador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @Column(name = "tipo_documento", nullable = false, length = 20)
    private String tipoDocumento;

    @Column(name = "numero_documento", nullable = false, length = 20)
    private String numeroDocumento;

    @Column(name = "nombres", nullable = false, length = 100)
    private String nombres;

    @Column(name = "apellidos", nullable = false, length = 100)
    private String apellidos;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "email", length = 120)
    private String email;

    @Column(name = "direccion", length = 250)
    private String direccion;

    @Column(name = "fecha_ingreso", nullable = false)
    private LocalDate fechaIngreso;

    @Column(name = "fecha_cese")
    private LocalDate fechaCese;

    @Column(name = "cargo", length = 100)
    private String cargo;

    @Column(name = "area", length = 100)
    private String area;

    @Column(name = "tipo_contrato", length = 50)
    private String tipoContrato;

    @Column(name = "regimen_laboral", length = 50)
    private String regimenLaboral;

    @Column(name = "sueldo_base", nullable = false, precision = 12, scale = 2)
    private BigDecimal sueldoBase = BigDecimal.ZERO;

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

        if (this.sueldoBase == null) {
            this.sueldoBase = BigDecimal.ZERO;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}