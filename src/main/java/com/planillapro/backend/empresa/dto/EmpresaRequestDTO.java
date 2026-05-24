package com.planillapro.backend.empresa.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmpresaRequestDTO {

    @NotBlank(message = "El RUC es obligatorio")
    @Pattern(regexp = "\\d{11}", message = "El RUC debe tener 11 dígitos")
    private String ruc;

    @NotBlank(message = "La razón social es obligatoria")
    @Size(max = 150, message = "La razón social no debe superar 150 caracteres")
    private String razonSocial;

    @Size(max = 150, message = "El nombre comercial no debe superar 150 caracteres")
    private String nombreComercial;

    @Size(max = 250, message = "La dirección no debe superar 250 caracteres")
    private String direccion;

    @Size(max = 20, message = "El teléfono no debe superar 20 caracteres")
    private String telefono;

    @Email(message = "El email no tiene un formato válido")
    @Size(max = 120, message = "El email no debe superar 120 caracteres")
    private String email;
}