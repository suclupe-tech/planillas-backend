package com.planillapro.backend.planilla.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class BoletaPagoDetalleDTO {

    private Long detalleId;

    private Long conceptoPlanillaId;
    private String conceptoCodigo;
    private String conceptoNombre;

    private String tipo;
    private BigDecimal monto;

    private String observacion;
}