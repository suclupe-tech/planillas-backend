package com.planillapro.backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.planillapro.backend.security.jwt.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.config.Customizer;

import java.util.List;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers("/api/health").permitAll()

                        .requestMatchers("/api/usuarios/**").hasAnyRole("SUPER_ADMIN", "ADMIN_EMPRESA")
                        .requestMatchers("/api/empresas/**").hasAnyRole("SUPER_ADMIN", "ADMIN_EMPRESA")
                        .requestMatchers("/api/trabajadores/**").hasAnyRole("SUPER_ADMIN", "ADMIN_EMPRESA", "RRHH")

                        .requestMatchers(HttpMethod.GET, "/api/dashboard/**")
                        .hasAnyRole("SUPER_ADMIN", "ADMIN_EMPRESA", "RRHH", "CONTADOR")
                        .requestMatchers("/api/roles/**").authenticated()

                        .requestMatchers(HttpMethod.GET, "/api/conceptos-planilla/**")
                        .hasAnyRole("SUPER_ADMIN", "ADMIN_EMPRESA", "RRHH", "CONTADOR")

                        .requestMatchers(HttpMethod.GET, "/api/periodos-planilla/**")
                        .hasAnyRole("SUPER_ADMIN", "ADMIN_EMPRESA", "RRHH", "CONTADOR")

                        .requestMatchers(HttpMethod.POST, "/api/periodos-planilla/**")
                        .hasAnyRole("SUPER_ADMIN", "ADMIN_EMPRESA", "RRHH")

                        .requestMatchers(HttpMethod.PATCH, "/api/periodos-planilla/**")
                        .hasAnyRole("SUPER_ADMIN", "ADMIN_EMPRESA", "RRHH")

                        .requestMatchers(HttpMethod.GET, "/api/detalles-planilla/**")
                        .hasAnyRole("SUPER_ADMIN", "ADMIN_EMPRESA", "RRHH", "CONTADOR")

                        .requestMatchers(HttpMethod.GET, "/api/auditoria-planilla/**")
                        .hasAnyRole("SUPER_ADMIN", "ADMIN_EMPRESA", "RRHH", "CONTADOR")

                        .requestMatchers(HttpMethod.POST, "/api/detalles-planilla/**")
                        .hasAnyRole("SUPER_ADMIN", "ADMIN_EMPRESA", "RRHH")

                        .requestMatchers(HttpMethod.PUT, "/api/detalles-planilla/**")
                        .hasAnyRole("SUPER_ADMIN", "ADMIN_EMPRESA", "RRHH")

                        .requestMatchers(HttpMethod.DELETE, "/api/detalles-planilla/**")
                        .hasAnyRole("SUPER_ADMIN", "ADMIN_EMPRESA", "RRHH")

                        .anyRequest().authenticated()
                )
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}