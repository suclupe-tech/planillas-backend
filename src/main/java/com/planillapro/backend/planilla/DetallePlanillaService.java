package com.planillapro.backend.planilla;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.planillapro.backend.concepto.ConceptoPlanilla;
import com.planillapro.backend.concepto.ConceptoPlanillaRepository;
import com.planillapro.backend.periodo.PeriodoPlanilla;
import com.planillapro.backend.periodo.PeriodoPlanillaRepository;
import com.planillapro.backend.planilla.dto.DetallePlanillaRequestDTO;
import com.planillapro.backend.planilla.dto.DetallePlanillaResponseDTO;
import com.planillapro.backend.planilla.dto.ResumenPlanillaPeriodoDTO;
import com.planillapro.backend.planilla.dto.ResumenPlanillaTrabajadorDTO;
import com.planillapro.backend.security.AuthenticatedUserService;
import com.planillapro.backend.shared.exception.AccessDeniedAppException;
import com.planillapro.backend.shared.exception.ResourceNotFoundException;
import com.planillapro.backend.trabajador.Trabajador;
import com.planillapro.backend.trabajador.TrabajadorRepository;

@Service
public class DetallePlanillaService {

    private final DetallePlanillaRepository detallePlanillaRepository;
    private final PeriodoPlanillaRepository periodoPlanillaRepository;
    private final TrabajadorRepository trabajadorRepository;
    private final ConceptoPlanillaRepository conceptoPlanillaRepository;
    private final AuthenticatedUserService authenticatedUserService;

    public DetallePlanillaService(
            DetallePlanillaRepository detallePlanillaRepository,
            PeriodoPlanillaRepository periodoPlanillaRepository,
            TrabajadorRepository trabajadorRepository,
            ConceptoPlanillaRepository conceptoPlanillaRepository,
            AuthenticatedUserService authenticatedUserService
    ) {
        this.detallePlanillaRepository = detallePlanillaRepository;
        this.periodoPlanillaRepository = periodoPlanillaRepository;
        this.trabajadorRepository = trabajadorRepository;
        this.conceptoPlanillaRepository = conceptoPlanillaRepository;
        this.authenticatedUserService = authenticatedUserService;
    }

    public DetallePlanillaResponseDTO crear(DetallePlanillaRequestDTO request) {
        PeriodoPlanilla periodo = periodoPlanillaRepository.findById(request.getPeriodoPlanillaId())
                .orElseThrow(() -> new ResourceNotFoundException("Periodo de planilla no encontrado"));

        validarAccesoEmpresa(periodo.getEmpresa().getId());

        if ("CERRADO".equals(periodo.getEstado())) {
            throw new RuntimeException("No se puede registrar detalles en un periodo cerrado");
        }

        Trabajador trabajador = trabajadorRepository.findById(request.getTrabajadorId())
                .orElseThrow(() -> new ResourceNotFoundException("Trabajador no encontrado"));

        validarAccesoEmpresa(trabajador.getEmpresa().getId());

        if (!periodo.getEmpresa().getId().equals(trabajador.getEmpresa().getId())) {
            throw new RuntimeException("El trabajador no pertenece a la empresa del periodo");
        }

        ConceptoPlanilla concepto = conceptoPlanillaRepository.findById(request.getConceptoPlanillaId())
                .orElseThrow(() -> new ResourceNotFoundException("Concepto de planilla no encontrado"));

        if (detallePlanillaRepository.existsByPeriodoPlanillaIdAndTrabajadorIdAndConceptoPlanillaId(
                request.getPeriodoPlanillaId(),
                request.getTrabajadorId(),
                request.getConceptoPlanillaId()
        )) {
            throw new RuntimeException("Ya existe este concepto registrado para el trabajador en este periodo");
        }

        DetallePlanilla detalle = new DetallePlanilla();
        detalle.setPeriodoPlanilla(periodo);
        detalle.setTrabajador(trabajador);
        detalle.setConceptoPlanilla(concepto);
        detalle.setTipo(concepto.getTipo());
        detalle.setMonto(request.getMonto());
        detalle.setObservacion(request.getObservacion());

        DetallePlanilla detalleGuardado = detallePlanillaRepository.save(detalle);

        return convertirAResponse(detalleGuardado);
    }

