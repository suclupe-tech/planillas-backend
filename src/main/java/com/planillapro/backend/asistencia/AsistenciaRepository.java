package com.planillapro.backend.asistencia;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {

    List<Asistencia> findByEmpresaIdOrderByFechaDesc(Long empresaId);

    List<Asistencia> findByEmpresaIdAndFechaBetweenOrderByFechaDesc(
            Long empresaId,
            LocalDate fechaDesde,
            LocalDate fechaHasta
    );

    List<Asistencia> findByTrabajadorIdAndFechaBetweenOrderByFechaDesc(
            Long trabajadorId,
            LocalDate fechaDesde,
            LocalDate fechaHasta
    );

    boolean existsByTrabajadorIdAndFecha(Long trabajadorId, LocalDate fecha);
}