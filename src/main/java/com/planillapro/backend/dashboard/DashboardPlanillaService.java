package com.planillapro.backend.dashboard;

import com.planillapro.backend.dashboard.dto.DashboardPlanillaDTO;
import com.planillapro.backend.periodo.PeriodoPlanilla;
import com.planillapro.backend.periodo.PeriodoPlanillaRepository;
import com.planillapro.backend.planilla.DetallePlanillaService;
import com.planillapro.backend.planilla.dto.ResumenPlanillaPeriodoDTO;
import com.planillapro.backend.security.AuthenticatedUserService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class DashboardPlanillaService {

    private final PeriodoPlanillaRepository periodoPlanillaRepository;
    private final DetallePlanillaService detallePlanillaService;
    private final AuthenticatedUserService authenticatedUserService;

    public DashboardPlanillaService(
            PeriodoPlanillaRepository periodoPlanillaRepository,
            DetallePlanillaService detallePlanillaService,
            AuthenticatedUserService authenticatedUserService
    ) {
        this.periodoPlanillaRepository = periodoPlanillaRepository;
        this.detallePlanillaService = detallePlanillaService;
        this.authenticatedUserService = authenticatedUserService;
    }

    public DashboardPlanillaDTO obtenerDashboardPlanillas() {
        Long empresaId = authenticatedUserService.obtenerEmpresaIdActual();

        DashboardPlanillaDTO dto = new DashboardPlanillaDTO();

        dto.setEmpresaId(empresaId);
        dto.setEmpresaRazonSocial(
                authenticatedUserService.obtenerUsuarioActual()
                        .getEmpresa()
                        .getRazonSocial()
        );

        dto.setTotalPlanillas(periodoPlanillaRepository.countByEmpresaId(empresaId));
        dto.setPlanillasAbiertas(periodoPlanillaRepository.countByEmpresaIdAndEstado(empresaId, "ABIERTO"));
        dto.setPlanillasCerradas(periodoPlanillaRepository.countByEmpresaIdAndEstado(empresaId, "CERRADO"));

        PeriodoPlanilla ultimaPlanilla =
                periodoPlanillaRepository.findFirstByEmpresaIdOrderByIdDesc(empresaId);

        if (ultimaPlanilla == null) {
            dto.setTotalIngresos(BigDecimal.ZERO);
            dto.setTotalDescuentos(BigDecimal.ZERO);
            dto.setTotalNetoPagar(BigDecimal.ZERO);
            return dto;
        }

        BigDecimal totalIngresos = BigDecimal.ZERO;
        BigDecimal totalDescuentos = BigDecimal.ZERO;
        BigDecimal totalNetoPagar = BigDecimal.ZERO;

        for (PeriodoPlanilla periodo : periodoPlanillaRepository.findByEmpresaId(empresaId)) {
            ResumenPlanillaPeriodoDTO resumen =
                    detallePlanillaService.calcularResumenPeriodo(periodo.getId());

            totalIngresos = totalIngresos.add(resumen.getTotalIngresos());
            totalDescuentos = totalDescuentos.add(resumen.getTotalDescuentos());
            totalNetoPagar = totalNetoPagar.add(resumen.getTotalNetoPagar());
        }

        dto.setTotalIngresos(totalIngresos);
        dto.setTotalDescuentos(totalDescuentos);
        dto.setTotalNetoPagar(totalNetoPagar);

        dto.setUltimaPlanillaId(ultimaPlanilla.getId());
        dto.setUltimaPlanillaNombre(ultimaPlanilla.getNombre());
        dto.setUltimaPlanillaTipo(ultimaPlanilla.getTipo());
        dto.setUltimaPlanillaEstado(ultimaPlanilla.getEstado());

        return dto;
    }
}