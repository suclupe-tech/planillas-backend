package com.planillapro.backend.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDTO {

    private Long usuarioId;

    private Long empresaId;
    private String empresaRazonSocial;

    private Long rolId;
    private String rolNombre;

    private String nombres;
    private String apellidos;
    private String email;

    private String token;
    private  String tipoToken;

    private String mensaje;
}