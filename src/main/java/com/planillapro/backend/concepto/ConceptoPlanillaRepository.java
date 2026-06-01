package com.planillapro.backend.concepto;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConceptoPlanillaRepository extends JpaRepository<ConceptoPlanilla, Long> {

    Optional<ConceptoPlanilla> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);

    List<ConceptoPlanilla> findByTipo(String tipo);

    List<ConceptoPlanilla> findByEstado(String estado);
}