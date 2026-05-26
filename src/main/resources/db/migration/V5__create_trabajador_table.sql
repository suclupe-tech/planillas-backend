CREATE TABLE trabajador (
    id BIGSERIAL PRIMARY KEY,

    empresa_id BIGINT NOT NULL,

    tipo_documento VARCHAR(20) NOT NULL,
    numero_documento VARCHAR(20) NOT NULL,

    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,

    fecha_nacimiento DATE,
    telefono VARCHAR(20),
    email VARCHAR(120),
    direccion VARCHAR(250),

    fecha_ingreso DATE NOT NULL,
    fecha_cese DATE,

    cargo VARCHAR(100),
    area VARCHAR(100),

    tipo_contrato VARCHAR(50),
    regimen_laboral VARCHAR(50),

    sueldo_base NUMERIC(12,2) NOT NULL DEFAULT 0,

    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO',

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,

    CONSTRAINT fk_trabajador_empresa
        FOREIGN KEY (empresa_id)
        REFERENCES empresa(id),

    CONSTRAINT uk_trabajador_empresa_documento
        UNIQUE (empresa_id, tipo_documento, numero_documento)
);