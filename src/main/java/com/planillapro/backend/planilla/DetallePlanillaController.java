package com.planillapro.backend.planilla;

import com.planillapro.backend.planilla.dto.DetallePlanillaRequestDTO;
import com.planillapro.backend.planilla.dto.DetallePlanillaResponseDTO;
import com.planillapro.backend.planilla.dto.ResumenPlanillaPeriodoDTO;
import com.planillapro.backend.planilla.dto.ResumenPlanillaTrabajadorDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/detalles-planilla")
public class DetallePlanillaController {

    private final DetallePlanillaService detallePlanillaService;

    public DetallePlanillaController(DetallePlanillaService detallePlanillaService) {
        this.detallePlanillaService = detallePlanillaService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DetallePlanillaResponseDTO crear(@Valid @RequestBody DetallePlanillaRequestDTO request) {
        return detallePlanillaService.crear(request);
    }

    @GetMapping("/periodo/{periodoPlanillaId}")
    public List<DetallePlanillaResponseDTO> listarPorPeriodo(
            @PathVariable Long periodoPlanillaId
    ) {
        return detallePlanillaService.listarPorPeriodo(periodoPlanillaId);
    }

    @GetMapping("/trabajador/{trabajadorId}")
    public List<DetallePlanillaResponseDTO> listarPorTrabajador(
            @PathVariable Long trabajadorId
    ) {
        return detallePlanillaService.listarPorTrabajador(trabajadorId);
    }

    @GetMapping("/periodo/{periodoPlanillaId}/trabajador/{trabajadorId}")
    public List<DetallePlanillaResponseDTO> listarPorPeriodoYTrabajador(
            @PathVariable Long periodoPlanillaId,
            @PathVariable Long trabajadorId
    ) {
        return detallePlanillaService.listarPorPeriodoYTrabajador(
                periodoPlanillaId,
                trabajadorId
        );
    }

    @GetMapping("/periodo/{periodoPlanillaId}/trabajador/{trabajadorId}/resumen")
    public ResumenPlanillaTrabajadorDTO calcularResumenTrabajador(
            @PathVariable Long periodoPlanillaId,
            @PathVariable Long trabajadorId
    ) {
        return detallePlanillaService.calcularResumenTrabajador(
                periodoPlanillaId,
                trabajadorId
        );
    }

    @GetMapping("/periodo/{periodoPlanillaId}/resumen")
    public ResumenPlanillaPeriodoDTO calcularResumenPeriodo(
            @PathVariable Long periodoPlanillaId
    ) {
        return detallePlanillaService.calcularResumenPeriodo(periodoPlanillaId);
    }
}