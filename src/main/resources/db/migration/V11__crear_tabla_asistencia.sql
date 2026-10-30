CREATE TABLE asistencias (
    id BIGSERIAL PRIMARY KEY,

    empresa_id BIGINT NOT NULL,
    trabajador_id BIGINT NOT NULL,

    fecha DATE NOT NULL,

    hora_entrada TIME,
    hora_salida TIME,

    horas_trabajadas NUMERIC(5,2) NOT NULL DEFAULT 0,
    horas_extra NUMERIC(5,2) NOT NULL DEFAULT 0,

    estado VARCHAR(20) NOT NULL,

    observacion VARCHAR(255),

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,

    CONSTRAINT fk_asistencia_empresa
        FOREIGN KEY (empresa_id)
        REFERENCES empresa(id),

    CONSTRAINT fk_asistencia_trabajador
        FOREIGN KEY (trabajador_id)
        REFERENCES trabajador(id)
);