        public DetallePlanillaResponseDTO actualizar(Long id, DetallePlanillaRequestDTO request) {
        DetallePlanilla detalle = detallePlanillaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Detalle de planilla no encontrado"));

        PeriodoPlanilla periodoActual = detalle.getPeriodoPlanilla();

        validarAccesoEmpresa(periodoActual.getEmpresa().getId());

        if ("CERRADO".equals(periodoActual.getEstado())) {
            throw new RuntimeException("No se puede modificar detalles de un periodo cerrado");
        }

        PeriodoPlanilla nuevoPeriodo = periodoPlanillaRepository.findById(request.getPeriodoPlanillaId())
                .orElseThrow(() -> new ResourceNotFoundException("Periodo de planilla no encontrado"));

        Trabajador trabajador = trabajadorRepository.findById(request.getTrabajadorId())
                .orElseThrow(() -> new ResourceNotFoundException("Trabajador no encontrado"));

        ConceptoPlanilla concepto = conceptoPlanillaRepository.findById(request.getConceptoPlanillaId())
                .orElseThrow(() -> new ResourceNotFoundException("Concepto de planilla no encontrado"));

        validarAccesoEmpresa(nuevoPeriodo.getEmpresa().getId());
        validarAccesoEmpresa(trabajador.getEmpresa().getId());

        if ("CERRADO".equals(nuevoPeriodo.getEstado())) {
            throw new RuntimeException("No se puede modificar detalles de un periodo cerrado");
        }

        if (!nuevoPeriodo.getEmpresa().getId().equals(trabajador.getEmpresa().getId())) {
            throw new RuntimeException("El trabajador no pertenece a la empresa del periodo");
        }

        detallePlanillaRepository
                .findByPeriodoPlanillaIdAndTrabajadorIdAndConceptoPlanillaId(
                        request.getPeriodoPlanillaId(),
                        request.getTrabajadorId(),
                        request.getConceptoPlanillaId()
                )
                .ifPresent(detalleExistente -> {
                    if (!detalleExistente.getId().equals(id)) {
                        throw new RuntimeException("Ya existe este concepto registrado para el trabajador en este periodo");
                    }
                });

        detalle.setPeriodoPlanilla(nuevoPeriodo);
        detalle.setTrabajador(trabajador);
        detalle.setConceptoPlanilla(concepto);
        detalle.setTipo(concepto.getTipo());
        detalle.setMonto(request.getMonto());
        detalle.setObservacion(request.getObservacion());

        DetallePlanilla detalleActualizado = detallePlanillaRepository.save(detalle);

        return convertirAResponse(detalleActualizado);
    }

    public void eliminar(Long id) {
        DetallePlanilla detalle = detallePlanillaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Detalle de planilla no encontrado"));

        PeriodoPlanilla periodo = detalle.getPeriodoPlanilla();

        validarAccesoEmpresa(periodo.getEmpresa().getId());

        if ("CERRADO".equals(periodo.getEstado())) {
            throw new RuntimeException("No se puede eliminar detalles de un periodo cerrado");
        }

        detallePlanillaRepository.delete(detalle);
    }

