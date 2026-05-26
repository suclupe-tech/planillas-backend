package com.planillapro.backend.trabajador;

import com.planillapro.backend.trabajador.dto.TrabajadorRequestDTO;
import com.planillapro.backend.trabajador.dto.TrabajadorResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
}