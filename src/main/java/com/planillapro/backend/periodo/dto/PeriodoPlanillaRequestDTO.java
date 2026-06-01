package com.planillapro.backend.periodo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PeriodoPlanillaRequestDTO {

    @NotNull(message = "La empresa es obligatoria")
    private Long empresaId;

    @NotBlank(message = "El nombre del periodo es obligatorio")
    @Size(max = 120, message = "El nombre no debe superar 120 caracteres")
    private String nombre;

    @NotBlank(message = "El tipo de periodo es obligatorio")
    @Size(max = 30, message = "El tipo no debe superar 30 caracteres")
    private String tipo;

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDate fechaInicio;

    @NotNull(message = "La fecha de fin es obligatoria")
    private LocalDate fechaFin;
}