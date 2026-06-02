package com.planillapro.backend.planilla.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class ResumenPlanillaPeriodoDTO {

    private Long periodoPlanillaId;
    private String periodoNombre;
    private String tipo;
    private String estado;

    private BigDecimal totalIngresos;
    private BigDecimal totalDescuentos;
    private BigDecimal totalNetoPagar;

    private Integer cantidadTrabajadores;

    private List<ResumenPlanillaTrabajadorDTO> trabajadores;
}