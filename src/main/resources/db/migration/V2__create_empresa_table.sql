CREATE TABLE empresa (
    id BIGSERIAL PRIMARY KEY,
    ruc VARCHAR(11) NOT NULL UNIQUE,
    razon_social VARCHAR(150) NOT NULL,
    nombre_comercial VARCHAR(150),
    direccion VARCHAR(250),
    telefono VARCHAR(20),
    email VARCHAR(120),
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVA',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);