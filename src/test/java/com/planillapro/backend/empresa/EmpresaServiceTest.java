package com.planillapro.backend.empresa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.planillapro.backend.security.AuthenticatedUserService;
import com.planillapro.backend.shared.exception.AccessDeniedAppException;

@ExtendWith(MockitoExtension.class)
class EmpresaServiceTest {

    @Mock
    private EmpresaRepository empresaRepository;

    @Mock
    private AuthenticatedUserService authenticatedUserService;

    @InjectMocks
    private EmpresaService empresaService;

    @Test
    void adminEmpresaSoloVeSuPropiaEmpresa() {
        when(authenticatedUserService.esSuperAdmin()).thenReturn(false);
        when(authenticatedUserService.obtenerEmpresaIdActual()).thenReturn(1L);
        when(empresaRepository.findById(1L)).thenReturn(Optional.of(empresa(1L)));

        assertThat(empresaService.listar()).extracting("id").containsExactly(1L);

        verify(empresaRepository, never()).findAll();
    }

    @Test
    void superAdminVeTodasLasEmpresas() {
        when(authenticatedUserService.esSuperAdmin()).thenReturn(true);
        when(empresaRepository.findAll()).thenReturn(List.of(empresa(1L), empresa(2L)));

        assertThat(empresaService.listar()).hasSize(2);
    }

    @Test
    void buscarEmpresaAjenaEsRechazado() {
        doThrow(new AccessDeniedAppException("sin acceso"))
                .when(authenticatedUserService).validarAccesoEmpresa(2L);

        assertThatThrownBy(() -> empresaService.buscarPorId(2L))
                .isInstanceOf(AccessDeniedAppException.class);

        verify(empresaRepository, never()).findById(2L);
    }

    private Empresa empresa(Long id) {
        Empresa empresa = new Empresa();
        empresa.setId(id);
        return empresa;
    }
}
