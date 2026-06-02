package com.planillapro.backend.planilla;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DetallePlanillaRepository extends JpaRepository<DetallePlanilla, Long> {

    List<DetallePlanilla> findByPeriodoPlanillaId(Long periodoPlanillaId);

    List<DetallePlanilla> findByTrabajadorId(Long trabajadorId);

    List<DetallePlanilla> findByPeriodoPlanillaIdAndTrabajadorId(
            Long periodoPlanillaId,
            Long trabajadorId
    );

    Optional<DetallePlanilla> findByPeriodoPlanillaIdAndTrabajadorIdAndConceptoPlanillaId(
            Long periodoPlanillaId,
            Long trabajadorId,
            Long conceptoPlanillaId
    );

    boolean existsByPeriodoPlanillaIdAndTrabajadorIdAndConceptoPlanillaId(
            Long periodoPlanillaId,
            Long trabajadorId,
            Long conceptoPlanillaId
    );
}