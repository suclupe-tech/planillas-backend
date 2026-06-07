package com.planillapro.backend.planilla;

import com.planillapro.backend.empresa.Empresa;
import com.planillapro.backend.periodo.PeriodoPlanilla;
import com.planillapro.backend.planilla.dto.AuditoriaPlanillaResponseDTO;
import com.planillapro.backend.security.AuthenticatedUserService;
import com.planillapro.backend.trabajador.Trabajador;
import com.planillapro.backend.usuario.Usuario;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditoriaPlanillaService {

    private final AuditoriaPlanillaRepository auditoriaPlanillaRepository;
    private final AuthenticatedUserService authenticatedUserService;

    public AuditoriaPlanillaService(
            AuditoriaPlanillaRepository auditoriaPlanillaRepository,
            AuthenticatedUserService authenticatedUserService
    ) {
        this.auditoriaPlanillaRepository = auditoriaPlanillaRepository;
        this.authenticatedUserService = authenticatedUserService;
    }

    public void registrar(
            Empresa empresa,
            PeriodoPlanilla periodoPlanilla,
            Trabajador trabajador,
            DetallePlanilla detallePlanilla,
            String accion,
            String descripcion
    ) {
        Usuario usuarioActual = authenticatedUserService.obtenerUsuarioActual();

        AuditoriaPlanilla auditoria = new AuditoriaPlanilla();
        auditoria.setUsuario(usuarioActual);
        auditoria.setEmpresa(empresa);
        auditoria.setPeriodoPlanilla(periodoPlanilla);
        auditoria.setTrabajador(trabajador);
        auditoria.setDetallePlanilla(detallePlanilla);
        auditoria.setAccion(accion);
        auditoria.setDescripcion(descripcion);

        auditoriaPlanillaRepository.save(auditoria);
    }

    public List<AuditoriaPlanillaResponseDTO> listarPorPeriodo(Long periodoPlanillaId) {
        return auditoriaPlanillaRepository
                .findByPeriodoPlanillaIdOrderByFechaHoraDesc(periodoPlanillaId)
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    public List<AuditoriaPlanillaResponseDTO> listarPorEmpresaActual() {
        Long empresaIdActual = authenticatedUserService.obtenerEmpresaIdActual();

        return auditoriaPlanillaRepository
                .findByEmpresaIdOrderByFechaHoraDesc(empresaIdActual)
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    public List<AuditoriaPlanillaResponseDTO> listarPorAccion(String accion) {
        Long empresaIdActual = authenticatedUserService.obtenerEmpresaIdActual();

        return auditoriaPlanillaRepository
                .findByEmpresaIdAndAccionOrderByFechaHoraDesc(empresaIdActual, accion)
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    public List<AuditoriaPlanillaResponseDTO> listarPorPeriodoYAccion(
            Long periodoPlanillaId,
            String accion
    ) {
        return auditoriaPlanillaRepository
                .findByPeriodoPlanillaIdAndAccionOrderByFechaHoraDesc(periodoPlanillaId, accion)
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    private AuditoriaPlanillaResponseDTO convertirAResponse(AuditoriaPlanilla auditoria) {
        AuditoriaPlanillaResponseDTO response = new AuditoriaPlanillaResponseDTO();

        response.setId(auditoria.getId());

        response.setUsuarioId(auditoria.getUsuario().getId());
        response.setUsuarioNombreCompleto(
                auditoria.getUsuario().getNombres() + " " + auditoria.getUsuario().getApellidos()
        );
        response.setUsuarioEmail(auditoria.getUsuario().getEmail());

        response.setEmpresaId(auditoria.getEmpresa().getId());
        response.setEmpresaRazonSocial(auditoria.getEmpresa().getRazonSocial());

        if (auditoria.getPeriodoPlanilla() != null) {
            response.setPeriodoPlanillaId(auditoria.getPeriodoPlanilla().getId());
            response.setPeriodoNombre(auditoria.getPeriodoPlanilla().getNombre());
        }

        if (auditoria.getTrabajador() != null) {
            response.setTrabajadorId(auditoria.getTrabajador().getId());
            response.setTrabajadorNombreCompleto(
                    auditoria.getTrabajador().getNombres() + " " + auditoria.getTrabajador().getApellidos()
            );
            response.setTrabajadorDocumento(auditoria.getTrabajador().getNumeroDocumento());
        }

        if (auditoria.getDetallePlanilla() != null) {
            response.setDetallePlanillaId(auditoria.getDetallePlanilla().getId());
        }

        response.setAccion(auditoria.getAccion());
        response.setDescripcion(auditoria.getDescripcion());
        response.setFechaHora(auditoria.getFechaHora());

        return response;
    }
}