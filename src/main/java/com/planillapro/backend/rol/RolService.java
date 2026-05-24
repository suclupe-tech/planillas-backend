package com.planillapro.backend.rol;

import com.planillapro.backend.rol.dto.RolResponseDTO;
import com.planillapro.backend.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolService {

    private final RolRepository rolRepository;

    public RolService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    public List<RolResponseDTO> listar() {
        return rolRepository.findAll()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    public RolResponseDTO buscarPorId(Long id) {
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado"));

        return convertirAResponse(rol);
    }

    public RolResponseDTO buscarPorNombre(String nombre) {
        Rol rol = rolRepository.findByNombre(nombre)
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado"));

        return convertirAResponse(rol);
    }

    private RolResponseDTO convertirAResponse(Rol rol) {
        RolResponseDTO response = new RolResponseDTO();
        response.setId(rol.getId());
        response.setNombre(rol.getNombre());
        response.setDescripcion(rol.getDescripcion());
        response.setEstado(rol.getEstado());
        response.setCreatedAt(rol.getCreatedAt());
        response.setUpdatedAt(rol.getUpdatedAt());

        return response;
    }
}