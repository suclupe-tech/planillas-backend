package com.planillapro.backend.periodo;

import com.planillapro.backend.periodo.dto.PeriodoPlanillaRequestDTO;
import com.planillapro.backend.periodo.dto.PeriodoPlanillaResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/periodos-planilla")
public class PeriodoPlanillaController {

    private final PeriodoPlanillaService periodoPlanillaService;

    public PeriodoPlanillaController(PeriodoPlanillaService periodoPlanillaService) {
        this.periodoPlanillaService = periodoPlanillaService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PeriodoPlanillaResponseDTO crear(@Valid @RequestBody PeriodoPlanillaRequestDTO request) {
        return periodoPlanillaService.crear(request);
    }

    @GetMapping
    public List<PeriodoPlanillaResponseDTO> listar() {
        return periodoPlanillaService.listar();
    }

    @GetMapping("/{id}")
    public PeriodoPlanillaResponseDTO buscarPorId(@PathVariable Long id) {
        return periodoPlanillaService.buscarPorId(id);
    }

    @GetMapping("/empresa/{empresaId}")
    public List<PeriodoPlanillaResponseDTO> listarPorEmpresa(@PathVariable Long empresaId) {
        return periodoPlanillaService.listarPorEmpresa(empresaId);
    }

    @GetMapping("/empresa/{empresaId}/abiertos")
    public List<PeriodoPlanillaResponseDTO> listarAbiertosPorEmpresa(@PathVariable Long empresaId) {
        return periodoPlanillaService.listarAbiertosPorEmpresa(empresaId);
    }

    @PatchMapping("/{id}/cerrar")
    public PeriodoPlanillaResponseDTO cerrar(@PathVariable Long id) {
        return periodoPlanillaService.cerrar(id);
    }
}