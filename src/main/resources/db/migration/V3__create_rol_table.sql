CREATE TABLE rol (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(200),
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

INSERT INTO rol (nombre, descripcion)
VALUES
('SUPER_ADMIN', 'Administrador general del sistema SaaS'),
('ADMIN_EMPRESA', 'Administrador de una empresa cliente'),
('RRHH', 'Usuario encargado de recursos humanos y planillas'),
('CONTADOR', 'Usuario encargado de reportes contables'),
('LECTURA', 'Usuario con acceso solo de consulta');