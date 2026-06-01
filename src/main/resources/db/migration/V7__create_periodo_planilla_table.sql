CREATE TABLE periodo_planilla (
    id BIGSERIAL PRIMARY KEY,

    empresa_id BIGINT NOT NULL,

    nombre VARCHAR(120) NOT NULL,
    tipo VARCHAR(30) NOT NULL,

    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL,

    estado VARCHAR(20) NOT NULL DEFAULT 'ABIERTO',

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,

    CONSTRAINT fk_periodo_planilla_empresa
        FOREIGN KEY (empresa_id)
        REFERENCES empresa(id),

    CONSTRAINT uk_periodo_planilla_empresa_fechas
        UNIQUE (empresa_id, tipo, fecha_inicio, fecha_fin)
);