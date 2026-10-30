package com.planillapro.backend.security;

import com.planillapro.backend.shared.exception.AccessDeniedAppException;
import com.planillapro.backend.shared.exception.UnauthorizedException;
import com.planillapro.backend.usuario.Usuario;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticatedUserService {

    public static final String ROL_SUPER_ADMIN = "SUPER_ADMIN";

    public Usuario obtenerUsuarioActual() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("Usuario no autenticado");
        }

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof Usuario usuario)) {
            throw new UnauthorizedException("No se pudo obtener el usuario autenticado");
        }

        return usuario;
    }

    public Long obtenerEmpresaIdActual() {
        return obtenerUsuarioActual().getEmpresa().getId();
    }

    public String obtenerRolActual() {
        return obtenerUsuarioActual().getRol().getNombre();
    }

    public boolean esSuperAdmin() {
        return ROL_SUPER_ADMIN.equals(obtenerRolActual());
    }

    public void validarAccesoEmpresa(Long empresaId) {
        if (esSuperAdmin()) {
            return;
        }

        if (!obtenerEmpresaIdActual().equals(empresaId)) {
            throw new AccessDeniedAppException("No tienes acceso a la información de esta empresa");
        }
    }
}
