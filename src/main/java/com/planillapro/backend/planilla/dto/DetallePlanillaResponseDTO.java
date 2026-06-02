package com.planillapro.backend.planilla.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DetallePlanillaResponseDTO {

    private Long id;

    private Long periodoPlanillaId;
    private String periodoNombre;

    private Long trabajadorId;
    private String trabajadorNombres;
    private String trabajadorApellidos;
    private String trabajadorDocumento;

    private Long conceptoPlanillaId;
    private String conceptoCodigo;
    private String conceptoNombre;

    private String tipo;
    private BigDecimal monto;

    private String observacion;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}