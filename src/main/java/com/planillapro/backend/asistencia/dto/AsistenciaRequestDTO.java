package com.planillapro.backend.asistencia.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.NotNull;

public class AsistenciaRequestDTO {

    @NotNull(message = "La empresa es obligatoria")
    private Long empresaId;

    @NotNull(message = "El trabajador es obligatorio")
    private Long trabajadorId;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    private LocalTime horaEntrada;
    private LocalTime horaSalida;

    private BigDecimal horasTrabajadas;
    private BigDecimal horasExtra;

    @NotNull(message = "El estado es obligatorio")
    private String estado;

    private String observacion;

    public Long getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
    }

    public Long getTrabajadorId() {
        return trabajadorId;
    }

    public void setTrabajadorId(Long trabajadorId) {
        this.trabajadorId = trabajadorId;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(LocalTime horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public LocalTime getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(LocalTime horaSalida) {
        this.horaSalida = horaSalida;
    }

    public BigDecimal getHorasTrabajadas() {
        return horasTrabajadas;
    }

    public void setHorasTrabajadas(BigDecimal horasTrabajadas) {
        this.horasTrabajadas = horasTrabajadas;
    }

    public BigDecimal getHorasExtra() {
        return horasExtra;
    }

    public void setHorasExtra(BigDecimal horasExtra) {
        this.horasExtra = horasExtra;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
}