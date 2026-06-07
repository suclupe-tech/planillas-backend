package com.planillapro.backend.planilla;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditoriaPlanillaRepository extends JpaRepository<AuditoriaPlanilla, Long> {

    List<AuditoriaPlanilla> findByPeriodoPlanillaIdOrderByFechaHoraDesc(Long periodoPlanillaId);

    List<AuditoriaPlanilla> findByTrabajadorIdOrderByFechaHoraDesc(Long trabajadorId);

    List<AuditoriaPlanilla> findByEmpresaIdOrderByFechaHoraDesc(Long empresaId);

    List<AuditoriaPlanilla> findByUsuarioIdOrderByFechaHoraDesc(Long usuarioId);

    List<AuditoriaPlanilla> findByEmpresaIdAndAccionOrderByFechaHoraDesc(
            Long empresaId,
            String accion
    );

    List<AuditoriaPlanilla> findByPeriodoPlanillaIdAndAccionOrderByFechaHoraDesc(
            Long periodoPlanillaId,
            String accion
    );
}