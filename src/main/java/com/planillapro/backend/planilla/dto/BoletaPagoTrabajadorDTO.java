package com.planillapro.backend.planilla.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class BoletaPagoTrabajadorDTO {
    
    private Long empresaId;
    private String empresaRazonSocial;
    private String empresaRuc;
    private String empresaDireccion;

    private Long periodoPlanillaId;
    private String periodoNombre;
    private String periodoTipo;
    private String periodoEstado;

    private Long trabajadorId;
    private String trabajadorNombres;
    private String trabajadorApellidos;
    private String trabajadorDocumento;
    private String cargo;
    private String area;

    private List<DetallePlanillaResponseDTO> ingresos;
    private List<DetallePlanillaResponseDTO> descuentos;

    private BigDecimal totalIngresos;
    private BigDecimal totalDescuentos;
    private BigDecimal netoPagar;
}