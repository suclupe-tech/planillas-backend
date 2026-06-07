package com.planillapro.backend.dashboard.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class DashboardPlanillaDTO {

    private Long empresaId;
    private String empresaRazonSocial;

    private Integer totalPlanillas;
    private Integer planillasAbiertas;
    private Integer planillasCerradas;

    private BigDecimal totalIngresos;
    private BigDecimal totalDescuentos;
    private BigDecimal totalNetoPagar;

    private Long ultimaPlanillaId;
    private String ultimaPlanillaNombre;
    private String ultimaPlanillaTipo;
    private String ultimaPlanillaEstado;
}