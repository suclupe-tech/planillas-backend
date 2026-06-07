package com.planillapro.backend.dashboard.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DashboardUltimaPlanillaDTO {

    private Long id;
    private String nombre;
    private String tipo;
    private String estado;

    private LocalDate fechaInicio;
    private LocalDate fechaFin;
}