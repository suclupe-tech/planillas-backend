package com.planillapro.backend.concepto;

import com.planillapro.backend.concepto.dto.ConceptoPlanillaResponseDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conceptos-planilla")
public class ConceptoPlanillaController {

    private final ConceptoPlanillaService conceptoPlanillaService;

    public ConceptoPlanillaController(ConceptoPlanillaService conceptoPlanillaService) {
        this.conceptoPlanillaService = conceptoPlanillaService;
    }

    @GetMapping
    public List<ConceptoPlanillaResponseDTO> listar() {
        return conceptoPlanillaService.listar();
    }

    @GetMapping("/activos")
    public List<ConceptoPlanillaResponseDTO> listarActivos() {
        return conceptoPlanillaService.listarActivos();
    }

    @GetMapping("/tipo/{tipo}")
    public List<ConceptoPlanillaResponseDTO> listarPorTipo(@PathVariable String tipo) {
        return conceptoPlanillaService.listarPorTipo(tipo);
    }

    @GetMapping("/{id}")
    public ConceptoPlanillaResponseDTO buscarPorId(@PathVariable Long id) {
        return conceptoPlanillaService.buscarPorId(id);
    }

    @GetMapping("/codigo/{codigo}")
    public ConceptoPlanillaResponseDTO buscarPorCodigo(@PathVariable String codigo) {
        return conceptoPlanillaService.buscarPorCodigo(codigo);
    }
}