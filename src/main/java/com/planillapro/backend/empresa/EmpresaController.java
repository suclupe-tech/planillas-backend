package com.planillapro.backend.empresa;

import com.planillapro.backend.empresa.dto.EmpresaRequestDTO;
import com.planillapro.backend.empresa.dto.EmpresaResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empresas")
public class EmpresaController {

    private final EmpresaService empresaService;

    public EmpresaController(EmpresaService empresaService) {
        this.empresaService = empresaService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmpresaResponseDTO crear(@Valid @RequestBody EmpresaRequestDTO request) {
        return empresaService.crear(request);
    }

    @GetMapping
    public List<EmpresaResponseDTO> listar() {
        return empresaService.listar();
    }

    @GetMapping("/{id}")
    public EmpresaResponseDTO buscarPorId(@PathVariable Long id) {
        return empresaService.buscarPorId(id);
    }
}