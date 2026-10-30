package com.planillapro.backend.auth;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.planillapro.backend.auth.dto.LoginRequestDTO;
import com.planillapro.backend.security.jwt.JwtService;
import com.planillapro.backend.shared.exception.UnauthorizedException;
import com.planillapro.backend.usuario.Usuario;
import com.planillapro.backend.usuario.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    @Test
    void emailInexistenteDevuelveMismoErrorQueClaveIncorrecta() {
        when(usuarioRepository.findByEmail("nadie@x.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(request("nadie@x.com")))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage("Credenciales incorrectas");
    }

    @Test
    void claveIncorrectaDevuelveUnauthorized() {
        when(usuarioRepository.findByEmail("ana@x.com")).thenReturn(Optional.of(usuario("ACTIVO")));
        when(passwordEncoder.matches("clave", "hash")).thenReturn(false);

        assertThatThrownBy(() -> authService.login(request("ana@x.com")))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage("Credenciales incorrectas");
    }

    @Test
    void usuarioInactivoNoRevelaSuEstado() {
        when(usuarioRepository.findByEmail("ana@x.com")).thenReturn(Optional.of(usuario("INACTIVO")));
        when(passwordEncoder.matches("clave", "hash")).thenReturn(true);

        assertThatThrownBy(() -> authService.login(request("ana@x.com")))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage("Credenciales incorrectas");
    }

    private LoginRequestDTO request(String email) {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail(email);
        request.setPassword("clave");
        return request;
    }

    private Usuario usuario(String estado) {
        Usuario usuario = new Usuario();
        usuario.setPassword("hash");
        usuario.setEstado(estado);
        return usuario;
    }
}
