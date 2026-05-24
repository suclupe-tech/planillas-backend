package com.planillapro.backend.rol.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RolResponseDTO {

    private Long id;
    private String nombre;
    private String descripcion;
    private String estado;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}