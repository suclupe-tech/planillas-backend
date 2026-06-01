package com.planillapro.backend.trabajador;

import com.planillapro.backend.trabajador.dto.TrabajadorRequestDTO;
import com.planillapro.backend.trabajador.dto.TrabajadorResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

import java.util.List;

@RestController
@RequestMapping("/api/trabajadores")
public class TrabajadorController {

    private final TrabajadorService trabajadorService;

    public TrabajadorController(TrabajadorService trabajadorService) {
        this.trabajadorService = trabajadorService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TrabajadorResponseDTO crear(@Valid @RequestBody TrabajadorRequestDTO request) {
        return trabajadorService.crear(request);
    }

    @GetMapping
    public List<TrabajadorResponseDTO> listar() {
        return trabajadorService.listar();
    }

    @GetMapping("/{id}")
    public TrabajadorResponseDTO buscarPorId(@PathVariable Long id) {
        return trabajadorService.buscarPorId(id);
    }

    @GetMapping("/empresa/{empresaId}")
    public List<TrabajadorResponseDTO> listarPorEmpresa(@PathVariable Long empresaId) {
        return trabajadorService.listarPorEmpresa(empresaId);
    }

    @PutMapping("/{id}")
    public TrabajadorResponseDTO actualizar(
            @PathVariable Long id,
            @Valid @RequestBody TrabajadorRequestDTO request
    ) {
        return trabajadorService.actualizar(id, request);
    }

    @PatchMapping("/{id}/baja")
    public TrabajadorResponseDTO darDeBaja(
            @PathVariable Long id,
            @RequestParam LocalDate fechaCese
    ) {
        return trabajadorService.darDeBaja(id, fechaCese);
    }

    @PatchMapping("/{id}/reactivar")
    public TrabajadorResponseDTO reactivar(@PathVariable Long id) {
        return trabajadorService.reactivar(id);
    }
}