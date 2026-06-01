package com.planillapro.backend.concepto;

import com.planillapro.backend.concepto.dto.ConceptoPlanillaResponseDTO;
import com.planillapro.backend.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConceptoPlanillaService {

    private final ConceptoPlanillaRepository conceptoPlanillaRepository;

    public ConceptoPlanillaService(ConceptoPlanillaRepository conceptoPlanillaRepository) {
        this.conceptoPlanillaRepository = conceptoPlanillaRepository;
    }

    public List<ConceptoPlanillaResponseDTO> listar() {
        return conceptoPlanillaRepository.findAll()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    public List<ConceptoPlanillaResponseDTO> listarActivos() {
        return conceptoPlanillaRepository.findByEstado("ACTIVO")
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    public List<ConceptoPlanillaResponseDTO> listarPorTipo(String tipo) {
        return conceptoPlanillaRepository.findByTipo(tipo)
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    public ConceptoPlanillaResponseDTO buscarPorId(Long id) {
        ConceptoPlanilla concepto = conceptoPlanillaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Concepto de planilla no encontrado"));

        return convertirAResponse(concepto);
    }

    public ConceptoPlanillaResponseDTO buscarPorCodigo(String codigo) {
        ConceptoPlanilla concepto = conceptoPlanillaRepository.findByCodigo(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("Concepto de planilla no encontrado"));

        return convertirAResponse(concepto);
    }

    private ConceptoPlanillaResponseDTO convertirAResponse(ConceptoPlanilla concepto) {
        ConceptoPlanillaResponseDTO response = new ConceptoPlanillaResponseDTO();

        response.setId(concepto.getId());
        response.setCodigo(concepto.getCodigo());
        response.setNombre(concepto.getNombre());
        response.setDescripcion(concepto.getDescripcion());

        response.setTipo(concepto.getTipo());
        response.setFormula(concepto.getFormula());

        response.setEsRemunerativo(concepto.getEsRemunerativo());
        response.setAfectaAfpOnp(concepto.getAfectaAfpOnp());
        response.setAfectaEssalud(concepto.getAfectaEssalud());

        response.setEstado(concepto.getEstado());

        response.setCreatedAt(concepto.getCreatedAt());
        response.setUpdatedAt(concepto.getUpdatedAt());

        return response;
    }
}