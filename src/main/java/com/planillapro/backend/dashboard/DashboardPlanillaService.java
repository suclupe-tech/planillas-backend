package com.planillapro.backend.dashboard;

import com.planillapro.backend.dashboard.dto.DashboardPlanillaDTO;
import com.planillapro.backend.periodo.PeriodoPlanilla;
import com.planillapro.backend.periodo.PeriodoPlanillaRepository;
import com.planillapro.backend.planilla.DetallePlanillaService;
import com.planillapro.backend.planilla.dto.ResumenPlanillaPeriodoDTO;
import com.planillapro.backend.security.AuthenticatedUserService;
import org.springframework.stereotype.Service;
import com.planillapro.backend.dashboard.dto.DashboardUltimaPlanillaDTO;
import org.springframework.data.domain.PageRequest;
import com.planillapro.backend.dashboard.dto.DashboardPlanillaMensualDTO;

import java.time.Month;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import java.util.List;

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

    public List<DashboardUltimaPlanillaDTO> obtenerUltimasPlanillas() {
        Long empresaId = authenticatedUserService.obtenerEmpresaIdActual();

        return periodoPlanillaRepository
                .findByEmpresaIdOrderByIdDesc(empresaId, PageRequest.of(0, 5))
                .stream()
                .map(this::convertirAUltimaPlanillaDTO)
                .toList();
    }

    private DashboardUltimaPlanillaDTO convertirAUltimaPlanillaDTO(PeriodoPlanilla periodo) {
        DashboardUltimaPlanillaDTO dto = new DashboardUltimaPlanillaDTO();

        dto.setId(periodo.getId());
        dto.setNombre(periodo.getNombre());
        dto.setTipo(periodo.getTipo());
        dto.setEstado(periodo.getEstado());
        dto.setFechaInicio(periodo.getFechaInicio());
        dto.setFechaFin(periodo.getFechaFin());

        return dto;
    }

    public List<DashboardPlanillaMensualDTO> obtenerTotalesMensuales() {
        Long empresaId = authenticatedUserService.obtenerEmpresaIdActual();

        Map<String, DashboardPlanillaMensualDTO> mapa = new TreeMap<>();

        for (PeriodoPlanilla periodo : periodoPlanillaRepository.findByEmpresaId(empresaId)) {
            Integer anio = periodo.getFechaInicio().getYear();
            Integer mes = periodo.getFechaInicio().getMonthValue();

            String clave = anio + "-" + String.format("%02d", mes);

            DashboardPlanillaMensualDTO dto = mapa.getOrDefault(
                    clave,
                    crearDashboardMensualDTO(anio, mes)
            );

            ResumenPlanillaPeriodoDTO resumen =
                    detallePlanillaService.calcularResumenPeriodo(periodo.getId());

            dto.setTotalIngresos(dto.getTotalIngresos().add(resumen.getTotalIngresos()));
            dto.setTotalDescuentos(dto.getTotalDescuentos().add(resumen.getTotalDescuentos()));
            dto.setTotalNetoPagar(dto.getTotalNetoPagar().add(resumen.getTotalNetoPagar()));

            mapa.put(clave, dto);
        }

        return new ArrayList<>(mapa.values());
    }

    private DashboardPlanillaMensualDTO crearDashboardMensualDTO(Integer anio, Integer mes) {
        DashboardPlanillaMensualDTO dto = new DashboardPlanillaMensualDTO();

        dto.setAnio(anio);
        dto.setMes(mes);
        dto.setNombreMes(obtenerNombreMes(mes));
        dto.setTotalIngresos(BigDecimal.ZERO);
        dto.setTotalDescuentos(BigDecimal.ZERO);
        dto.setTotalNetoPagar(BigDecimal.ZERO);

        return dto;
    }

    private String obtenerNombreMes(Integer mes) {
        return switch (Month.of(mes)) {
            case JANUARY -> "Enero";
            case FEBRUARY -> "Febrero";
            case MARCH -> "Marzo";
            case APRIL -> "Abril";
            case MAY -> "Mayo";
            case JUNE -> "Junio";
            case JULY -> "Julio";
            case AUGUST -> "Agosto";
            case SEPTEMBER -> "Septiembre";
            case OCTOBER -> "Octubre";
            case NOVEMBER -> "Noviembre";
            case DECEMBER -> "Diciembre";
        };
    }
}