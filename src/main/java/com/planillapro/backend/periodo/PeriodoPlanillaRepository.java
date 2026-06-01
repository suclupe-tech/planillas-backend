package com.planillapro.backend.periodo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PeriodoPlanillaRepository extends JpaRepository<PeriodoPlanilla, Long> {

    List<PeriodoPlanilla> findByEmpresaId(Long empresaId);

    List<PeriodoPlanilla> findByEmpresaIdAndEstado(Long empresaId, String estado);

    boolean existsByEmpresaIdAndTipoAndFechaInicioAndFechaFin(
            Long empresaId,
            String tipo,
            LocalDate fechaInicio,
            LocalDate fechaFin
    );
}