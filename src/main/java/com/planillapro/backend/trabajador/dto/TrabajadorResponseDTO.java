package com.planillapro.backend.trabajador.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class TrabajadorResponseDTO {

    private Long id;

    private Long empresaId;
    private String empresaRazonSocial;

    private String tipoDocumento;
    private String numeroDocumento;

    private String nombres;
    private String apellidos;

    private LocalDate fechaNacimiento;
    private String telefono;
    private String email;
    private String direccion;

    private LocalDate fechaIngreso;
    private LocalDate fechaCese;

    private String cargo;
    private String area;

    private String tipoContrato;
    private String regimenLaboral;

    private BigDecimal sueldoBase;

    private String estado;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}