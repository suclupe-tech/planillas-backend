package com.planillapro.backend.trabajador.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class TrabajadorRequestDTO {

    @NotNull(message = "La empresa es obligatoria")
    private Long empresaId;

    @NotBlank(message = "El tipo de documento es obligatorio")
    @Size(max = 20, message = "El tipo de documento no debe superar 20 caracteres")
    private String tipoDocumento;

    @NotBlank(message = "El número de documento es obligatorio")
    @Size(max = 20, message = "El número de documento no debe superar 20 caracteres")
    private String numeroDocumento;

    @NotBlank(message = "Los nombres son obligatorios")
    @Size(max = 100, message = "Los nombres no deben superar 100 caracteres")
    private String nombres;

    @NotBlank(message = "Los apellidos son obligatorios")
    @Size(max = 100, message = "Los apellidos no deben superar 100 caracteres")
    private String apellidos;

    private LocalDate fechaNacimiento;

    @Size(max = 20, message = "El teléfono no debe superar 20 caracteres")
    private String telefono;

    @Email(message = "El email no tiene un formato válido")
    @Size(max = 120, message = "El email no debe superar 120 caracteres")
    private String email;

    @Size(max = 250, message = "La dirección no debe superar 250 caracteres")
    private String direccion;

    @NotNull(message = "La fecha de ingreso es obligatoria")
    private LocalDate fechaIngreso;

    private LocalDate fechaCese;

    @Size(max = 100, message = "El cargo no debe superar 100 caracteres")
    private String cargo;

    @Size(max = 100, message = "El área no debe superar 100 caracteres")
    private String area;

    @Size(max = 50, message = "El tipo de contrato no debe superar 50 caracteres")
    private String tipoContrato;

    @Size(max = 50, message = "El régimen laboral no debe superar 50 caracteres")
    private String regimenLaboral;

    @NotNull(message = "El sueldo base es obligatorio")
    @DecimalMin(value = "0.00", message = "El sueldo base no puede ser negativo")
    private BigDecimal sueldoBase;
}