CREATE TABLE detalle_planilla (
    id BIGSERIAL PRIMARY KEY,

    periodo_planilla_id BIGINT NOT NULL,
    trabajador_id BIGINT NOT NULL,
    concepto_planilla_id BIGINT NOT NULL,

    tipo VARCHAR(30) NOT NULL,
    monto NUMERIC(12,2) NOT NULL DEFAULT 0,

    observacion VARCHAR(250),

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,

    CONSTRAINT fk_detalle_planilla_periodo
        FOREIGN KEY (periodo_planilla_id)
        REFERENCES periodo_planilla(id),

    CONSTRAINT fk_detalle_planilla_trabajador
        FOREIGN KEY (trabajador_id)
        REFERENCES trabajador(id),

    CONSTRAINT fk_detalle_planilla_concepto
        FOREIGN KEY (concepto_planilla_id)
        REFERENCES concepto_planilla(id),

    CONSTRAINT uk_detalle_planilla_periodo_trabajador_concepto
        UNIQUE (periodo_planilla_id, trabajador_id, concepto_planilla_id)
);