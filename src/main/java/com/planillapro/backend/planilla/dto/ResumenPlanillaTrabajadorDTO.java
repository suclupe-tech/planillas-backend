package com.planillapro.backend.planilla.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ResumenPlanillaTrabajadorDTO {

    private Long periodoPlanillaId;
    private String periodoNombre;

    private Long trabajadorId;
    private String trabajadorNombres;
    private String trabajadorApellidos;
    private String trabajadorDocumento;

    private BigDecimal totalIngresos;
    private BigDecimal totalDescuentos;
    private BigDecimal netoPagar;
}