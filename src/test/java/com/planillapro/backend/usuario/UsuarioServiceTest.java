package com.planillapro.backend.usuario;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import com.planillapro.backend.empresa.Empresa;
import com.planillapro.backend.empresa.EmpresaRepository;
import com.planillapro.backend.rol.Rol;
import com.planillapro.backend.rol.RolRepository;
import com.planillapro.backend.security.AuthenticatedUserService;
import com.planillapro.backend.shared.exception.AccessDeniedAppException;
import com.planillapro.backend.shared.exception.ResourceNotFoundException;
import com.planillapro.backend.usuario.dto.UsuarioRequestDTO;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private EmpresaRepository empresaRepository;

    @Mock
    private RolRepository rolRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticatedUserService authenticatedUserService;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void adminEmpresaNoPuedeCrearUsuarioEnOtraEmpresa() {
        doThrow(new AccessDeniedAppException("sin acceso"))
                .when(authenticatedUserService).validarAccesoEmpresa(2L);

        assertThatThrownBy(() -> usuarioService.crear(request(2L, 3L)))
                .isInstanceOf(AccessDeniedAppException.class);

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void adminEmpresaNoPuedeAsignarRolSuperAdmin() {
        when(empresaRepository.findById(1L)).thenReturn(Optional.of(empresa(1L)));
        when(rolRepository.findById(1L)).thenReturn(Optional.of(rol(1L, "SUPER_ADMIN")));
        when(authenticatedUserService.esSuperAdmin()).thenReturn(false);

        assertThatThrownBy(() -> usuarioService.crear(request(1L, 1L)))
                .isInstanceOf(AccessDeniedAppException.class);

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void superAdminPuedeAsignarRolSuperAdmin() {
        when(empresaRepository.findById(1L)).thenReturn(Optional.of(empresa(1L)));
        when(rolRepository.findById(1L)).thenReturn(Optional.of(rol(1L, "SUPER_ADMIN")));
        when(authenticatedUserService.esSuperAdmin()).thenReturn(true);
        when(usuarioRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        assertThat(usuarioService.crear(request(1L, 1L)).getRolNombre()).isEqualTo("SUPER_ADMIN");
    }

    @Test
    void listarSoloDevuelveUsuariosDeLaEmpresaActual() {
        when(authenticatedUserService.esSuperAdmin()).thenReturn(false);
        when(authenticatedUserService.obtenerEmpresaIdActual()).thenReturn(1L);
        when(usuarioRepository.findByEmpresaId(1L)).thenReturn(List.of(usuario(10L, empresa(1L))));

        assertThat(usuarioService.listar()).extracting("empresaId").containsOnly(1L);

        verify(usuarioRepository, never()).findAll();
    }

    @Test
    void buscarPorIdDeOtraEmpresaEsRechazado() {
        when(usuarioRepository.findById(10L)).thenReturn(Optional.of(usuario(10L, empresa(2L))));
        doThrow(new AccessDeniedAppException("sin acceso"))
                .when(authenticatedUserService).validarAccesoEmpresa(2L);

        assertThatThrownBy(() -> usuarioService.buscarPorId(10L))
                .isInstanceOf(AccessDeniedAppException.class);
    }

    @Test
    void buscarPorIdInexistenteDevuelveNotFound() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioService.buscarPorId(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    private UsuarioRequestDTO request(Long empresaId, Long rolId) {
        UsuarioRequestDTO request = new UsuarioRequestDTO();
        request.setEmpresaId(empresaId);
        request.setRolId(rolId);
        request.setNombres("Ana");
        request.setApellidos("Pérez");
        request.setEmail("ana@empresa.com");
        request.setPassword("secreto123");
        return request;
    }

    private Empresa empresa(Long id) {
        Empresa empresa = new Empresa();
        empresa.setId(id);
        empresa.setRazonSocial("Empresa " + id);
        return empresa;
    }

    private Rol rol(Long id, String nombre) {
        Rol rol = new Rol();
        rol.setId(id);
        rol.setNombre(nombre);
        return rol;
    }

    private Usuario usuario(Long id, Empresa empresa) {
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setEmpresa(empresa);
        usuario.setRol(rol(3L, "RRHH"));
        return usuario;
    }
}
