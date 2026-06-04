CREATE TABLE auditoria_planilla (
    id BIGSERIAL PRIMARY KEY,

    usuario_id BIGINT NOT NULL,
    empresa_id BIGINT NOT NULL,

    periodo_planilla_id BIGINT,
    trabajador_id BIGINT,
    detalle_planilla_id BIGINT,

    accion VARCHAR(60) NOT NULL,
    descripcion VARCHAR(500),

    fecha_hora TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_auditoria_planilla_usuario
        FOREIGN KEY (usuario_id)
        REFERENCES usuario(id),

    CONSTRAINT fk_auditoria_planilla_empresa
        FOREIGN KEY (empresa_id)
        REFERENCES empresa(id),

    CONSTRAINT fk_auditoria_planilla_periodo
        FOREIGN KEY (periodo_planilla_id)
        REFERENCES periodo_planilla(id),

    CONSTRAINT fk_auditoria_planilla_trabajador
        FOREIGN KEY (trabajador_id)
        REFERENCES trabajador(id),

    CONSTRAINT fk_auditoria_planilla_detalle
        FOREIGN KEY (detalle_planilla_id)
        REFERENCES detalle_planilla(id)
);