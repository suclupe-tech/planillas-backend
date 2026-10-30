package com.planillapro.backend.asistencia;

import com.planillapro.backend.asistencia.dto.AsistenciaRequestDTO;
import com.planillapro.backend.asistencia.dto.AsistenciaResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/asistencias")
public class AsistenciaController {

    private final AsistenciaService asistenciaService;

    public AsistenciaController(AsistenciaService asistenciaService) {
        this.asistenciaService = asistenciaService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AsistenciaResponseDTO crear(@Valid @RequestBody AsistenciaRequestDTO request) {
        return asistenciaService.crear(request);
    }

    @GetMapping("/empresa/{empresaId}")
    public List<AsistenciaResponseDTO> listarPorEmpresa(@PathVariable Long empresaId) {
        return asistenciaService.listarPorEmpresa(empresaId);
    }

    @GetMapping("/empresa/{empresaId}/fechas")
    public List<AsistenciaResponseDTO> listarPorEmpresaYFechas(
            @PathVariable Long empresaId,
            @RequestParam LocalDate fechaDesde,
            @RequestParam LocalDate fechaHasta
    ) {
        return asistenciaService.listarPorEmpresaYFechas(
                empresaId,
                fechaDesde,
                fechaHasta
        );
    }

    @GetMapping("/{id}")
    public AsistenciaResponseDTO buscarPorId(@PathVariable Long id) {
        return asistenciaService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public AsistenciaResponseDTO actualizar(
            @PathVariable Long id,
            @Valid @RequestBody AsistenciaRequestDTO request
    ) {
        return asistenciaService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        asistenciaService.eliminar(id);
    }
}