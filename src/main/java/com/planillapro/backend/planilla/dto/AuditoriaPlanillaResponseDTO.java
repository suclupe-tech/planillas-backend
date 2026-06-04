package com.planillapro.backend.planilla.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AuditoriaPlanillaResponseDTO {

    private Long id;

    private Long usuarioId;
    private String usuarioNombreCompleto;
    private String usuarioEmail;

    private Long empresaId;
    private String empresaRazonSocial;

    private Long periodoPlanillaId;
    private String periodoNombre;

    private Long trabajadorId;
    private String trabajadorNombreCompleto;
    private String trabajadorDocumento;

    private Long detallePlanillaId;

    private String accion;
    private String descripcion;

    private LocalDateTime fechaHora;
}