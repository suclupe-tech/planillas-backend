package com.planillapro.backend.empresa.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EmpresaResponseDTO {

    private Long id;
    private String ruc;
    private String razonSocial;
    private String nombreComercial;
    private String direccion;
    private String telefono;
    private String email;
    private String estado;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}