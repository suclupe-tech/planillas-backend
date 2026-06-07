package com.planillapro.backend.dashboard.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class DashboardPlanillaMensualDTO {

    private Integer anio;
    private Integer mes;
    private String nombreMes;

    private BigDecimal totalIngresos;
    private BigDecimal totalDescuentos;
    private BigDecimal totalNetoPagar;
}