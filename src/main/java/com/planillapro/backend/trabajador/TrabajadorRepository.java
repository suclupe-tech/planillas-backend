package com.planillapro.backend.trabajador;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TrabajadorRepository extends JpaRepository<Trabajador, Long> {

    List<Trabajador> findByEmpresaId(Long empresaId);

    Optional<Trabajador> findByEmpresaIdAndTipoDocumentoAndNumeroDocumento(
            Long empresaId,
            String tipoDocumento,
            String numeroDocumento
    );

    boolean existsByEmpresaIdAndTipoDocumentoAndNumeroDocumento(
            Long empresaId,
            String tipoDocumento,
            String numeroDocumento
    );
}