package com.planillapro.backend.rol;

import com.planillapro.backend.rol.dto.RolResponseDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RolController {

    private final RolService rolService;

    public RolController(RolService rolService) {
        this.rolService = rolService;
    }

    @GetMapping
    public List<RolResponseDTO> listar() {
        return rolService.listar();
    }

    @GetMapping("/{id}")
    public RolResponseDTO buscarPorId(@PathVariable Long id) {
        return rolService.buscarPorId(id);
    }

    @GetMapping("/nombre/{nombre}")
    public RolResponseDTO buscarPorNombre(@PathVariable String nombre) {
        return rolService.buscarPorNombre(nombre);
    }
}