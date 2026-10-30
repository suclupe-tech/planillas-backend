package com.planillapro.backend.empresa;

import com.planillapro.backend.security.AuthenticatedUserService;
import com.planillapro.backend.shared.exception.BusinessException;
import com.planillapro.backend.empresa.dto.EmpresaRequestDTO;
import com.planillapro.backend.empresa.dto.EmpresaResponseDTO;
import org.springframework.stereotype.Service;
import com.planillapro.backend.shared.exception.ResourceNotFoundException;

import java.util.List;

@Service
public class EmpresaService {

    private final EmpresaRepository empresaRepository;
    private final AuthenticatedUserService authenticatedUserService;

    public EmpresaService(
            EmpresaRepository empresaRepository,
            AuthenticatedUserService authenticatedUserService
    ) {
        this.empresaRepository = empresaRepository;
        this.authenticatedUserService = authenticatedUserService;
    }

    public EmpresaResponseDTO crear(EmpresaRequestDTO request) {
        if (empresaRepository.existsByRuc(request.getRuc())) {
            throw new BusinessException("Ya existe una empresa registrada con ese RUC");
        }

        Empresa empresa = new Empresa();
        empresa.setRuc(request.getRuc());
        empresa.setRazonSocial(request.getRazonSocial());
        empresa.setNombreComercial(request.getNombreComercial());
        empresa.setDireccion(request.getDireccion());
        empresa.setTelefono(request.getTelefono());
        empresa.setEmail(request.getEmail());
        empresa.setEstado("ACTIVA");

        Empresa empresaGuardada = empresaRepository.save(empresa);

        return convertirAResponse(empresaGuardada);
    }

    public List<EmpresaResponseDTO> listar() {
        List<Empresa> empresas = authenticatedUserService.esSuperAdmin()
                ? empresaRepository.findAll()
                : empresaRepository.findById(authenticatedUserService.obtenerEmpresaIdActual())
                        .map(List::of)
                        .orElse(List.of());

        return empresas
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    public EmpresaResponseDTO buscarPorId(Long id) {
        authenticatedUserService.validarAccesoEmpresa(id);

        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada"));

        return convertirAResponse(empresa);
    }

    private EmpresaResponseDTO convertirAResponse(Empresa empresa) {
        EmpresaResponseDTO response = new EmpresaResponseDTO();
        response.setId(empresa.getId());
        response.setRuc(empresa.getRuc());
        response.setRazonSocial(empresa.getRazonSocial());
        response.setNombreComercial(empresa.getNombreComercial());
        response.setDireccion(empresa.getDireccion());
        response.setTelefono(empresa.getTelefono());
        response.setEmail(empresa.getEmail());
        response.setEstado(empresa.getEstado());
        response.setCreatedAt(empresa.getCreatedAt());
        response.setUpdatedAt(empresa.getUpdatedAt());

        return response;
    }
}