package com.planillapro.backend.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.planillapro.backend.auth.dto.LoginRequestDTO;
import com.planillapro.backend.auth.dto.LoginResponseDTO;
import com.planillapro.backend.security.jwt.JwtService;
import com.planillapro.backend.shared.exception.ResourceNotFoundException;
import com.planillapro.backend.usuario.Usuario;
import com.planillapro.backend.usuario.UsuarioRepository;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginResponseDTO login(LoginRequestDTO request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        boolean passwordValido = passwordEncoder.matches(
                request.getPassword(),
                usuario.getPassword()
        );

        if (!passwordValido) {
            throw new RuntimeException("Credenciales incorrectas");
        }

        if (!"ACTIVO".equals(usuario.getEstado())) {
            throw new RuntimeException("El usuario no está activo");
        }

        String token = jwtService.generarToken(usuario);

        LoginResponseDTO response = new LoginResponseDTO();

        response.setUsuarioId(usuario.getId());

        response.setEmpresaId(usuario.getEmpresa().getId());
        response.setEmpresaRazonSocial(usuario.getEmpresa().getRazonSocial());

        response.setRolId(usuario.getRol().getId());
        response.setRolNombre(usuario.getRol().getNombre());

        response.setNombres(usuario.getNombres());
        response.setApellidos(usuario.getApellidos());
        response.setEmail(usuario.getEmail());

        response.setToken(token);
        response.setTipoToken("Bearer");

        response.setMensaje("Login correcto");

        return response;
    }
}