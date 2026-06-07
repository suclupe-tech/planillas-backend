# PlanillaPro Backend

Backend del sistema PlanillaPro desarrollado con Spring Boot y PostgreSQL.

## Tecnologías usadas

- Java 21
- Spring Boot
- PostgreSQL
- Spring Security
- JWT
- Flyway
- JPA / Hibernate
- Lombok
- OpenPDF
- Apache POI

## Módulos implementados

- Autenticación con JWT
- Empresas
- Roles
- Usuarios
- Trabajadores
- Conceptos de planilla
- Periodos de planilla
- Detalles de planilla
- Generación automática de planilla
- Cierre y bloqueo de planilla
- Boleta individual PDF
- Reporte general PDF
- Reporte general Excel
- Auditoría de acciones
- Dashboard de planillas

---

## Autenticación

```http
POST /api/auth/login
```

Ejemplo de login:

```json
{
  "email": "cesar@planillapro.com",
  "password": "123456"
}
```

---

## Empresas

```http
GET /api/empresas
GET /api/empresas/{id}
POST /api/empresas
PUT /api/empresas/{id}
DELETE /api/empresas/{id}
```

Permisos:

- SUPER_ADMIN
- ADMIN_EMPRESA

---

## Usuarios

```http
GET /api/usuarios
GET /api/usuarios/{id}
POST /api/usuarios
PUT /api/usuarios/{id}
DELETE /api/usuarios/{id}
```

Permisos:

- SUPER_ADMIN
- ADMIN_EMPRESA

---

## Trabajadores

```http
GET /api/trabajadores
GET /api/trabajadores/{id}
POST /api/trabajadores
PUT /api/trabajadores/{id}
DELETE /api/trabajadores/{id}
```

Permisos:

- SUPER_ADMIN
- ADMIN_EMPRESA
- RRHH

---

## Conceptos de planilla

```http
GET /api/conceptos-planilla
GET /api/conceptos-planilla/activos
GET /api/conceptos-planilla/tipo/{tipo}
GET /api/conceptos-planilla/{id}
GET /api/conceptos-planilla/codigo/{codigo}
```

Conceptos principales:

- SUELDO_BASICO
- ASIGNACION_FAMILIAR
- HORAS_EXTRA
- BONIFICACION
- DESCUENTO_TARDANZA
- ADELANTO
- AFP
- ONP
- ESSALUD

---

## Periodos de planilla

```http
POST /api/periodos-planilla
GET /api/periodos-planilla
GET /api/periodos-planilla/{id}
GET /api/periodos-planilla/empresa/{empresaId}
GET /api/periodos-planilla/empresa/{empresaId}/abiertos
PATCH /api/periodos-planilla/{id}/cerrar
```

Estados:

- ABIERTO
- CERRADO

Reglas:

- Una planilla cerrada no permite crear, actualizar ni eliminar detalles.
- Al cerrar una planilla se registra auditoría.

---

## Detalles de planilla

```http
POST /api/detalles-planilla
PUT /api/detalles-planilla/{id}
DELETE /api/detalles-planilla/{id}
GET /api/detalles-planilla/periodo/{periodoPlanillaId}
GET /api/detalles-planilla/trabajador/{trabajadorId}
GET /api/detalles-planilla/periodo/{periodoPlanillaId}/trabajador/{trabajadorId}
```

Reglas:

- No se puede duplicar el mismo concepto para el mismo trabajador dentro del mismo periodo.
- El tipo del detalle se obtiene desde el concepto de planilla.
- Toda creación, actualización y eliminación registra auditoría.

---

## Resúmenes de planilla

### Resumen por trabajador

```http
GET /api/detalles-planilla/periodo/{periodoPlanillaId}/trabajador/{trabajadorId}/resumen
```

Devuelve:

- Total ingresos
- Total descuentos
- Neto a pagar

### Resumen por periodo

```http
GET /api/detalles-planilla/periodo/{periodoPlanillaId}/resumen
```

Devuelve:

- Total ingresos
- Total descuentos
- Total neto a pagar
- Cantidad de trabajadores
- Detalle por trabajador

---

## Generación automática de planilla

```http
POST /api/detalles-planilla/periodo/{periodoPlanillaId}/generar
```

Reglas:

