ALTER TABLE auditoria_planilla
DROP CONSTRAINT fk_auditoria_planilla_detalle;

ALTER TABLE auditoria_planilla
ADD CONSTRAINT fk_auditoria_planilla_detalle
FOREIGN KEY (detalle_planilla_id)
REFERENCES detalle_planilla(id)
ON DELETE SET NULL;