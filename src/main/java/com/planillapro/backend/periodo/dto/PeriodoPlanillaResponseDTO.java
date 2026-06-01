package com.planillapro.backend.periodo.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class PeriodoPlanillaResponseDTO {

    private Long id;

    private Long empresaId;
    private String empresaRazonSocial;

    private String nombre;
    private String tipo;

    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    private String estado;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}