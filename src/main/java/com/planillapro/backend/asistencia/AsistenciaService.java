package com.planillapro.backend.asistencia;

import com.planillapro.backend.asistencia.dto.AsistenciaRequestDTO;
import com.planillapro.backend.asistencia.dto.AsistenciaResponseDTO;
import com.planillapro.backend.trabajador.Trabajador;
import com.planillapro.backend.trabajador.TrabajadorRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

@Service
public class AsistenciaService {

    private final AsistenciaRepository asistenciaRepository;
    private final TrabajadorRepository trabajadorRepository;

    public AsistenciaService(
            AsistenciaRepository asistenciaRepository,
            TrabajadorRepository trabajadorRepository
    ) {
        this.asistenciaRepository = asistenciaRepository;
        this.trabajadorRepository = trabajadorRepository;
    }

    public AsistenciaResponseDTO crear(AsistenciaRequestDTO request) {
        if (asistenciaRepository.existsByTrabajadorIdAndFecha(
                request.getTrabajadorId(),
                request.getFecha()
        )) {
            throw new IllegalArgumentException("Ya existe asistencia registrada para este trabajador en la fecha indicada");
        }

        Trabajador trabajador = trabajadorRepository.findById(request.getTrabajadorId())
                .orElseThrow(() -> new IllegalArgumentException("Trabajador no encontrado"));

        Asistencia asistencia = new Asistencia();
        asistencia.setEmpresaId(request.getEmpresaId());
        asistencia.setTrabajadorId(request.getTrabajadorId());
        asistencia.setFecha(request.getFecha());
        asistencia.setHoraEntrada(request.getHoraEntrada());
        asistencia.setHoraSalida(request.getHoraSalida());
        asistencia.setEstado(request.getEstado());
        asistencia.setObservacion(request.getObservacion());

        calcularHoras(asistencia);

        Asistencia asistenciaGuardada = asistenciaRepository.save(asistencia);

        return convertirAResponse(asistenciaGuardada, trabajador);
    }

    public List<AsistenciaResponseDTO> listarPorEmpresa(Long empresaId) {
        return asistenciaRepository.findByEmpresaIdOrderByFechaDesc(empresaId)
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    public List<AsistenciaResponseDTO> listarPorEmpresaYFechas(
            Long empresaId,
            LocalDate fechaDesde,
            LocalDate fechaHasta
    ) {
        return asistenciaRepository
                .findByEmpresaIdAndFechaBetweenOrderByFechaDesc(empresaId, fechaDesde, fechaHasta)
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    public AsistenciaResponseDTO buscarPorId(Long id) {
        Asistencia asistencia = asistenciaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Asistencia no encontrada"));

        return convertirAResponse(asistencia);
    }

    public AsistenciaResponseDTO actualizar(Long id, AsistenciaRequestDTO request) {
        Asistencia asistencia = asistenciaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Asistencia no encontrada"));

        Trabajador trabajador = trabajadorRepository.findById(request.getTrabajadorId())
                .orElseThrow(() -> new IllegalArgumentException("Trabajador no encontrado"));

        asistencia.setEmpresaId(request.getEmpresaId());
        asistencia.setTrabajadorId(request.getTrabajadorId());
        asistencia.setFecha(request.getFecha());
        asistencia.setHoraEntrada(request.getHoraEntrada());
        asistencia.setHoraSalida(request.getHoraSalida());
        asistencia.setEstado(request.getEstado());
        asistencia.setObservacion(request.getObservacion());

        calcularHoras(asistencia);

        Asistencia asistenciaActualizada = asistenciaRepository.save(asistencia);

        return convertirAResponse(asistenciaActualizada, trabajador);
    }

    public void eliminar(Long id) {
        Asistencia asistencia = asistenciaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Asistencia no encontrada"));

        asistenciaRepository.delete(asistencia);
    }

    private void calcularHoras(Asistencia asistencia) {
        boolean debeCalcularHoras =
                "ASISTIO".equals(asistencia.getEstado()) ||
                "TARDANZA".equals(asistencia.getEstado());

        if (
                asistencia.getHoraEntrada() == null ||
                asistencia.getHoraSalida() == null ||
                !debeCalcularHoras
        ) {
            asistencia.setHorasTrabajadas(BigDecimal.ZERO);
            asistencia.setHorasExtra(BigDecimal.ZERO);
            return;
        }

        long minutos = Duration.between(
                asistencia.getHoraEntrada(),
                asistencia.getHoraSalida()
        ).toMinutes();

        long minutosRefrigerio = 60;

        long minutosTrabajados = minutos - minutosRefrigerio;

        if (minutosTrabajados < 0) {
            minutosTrabajados = 0;
        }

        BigDecimal horasTrabajadas = BigDecimal.valueOf(minutosTrabajados)
                .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);

        BigDecimal jornadaNormal = BigDecimal.valueOf(8);

        BigDecimal horasExtra = horasTrabajadas.compareTo(jornadaNormal) > 0
                ? horasTrabajadas.subtract(jornadaNormal)
                : BigDecimal.ZERO;

        asistencia.setHorasTrabajadas(horasTrabajadas);
        asistencia.setHorasExtra(horasExtra);
    }

    private AsistenciaResponseDTO convertirAResponse(Asistencia asistencia) {
        Trabajador trabajador = trabajadorRepository.findById(asistencia.getTrabajadorId())
                .orElse(null);

        return convertirAResponse(asistencia, trabajador);
    }

    private AsistenciaResponseDTO convertirAResponse(
            Asistencia asistencia,
            Trabajador trabajador
    ) {
        AsistenciaResponseDTO response = new AsistenciaResponseDTO();

        response.setId(asistencia.getId());
        response.setEmpresaId(asistencia.getEmpresaId());
        response.setTrabajadorId(asistencia.getTrabajadorId());

        if (trabajador != null) {
            response.setTrabajadorNombre(
                    trabajador.getNombres() + " " + trabajador.getApellidos()
            );
        } else {
            response.setTrabajadorNombre("Trabajador no encontrado");
        }

        response.setFecha(asistencia.getFecha());
        response.setHoraEntrada(asistencia.getHoraEntrada());
        response.setHoraSalida(asistencia.getHoraSalida());
        response.setHorasTrabajadas(asistencia.getHorasTrabajadas());
        response.setHorasExtra(asistencia.getHorasExtra());
        response.setEstado(asistencia.getEstado());
        response.setObservacion(asistencia.getObservacion());

        return response;
    }
}