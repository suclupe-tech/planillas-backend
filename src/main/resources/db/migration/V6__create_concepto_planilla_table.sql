CREATE TABLE concepto_planilla (
    id BIGSERIAL PRIMARY KEY,

    codigo VARCHAR(50) NOT NULL UNIQUE,
    nombre VARCHAR(120) NOT NULL,
    descripcion VARCHAR(250),

    tipo VARCHAR(30) NOT NULL,
    formula VARCHAR(250),

    es_remunerativo BOOLEAN NOT NULL DEFAULT FALSE,
    afecta_afp_onp BOOLEAN NOT NULL DEFAULT FALSE,
    afecta_essalud BOOLEAN NOT NULL DEFAULT FALSE,

    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO',

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

INSERT INTO concepto_planilla (
    codigo,
    nombre,
    descripcion,
    tipo,
    formula,
    es_remunerativo,
    afecta_afp_onp,
    afecta_essalud
)
VALUES
('SUELDO_BASICO', 'Sueldo básico', 'Remuneración base mensual del trabajador', 'INGRESO', NULL, TRUE, TRUE, TRUE),
('ASIGNACION_FAMILIAR', 'Asignación familiar', 'Beneficio laboral por carga familiar', 'INGRESO', NULL, TRUE, TRUE, TRUE),
('HORAS_EXTRA', 'Horas extra', 'Pago adicional por horas trabajadas fuera de jornada', 'INGRESO', NULL, TRUE, TRUE, TRUE),
('BONIFICACION', 'Bonificación', 'Ingreso adicional otorgado al trabajador', 'INGRESO', NULL, TRUE, TRUE, TRUE),

('DESCUENTO_TARDANZA', 'Descuento por tardanza', 'Descuento aplicado por tardanzas', 'DESCUENTO', NULL, FALSE, FALSE, FALSE),
('ADELANTO', 'Adelanto', 'Descuento por adelanto de sueldo', 'DESCUENTO', NULL, FALSE, FALSE, FALSE),
('AFP', 'AFP', 'Descuento por fondo privado de pensiones', 'DESCUENTO', NULL, FALSE, FALSE, FALSE),
('ONP', 'ONP', 'Descuento por sistema nacional de pensiones', 'DESCUENTO', NULL, FALSE, FALSE, FALSE),

('ESSALUD', 'EsSalud', 'Aporte del empleador a EsSalud', 'APORTE_EMPLEADOR', NULL, FALSE, FALSE, FALSE);