- Procesa trabajadores activos.
- Crea automáticamente el concepto SUELDO_BASICO.
- No duplica conceptos ya existentes.
- Registra auditoría con la acción GENERAR_PLANILLA.

---

## Boleta individual PDF

```http
GET /api/detalles-planilla/periodo/{periodoPlanillaId}/trabajador/{trabajadorId}/boleta
GET /api/detalles-planilla/periodo/{periodoPlanillaId}/trabajador/{trabajadorId}/boleta/pdf
```

El PDF incluye:

- Datos de empresa
- Datos del trabajador
- Periodo de planilla
- Ingresos
- Descuentos
- Total ingresos
- Total descuentos
- Neto a pagar
- Firmas

Auditoría:

```text
DESCARGAR_BOLETA_PDF
```

---

## Reporte general PDF

```http
GET /api/detalles-planilla/periodo/{periodoPlanillaId}/reporte/pdf
```

Incluye:

- Periodo
- Tipo
- Estado
- Fecha de emisión
- Trabajadores
- Documento
- Ingresos
- Descuentos
- Neto a pagar
- Totales generales

Auditoría:

```text
DESCARGAR_REPORTE_PLANILLA_PDF
```

---

## Reporte general Excel

```http
GET /api/detalles-planilla/periodo/{periodoPlanillaId}/reporte/excel
```

Incluye:

- Periodo
- Tipo
- Estado
- Fecha de emisión
- Trabajadores
- Documento
- Ingresos
- Descuentos
- Neto a pagar
- Totales generales

Auditoría:

```text
DESCARGAR_REPORTE_PLANILLA_EXCEL
```

---

## Auditoría de planilla

Acciones registradas:

```text
CREAR_DETALLE
ACTUALIZAR_DETALLE
ELIMINAR_DETALLE
GENERAR_PLANILLA
DESCARGAR_BOLETA_PDF
DESCARGAR_REPORTE_PLANILLA_PDF
DESCARGAR_REPORTE_PLANILLA_EXCEL
CERRAR_PLANILLA
```

Endpoints:

```http
GET /api/auditoria-planilla/empresa-actual
GET /api/auditoria-planilla/periodo/{periodoId}
GET /api/auditoria-planilla/accion/{accion}
GET /api/auditoria-planilla/periodo/{periodoId}/accion/{accion}
GET /api/auditoria-planilla/trabajador/{trabajadorId}
GET /api/auditoria-planilla/usuario/{usuarioId}
```

---

## Dashboard de planillas

### Resumen general

```http
GET /api/dashboard/planillas
```

Devuelve:

- Total de planillas
- Planillas abiertas
- Planillas cerradas
- Total ingresos
- Total descuentos
- Total neto a pagar
- Última planilla registrada

### Últimas planillas

```http
GET /api/dashboard/planillas/ultimas
```

Devuelve las últimas planillas registradas, ordenadas desde la más reciente.

### Totales mensuales

```http
GET /api/dashboard/planillas/totales-mensuales
```

Devuelve:

- Año
- Mes
- Nombre del mes
- Total ingresos
- Total descuentos
- Total neto a pagar

---

## Roles del sistema

### SUPER_ADMIN

Tiene acceso completo.

### ADMIN_EMPRESA

Tiene acceso completo dentro de su empresa.

### RRHH

Puede gestionar trabajadores, planillas, detalles, reportes y dashboard.

### CONTADOR

Puede consultar información, descargar reportes PDF/Excel y ver dashboard.

No puede crear, actualizar ni eliminar detalles de planilla.

### LECTURA

No puede acceder a planillas, reportes ni dashboard.

---

## Pruebas realizadas

Se validó correctamente:

- Login JWT
- Acceso por roles
- Bloqueo de usuario LECTURA
- Acceso permitido para CONTADOR en consultas y reportes
- Creación de detalles
- Actualización de detalles
- Eliminación de detalles
- Generación automática de planilla
- Cierre de planilla
- Bloqueo de planilla cerrada
- Boleta individual PDF
- Reporte general PDF
- Reporte general Excel
- Auditoría de acciones
- Dashboard general
- Últimas planillas
- Totales mensuales

---

## Comandos útiles

Ejecutar proyecto:

```powershell
mvn spring-boot:run
```

Compilar:

```powershell
mvn clean compile
```

Guardar cambios en Git:

```powershell
git status
git add .
git commit -m "mensaje del cambio"
git push
```