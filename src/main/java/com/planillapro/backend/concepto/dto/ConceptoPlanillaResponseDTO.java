package com.planillapro.backend.concepto.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ConceptoPlanillaResponseDTO {

    private Long id;

    private String codigo;
    private String nombre;
    private String descripcion;

    private String tipo;
    private String formula;

    private Boolean esRemunerativo;
    private Boolean afectaAfpOnp;
    private Boolean afectaEssalud;

    private String estado;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}