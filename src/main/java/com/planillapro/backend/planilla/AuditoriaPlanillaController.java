package com.planillapro.backend.planilla;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.planillapro.backend.planilla.dto.AuditoriaPlanillaResponseDTO;

@RestController
@RequestMapping("/api/auditoria-planilla")
public class AuditoriaPlanillaController {

    private final AuditoriaPlanillaService auditoriaPlanillaService;

    public AuditoriaPlanillaController(AuditoriaPlanillaService auditoriaPlanillaService) {
        this.auditoriaPlanillaService = auditoriaPlanillaService;
    }

    @GetMapping("/periodo/{periodoPlanillaId}")
    public List<AuditoriaPlanillaResponseDTO> listarPorPeriodo(
            @PathVariable Long periodoPlanillaId
    ) {
        return auditoriaPlanillaService.listarPorPeriodo(periodoPlanillaId);
    }

    @GetMapping("/empresa-actual")
    public List<AuditoriaPlanillaResponseDTO> listarPorEmpresaActual() {
        return auditoriaPlanillaService.listarPorEmpresaActual();
    }

    @GetMapping("/accion/{accion}")
    public List<AuditoriaPlanillaResponseDTO> listarPorAccion(
            @PathVariable String accion
    ) {
        return auditoriaPlanillaService.listarPorAccion(accion);
    }

    @GetMapping("/periodo/{periodoPlanillaId}/accion/{accion}")
    public List<AuditoriaPlanillaResponseDTO> listarPorPeriodoYAccion(
            @PathVariable Long periodoPlanillaId,
            @PathVariable String accion
    ) {
        return auditoriaPlanillaService.listarPorPeriodoYAccion(
                periodoPlanillaId,
                accion
        );
    }

    @GetMapping("/trabajador/{trabajadorId}")
    public List<AuditoriaPlanillaResponseDTO> listarPorTrabajador(
            @PathVariable Long trabajadorId
    ) {
        return auditoriaPlanillaService.listarPorTrabajador(trabajadorId);
    }
}