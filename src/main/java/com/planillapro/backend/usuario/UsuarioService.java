package com.planillapro.backend.usuario;

import com.planillapro.backend.security.AuthenticatedUserService;
import com.planillapro.backend.shared.exception.AccessDeniedAppException;
import com.planillapro.backend.shared.exception.BusinessException;
import com.planillapro.backend.empresa.Empresa;
import com.planillapro.backend.empresa.EmpresaRepository;
import com.planillapro.backend.rol.Rol;
import com.planillapro.backend.rol.RolRepository;
import com.planillapro.backend.shared.exception.ResourceNotFoundException;
import com.planillapro.backend.usuario.dto.UsuarioRequestDTO;
import com.planillapro.backend.usuario.dto.UsuarioResponseDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final EmpresaRepository empresaRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticatedUserService authenticatedUserService;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            EmpresaRepository empresaRepository,
            RolRepository rolRepository,
            PasswordEncoder passwordEncoder,
            AuthenticatedUserService authenticatedUserService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.empresaRepository = empresaRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticatedUserService = authenticatedUserService;
    }

    public UsuarioResponseDTO crear(UsuarioRequestDTO request) {
        authenticatedUserService.validarAccesoEmpresa(request.getEmpresaId());

        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Ya existe un usuario registrado con ese email");
        }

        Empresa empresa = empresaRepository.findById(request.getEmpresaId())
                .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada"));

        Rol rol = rolRepository.findById(request.getRolId())
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado"));

        if (AuthenticatedUserService.ROL_SUPER_ADMIN.equals(rol.getNombre())
                && !authenticatedUserService.esSuperAdmin()) {
            throw new AccessDeniedAppException("No tienes permiso para asignar el rol SUPER_ADMIN");
        }

        Usuario usuario = new Usuario();
        usuario.setEmpresa(empresa);
        usuario.setRol(rol);
        usuario.setNombres(request.getNombres());
        usuario.setApellidos(request.getApellidos());
        usuario.setEmail(request.getEmail());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setEstado("ACTIVO");

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        return convertirAResponse(usuarioGuardado);
    }

    public List<UsuarioResponseDTO> listar() {
        List<Usuario> usuarios = authenticatedUserService.esSuperAdmin()
                ? usuarioRepository.findAll()
                : usuarioRepository.findByEmpresaId(authenticatedUserService.obtenerEmpresaIdActual());

        return usuarios
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    public UsuarioResponseDTO buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        authenticatedUserService.validarAccesoEmpresa(usuario.getEmpresa().getId());

        return convertirAResponse(usuario);
    }

    private UsuarioResponseDTO convertirAResponse(Usuario usuario) {
        UsuarioResponseDTO response = new UsuarioResponseDTO();

        response.setId(usuario.getId());

        response.setEmpresaId(usuario.getEmpresa().getId());
        response.setEmpresaRazonSocial(usuario.getEmpresa().getRazonSocial());

        response.setRolId(usuario.getRol().getId());
        response.setRolNombre(usuario.getRol().getNombre());

        response.setNombres(usuario.getNombres());
        response.setApellidos(usuario.getApellidos());
        response.setEmail(usuario.getEmail());
        response.setEstado(usuario.getEstado());

        response.setCreatedAt(usuario.getCreatedAt());
        response.setUpdatedAt(usuario.getUpdatedAt());

        return response;
    }
}