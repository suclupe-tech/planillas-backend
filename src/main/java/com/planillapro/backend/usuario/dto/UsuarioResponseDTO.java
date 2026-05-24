package com.planillapro.backend.usuario.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UsuarioResponseDTO {

    private Long id;

    private Long empresaId;
    private String empresaRazonSocial;

    private Long rolId;
    private String rolNombre;

    private String nombres;
    private String apellidos;
    private String email;
    private String estado;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}