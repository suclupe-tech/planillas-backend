package com.planillapro.backend.periodo;

import com.planillapro.backend.empresa.Empresa;
import com.planillapro.backend.empresa.EmpresaRepository;
import com.planillapro.backend.security.AuthenticatedUserService;
import com.planillapro.backend.shared.exception.AccessDeniedAppException;
import com.planillapro.backend.shared.exception.ResourceNotFoundException;
import com.planillapro.backend.periodo.dto.PeriodoPlanillaRequestDTO;
import com.planillapro.backend.periodo.dto.PeriodoPlanillaResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PeriodoPlanillaService {

    private final PeriodoPlanillaRepository periodoPlanillaRepository;
    private final EmpresaRepository empresaRepository;
    private final AuthenticatedUserService authenticatedUserService;

    public PeriodoPlanillaService(
            PeriodoPlanillaRepository periodoPlanillaRepository,
            EmpresaRepository empresaRepository,
            AuthenticatedUserService authenticatedUserService
    ) {
        this.periodoPlanillaRepository = periodoPlanillaRepository;
        this.empresaRepository = empresaRepository;
        this.authenticatedUserService = authenticatedUserService;
    }

    public PeriodoPlanillaResponseDTO crear(PeriodoPlanillaRequestDTO request) {
        validarAccesoEmpresa(request.getEmpresaId());

        if (request.getFechaFin().isBefore(request.getFechaInicio())) {
            throw new RuntimeException("La fecha fin no puede ser menor que la fecha de inicio");
        }

        if (periodoPlanillaRepository.existsByEmpresaIdAndTipoAndFechaInicioAndFechaFin(
                request.getEmpresaId(),
                request.getTipo(),
                request.getFechaInicio(),
                request.getFechaFin()
        )) {
            throw new RuntimeException("Ya existe un periodo de planilla con esas fechas para esta empresa");
        }

        Empresa empresa = empresaRepository.findById(request.getEmpresaId())
                .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada"));

        PeriodoPlanilla periodo = new PeriodoPlanilla();
        periodo.setEmpresa(empresa);
        periodo.setNombre(request.getNombre());
        periodo.setTipo(request.getTipo());
        periodo.setFechaInicio(request.getFechaInicio());
        periodo.setFechaFin(request.getFechaFin());
        periodo.setEstado("ABIERTO");

        PeriodoPlanilla periodoGuardado = periodoPlanillaRepository.save(periodo);

        return convertirAResponse(periodoGuardado);
    }

    public List<PeriodoPlanillaResponseDTO> listar() {
        String rolActual = authenticatedUserService.obtenerRolActual();

        if ("SUPER_ADMIN".equals(rolActual)) {
            return periodoPlanillaRepository.findAll()
                    .stream()
                    .map(this::convertirAResponse)
                    .toList();
        }

        Long empresaIdActual = authenticatedUserService.obtenerEmpresaIdActual();

        return periodoPlanillaRepository.findByEmpresaId(empresaIdActual)
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    public List<PeriodoPlanillaResponseDTO> listarPorEmpresa(Long empresaId) {
        validarAccesoEmpresa(empresaId);

        return periodoPlanillaRepository.findByEmpresaId(empresaId)
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    public List<PeriodoPlanillaResponseDTO> listarAbiertosPorEmpresa(Long empresaId) {
        validarAccesoEmpresa(empresaId);

        return periodoPlanillaRepository.findByEmpresaIdAndEstado(empresaId, "ABIERTO")
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    public PeriodoPlanillaResponseDTO buscarPorId(Long id) {
        PeriodoPlanilla periodo = periodoPlanillaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Periodo de planilla no encontrado"));

        validarAccesoEmpresa(periodo.getEmpresa().getId());

        return convertirAResponse(periodo);
    }

    public PeriodoPlanillaResponseDTO cerrar(Long id) {
        PeriodoPlanilla periodo = periodoPlanillaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Periodo de planilla no encontrado"));

        validarAccesoEmpresa(periodo.getEmpresa().getId());

        if ("CERRADO".equals(periodo.getEstado())) {
            throw new RuntimeException("El periodo de planilla ya se encuentra cerrado");
        }

        periodo.setEstado("CERRADO");

        PeriodoPlanilla periodoActualizado = periodoPlanillaRepository.save(periodo);

        return convertirAResponse(periodoActualizado);
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

    private PeriodoPlanillaResponseDTO convertirAResponse(PeriodoPlanilla periodo) {
        PeriodoPlanillaResponseDTO response = new PeriodoPlanillaResponseDTO();

        response.setId(periodo.getId());

        response.setEmpresaId(periodo.getEmpresa().getId());
        response.setEmpresaRazonSocial(periodo.getEmpresa().getRazonSocial());

        response.setNombre(periodo.getNombre());
        response.setTipo(periodo.getTipo());

        response.setFechaInicio(periodo.getFechaInicio());
        response.setFechaFin(periodo.getFechaFin());

        response.setEstado(periodo.getEstado());

        response.setCreatedAt(periodo.getCreatedAt());
        response.setUpdatedAt(periodo.getUpdatedAt());

        return response;
    }
}