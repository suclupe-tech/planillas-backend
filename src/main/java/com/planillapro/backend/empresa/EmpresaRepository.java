package com.planillapro.backend.empresa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    Optional<Empresa> findByRuc(String ruc);

    boolean existsByRuc(String ruc);
}