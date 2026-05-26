package com.planillapro.backend.security;

import com.planillapro.backend.usuario.Usuario;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticatedUserService {

    public Usuario obtenerUsuarioActual() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Usuario no autenticado");
        }

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof Usuario usuario)) {
            throw new RuntimeException("No se pudo obtener el usuario autenticado");
        }

        return usuario;
    }

    public Long obtenerEmpresaIdActual() {
        return obtenerUsuarioActual().getEmpresa().getId();
    }

    public String obtenerRolActual() {
        return obtenerUsuarioActual().getRol().getNombre();
    }
}