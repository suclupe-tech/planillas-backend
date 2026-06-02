package com.planillapro.backend.planilla.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DetallePlanillaRequestDTO {

    @NotNull(message = "El periodo de planilla es obligatorio")
    private Long periodoPlanillaId;

    @NotNull(message = "El trabajador es obligatorio")
    private Long trabajadorId;

    @NotNull(message = "El concepto de planilla es obligatorio")
    private Long conceptoPlanillaId;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.00", message = "El monto no puede ser negativo")
    private BigDecimal monto;

    @Size(max = 250, message = "La observación no debe superar 250 caracteres")
    private String observacion;
}