    public List<DetallePlanillaResponseDTO> listarPorPeriodo(Long periodoPlanillaId) {
        PeriodoPlanilla periodo = periodoPlanillaRepository.findById(periodoPlanillaId)
                .orElseThrow(() -> new ResourceNotFoundException("Periodo de planilla no encontrado"));

        validarAccesoEmpresa(periodo.getEmpresa().getId());

        return detallePlanillaRepository.findByPeriodoPlanillaId(periodoPlanillaId)
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    public List<DetallePlanillaResponseDTO> listarPorTrabajador(Long trabajadorId) {
        Trabajador trabajador = trabajadorRepository.findById(trabajadorId)
                .orElseThrow(() -> new ResourceNotFoundException("Trabajador no encontrado"));

        validarAccesoEmpresa(trabajador.getEmpresa().getId());

        return detallePlanillaRepository.findByTrabajadorId(trabajadorId)
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    public List<DetallePlanillaResponseDTO> listarPorPeriodoYTrabajador(
            Long periodoPlanillaId,
            Long trabajadorId
    ) {
        PeriodoPlanilla periodo = periodoPlanillaRepository.findById(periodoPlanillaId)
                .orElseThrow(() -> new ResourceNotFoundException("Periodo de planilla no encontrado"));

        Trabajador trabajador = trabajadorRepository.findById(trabajadorId)
                .orElseThrow(() -> new ResourceNotFoundException("Trabajador no encontrado"));

        validarAccesoEmpresa(periodo.getEmpresa().getId());
        validarAccesoEmpresa(trabajador.getEmpresa().getId());

        if (!periodo.getEmpresa().getId().equals(trabajador.getEmpresa().getId())) {
            throw new RuntimeException("El trabajador no pertenece a la empresa del periodo");
        }

        return detallePlanillaRepository.findByPeriodoPlanillaIdAndTrabajadorId(
                        periodoPlanillaId,
                        trabajadorId
                )
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    public ResumenPlanillaTrabajadorDTO calcularResumenTrabajador(
            Long periodoPlanillaId,
            Long trabajadorId
    ) {
        PeriodoPlanilla periodo = periodoPlanillaRepository.findById(periodoPlanillaId)
                .orElseThrow(() -> new ResourceNotFoundException("Periodo de planilla no encontrado"));

        Trabajador trabajador = trabajadorRepository.findById(trabajadorId)
                .orElseThrow(() -> new ResourceNotFoundException("Trabajador no encontrado"));

        validarAccesoEmpresa(periodo.getEmpresa().getId());
        validarAccesoEmpresa(trabajador.getEmpresa().getId());

        if (!periodo.getEmpresa().getId().equals(trabajador.getEmpresa().getId())) {
            throw new RuntimeException("El trabajador no pertenece a la empresa del periodo");
        }

        List<DetallePlanilla> detalles = detallePlanillaRepository
                .findByPeriodoPlanillaIdAndTrabajadorId(periodoPlanillaId, trabajadorId);

        BigDecimal totalIngresos = BigDecimal.ZERO;
        BigDecimal totalDescuentos = BigDecimal.ZERO;

        for (DetallePlanilla detalle : detalles) {
            if ("INGRESO".equals(detalle.getTipo())) {
                totalIngresos = totalIngresos.add(detalle.getMonto());
            }

            if ("DESCUENTO".equals(detalle.getTipo())) {
                totalDescuentos = totalDescuentos.add(detalle.getMonto());
            }
        }

        BigDecimal netoPagar = totalIngresos.subtract(totalDescuentos);

        ResumenPlanillaTrabajadorDTO response = new ResumenPlanillaTrabajadorDTO();

        response.setPeriodoPlanillaId(periodo.getId());
        response.setPeriodoNombre(periodo.getNombre());

        response.setTrabajadorId(trabajador.getId());
        response.setTrabajadorNombres(trabajador.getNombres());
        response.setTrabajadorApellidos(trabajador.getApellidos());
        response.setTrabajadorDocumento(trabajador.getNumeroDocumento());

        response.setTotalIngresos(totalIngresos);
        response.setTotalDescuentos(totalDescuentos);
        response.setNetoPagar(netoPagar);

        return response;
    }

        public ResumenPlanillaPeriodoDTO calcularResumenPeriodo(Long periodoPlanillaId) {
        PeriodoPlanilla periodo = periodoPlanillaRepository.findById(periodoPlanillaId)
                .orElseThrow(() -> new ResourceNotFoundException("Periodo de planilla no encontrado"));

        validarAccesoEmpresa(periodo.getEmpresa().getId());

        List<DetallePlanilla> detalles = detallePlanillaRepository
                .findByPeriodoPlanillaId(periodoPlanillaId);

        List<Long> trabajadorIds = detalles.stream()
                .map(detalle -> detalle.getTrabajador().getId())
                .distinct()
                .toList();

        List<ResumenPlanillaTrabajadorDTO> resumenTrabajadores = trabajadorIds.stream()
                .map(trabajadorId -> calcularResumenTrabajador(periodoPlanillaId, trabajadorId))
                .toList();

        BigDecimal totalIngresos = BigDecimal.ZERO;
        BigDecimal totalDescuentos = BigDecimal.ZERO;
        BigDecimal totalNetoPagar = BigDecimal.ZERO;

        for (ResumenPlanillaTrabajadorDTO trabajador : resumenTrabajadores) {
            totalIngresos = totalIngresos.add(trabajador.getTotalIngresos());
            totalDescuentos = totalDescuentos.add(trabajador.getTotalDescuentos());
            totalNetoPagar = totalNetoPagar.add(trabajador.getNetoPagar());
        }

        ResumenPlanillaPeriodoDTO response = new ResumenPlanillaPeriodoDTO();

        response.setPeriodoPlanillaId(periodo.getId());
        response.setPeriodoNombre(periodo.getNombre());
        response.setTipo(periodo.getTipo());
        response.setEstado(periodo.getEstado());

        response.setTotalIngresos(totalIngresos);
        response.setTotalDescuentos(totalDescuentos);
        response.setTotalNetoPagar(totalNetoPagar);

        response.setCantidadTrabajadores(resumenTrabajadores.size());
        response.setTrabajadores(resumenTrabajadores);

        return response;
    }

    private void validarAccesoEmpresa(Long empresaId) {
        String rolActual = authenticatedUserService.obtenerRolActual();

        if ("SUPER_ADMIN".equals(rolActual)) {
            return;
        }

        Long empresaIdActual = authenticatedUserService.obtenerEmpresaIdActual();

        if (!empresaIdActual.equals(empresaId)) {
            throw new AccessDeniedAppException("No tienes acceso a la información de esta empresa");
        }
    }

    private DetallePlanillaResponseDTO convertirAResponse(DetallePlanilla detalle) {
        DetallePlanillaResponseDTO response = new DetallePlanillaResponseDTO();

        response.setId(detalle.getId());

        response.setPeriodoPlanillaId(detalle.getPeriodoPlanilla().getId());
        response.setPeriodoNombre(detalle.getPeriodoPlanilla().getNombre());

        response.setTrabajadorId(detalle.getTrabajador().getId());
        response.setTrabajadorNombres(detalle.getTrabajador().getNombres());
        response.setTrabajadorApellidos(detalle.getTrabajador().getApellidos());
        response.setTrabajadorDocumento(detalle.getTrabajador().getNumeroDocumento());

        response.setConceptoPlanillaId(detalle.getConceptoPlanilla().getId());
        response.setConceptoCodigo(detalle.getConceptoPlanilla().getCodigo());
        response.setConceptoNombre(detalle.getConceptoPlanilla().getNombre());

        response.setTipo(detalle.getTipo());
        response.setMonto(detalle.getMonto());

        response.setObservacion(detalle.getObservacion());

        response.setCreatedAt(detalle.getCreatedAt());
        response.setUpdatedAt(detalle.getUpdatedAt());

        return response;
    }
}