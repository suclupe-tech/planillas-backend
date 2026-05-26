package com.planillapro.backend.trabajador;

import com.planillapro.backend.empresa.Empresa;
import com.planillapro.backend.empresa.EmpresaRepository;
import com.planillapro.backend.shared.exception.ResourceNotFoundException;
import com.planillapro.backend.trabajador.dto.TrabajadorRequestDTO;
import com.planillapro.backend.trabajador.dto.TrabajadorResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrabajadorService {

    private final TrabajadorRepository trabajadorRepository;
    private final EmpresaRepository empresaRepository;

    public TrabajadorService(
            TrabajadorRepository trabajadorRepository,
            EmpresaRepository empresaRepository
    ) {
        this.trabajadorRepository = trabajadorRepository;
        this.empresaRepository = empresaRepository;
    }

    public TrabajadorResponseDTO crear(TrabajadorRequestDTO request) {
        if (trabajadorRepository.existsByEmpresaIdAndTipoDocumentoAndNumeroDocumento(
                request.getEmpresaId(),
                request.getTipoDocumento(),
                request.getNumeroDocumento()
        )) {
            throw new RuntimeException("Ya existe un trabajador registrado con ese documento en esta empresa");
        }

        Empresa empresa = empresaRepository.findById(request.getEmpresaId())
                .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada"));

        Trabajador trabajador = new Trabajador();
        trabajador.setEmpresa(empresa);
        trabajador.setTipoDocumento(request.getTipoDocumento());
        trabajador.setNumeroDocumento(request.getNumeroDocumento());
        trabajador.setNombres(request.getNombres());
        trabajador.setApellidos(request.getApellidos());
        trabajador.setFechaNacimiento(request.getFechaNacimiento());
        trabajador.setTelefono(request.getTelefono());
        trabajador.setEmail(request.getEmail());
        trabajador.setDireccion(request.getDireccion());
        trabajador.setFechaIngreso(request.getFechaIngreso());
        trabajador.setFechaCese(request.getFechaCese());
        trabajador.setCargo(request.getCargo());
        trabajador.setArea(request.getArea());
        trabajador.setTipoContrato(request.getTipoContrato());
        trabajador.setRegimenLaboral(request.getRegimenLaboral());
        trabajador.setSueldoBase(request.getSueldoBase());
        trabajador.setEstado("ACTIVO");

        Trabajador trabajadorGuardado = trabajadorRepository.save(trabajador);

        return convertirAResponse(trabajadorGuardado);
    }

    public List<TrabajadorResponseDTO> listar() {
        return trabajadorRepository.findAll()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    public List<TrabajadorResponseDTO> listarPorEmpresa(Long empresaId) {
        return trabajadorRepository.findByEmpresaId(empresaId)
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    public TrabajadorResponseDTO buscarPorId(Long id) {
        Trabajador trabajador = trabajadorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trabajador no encontrado"));

        return convertirAResponse(trabajador);
    }

    private TrabajadorResponseDTO convertirAResponse(Trabajador trabajador) {
        TrabajadorResponseDTO response = new TrabajadorResponseDTO();

        response.setId(trabajador.getId());

        response.setEmpresaId(trabajador.getEmpresa().getId());
        response.setEmpresaRazonSocial(trabajador.getEmpresa().getRazonSocial());

        response.setTipoDocumento(trabajador.getTipoDocumento());
        response.setNumeroDocumento(trabajador.getNumeroDocumento());

        response.setNombres(trabajador.getNombres());
        response.setApellidos(trabajador.getApellidos());

        response.setFechaNacimiento(trabajador.getFechaNacimiento());
        response.setTelefono(trabajador.getTelefono());
        response.setEmail(trabajador.getEmail());
        response.setDireccion(trabajador.getDireccion());

        response.setFechaIngreso(trabajador.getFechaIngreso());
        response.setFechaCese(trabajador.getFechaCese());

        response.setCargo(trabajador.getCargo());
        response.setArea(trabajador.getArea());

        response.setTipoContrato(trabajador.getTipoContrato());
        response.setRegimenLaboral(trabajador.getRegimenLaboral());

        response.setSueldoBase(trabajador.getSueldoBase());

        response.setEstado(trabajador.getEstado());

        response.setCreatedAt(trabajador.getCreatedAt());
        response.setUpdatedAt(trabajador.getUpdatedAt());

        return response;
    }
}