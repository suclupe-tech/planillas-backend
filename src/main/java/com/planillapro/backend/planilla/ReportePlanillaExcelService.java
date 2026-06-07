package com.planillapro.backend.planilla;

import com.planillapro.backend.periodo.PeriodoPlanilla;
import com.planillapro.backend.periodo.PeriodoPlanillaRepository;
import com.planillapro.backend.planilla.dto.ResumenPlanillaPeriodoDTO;
import com.planillapro.backend.planilla.dto.ResumenPlanillaTrabajadorDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class ReportePlanillaExcelService {

    private final DetallePlanillaService detallePlanillaService;
    private final AuditoriaPlanillaService auditoriaPlanillaService;
    private final PeriodoPlanillaRepository periodoPlanillaRepository;

    public ReportePlanillaExcelService(
            DetallePlanillaService detallePlanillaService,
            AuditoriaPlanillaService auditoriaPlanillaService,
            PeriodoPlanillaRepository periodoPlanillaRepository
    ) {
        this.detallePlanillaService = detallePlanillaService;
        this.auditoriaPlanillaService = auditoriaPlanillaService;
        this.periodoPlanillaRepository = periodoPlanillaRepository;
    }

    public byte[] generarReportePlanillaExcel(Long periodoPlanillaId) {
        ResumenPlanillaPeriodoDTO resumen =
                detallePlanillaService.calcularResumenPeriodo(periodoPlanillaId);

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Reporte Planilla");

            CellStyle tituloStyle = crearTituloStyle(workbook);
            CellStyle headerStyle = crearHeaderStyle(workbook);
            CellStyle moneyStyle = crearMoneyStyle(workbook);
            CellStyle normalStyle = crearNormalStyle(workbook);

            int rowIndex = 0;

            Row tituloRow = sheet.createRow(rowIndex++);
            Cell tituloCell = tituloRow.createCell(0);
            tituloCell.setCellValue("REPORTE GENERAL DE PLANILLA");
            tituloCell.setCellStyle(tituloStyle);

            rowIndex++;

            rowIndex = agregarDato(sheet, rowIndex, "Periodo", resumen.getPeriodoNombre(), normalStyle);
            rowIndex = agregarDato(sheet, rowIndex, "Tipo", resumen.getTipo(), normalStyle);
            rowIndex = agregarDato(sheet, rowIndex, "Estado", resumen.getEstado(), normalStyle);
            rowIndex = agregarDato(
                    sheet,
                    rowIndex,
                    "Fecha emisión",
                    LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    normalStyle
            );

            rowIndex++;

            Row headerRow = sheet.createRow(rowIndex++);
            String[] headers = {
                    "N°",
                    "Trabajador",
                    "Documento",
                    "Ingresos",
                    "Descuentos",
                    "Neto a pagar"
            };

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            int contador = 1;

            for (ResumenPlanillaTrabajadorDTO trabajador : resumen.getTrabajadores()) {
                Row row = sheet.createRow(rowIndex++);

                String nombreCompleto = trabajador.getTrabajadorNombres()
                        + " "
                        + trabajador.getTrabajadorApellidos();

                row.createCell(0).setCellValue(contador);
                row.createCell(1).setCellValue(nombreCompleto);
                row.createCell(2).setCellValue(trabajador.getTrabajadorDocumento());

                crearCeldaMonto(row, 3, trabajador.getTotalIngresos(), moneyStyle);
                crearCeldaMonto(row, 4, trabajador.getTotalDescuentos(), moneyStyle);
                crearCeldaMonto(row, 5, trabajador.getNetoPagar(), moneyStyle);

                contador++;
            }

            rowIndex++;

            rowIndex = agregarTotal(sheet, rowIndex, "Total trabajadores", resumen.getCantidadTrabajadores(), normalStyle);
            rowIndex = agregarTotalMonto(sheet, rowIndex, "Total ingresos", resumen.getTotalIngresos(), moneyStyle);
            rowIndex = agregarTotalMonto(sheet, rowIndex, "Total descuentos", resumen.getTotalDescuentos(), moneyStyle);
            rowIndex = agregarTotalMonto(sheet, rowIndex, "Total neto a pagar", resumen.getTotalNetoPagar(), moneyStyle);

            sheet.setColumnWidth(0, 8 * 256);   // N°
            sheet.setColumnWidth(1, 35 * 256);  // Trabajador
            sheet.setColumnWidth(2, 18 * 256);  // Documento
            sheet.setColumnWidth(3, 18 * 256);  // Ingresos
            sheet.setColumnWidth(4, 18 * 256);  // Descuentos
            sheet.setColumnWidth(5, 18 * 256);  // Neto a pagar

            workbook.write(outputStream);

            registrarAuditoria(periodoPlanillaId);

            return outputStream.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error al generar reporte Excel de planilla", e);
        }
    }

    private void registrarAuditoria(Long periodoPlanillaId) {
        PeriodoPlanilla periodoPlanilla = periodoPlanillaRepository.findById(periodoPlanillaId)
                .orElseThrow(() -> new RuntimeException("Periodo de planilla no encontrado"));

        auditoriaPlanillaService.registrar(
                periodoPlanilla.getEmpresa(),
                periodoPlanilla,
                null,
                null,
                "DESCARGAR_REPORTE_PLANILLA_EXCEL",
                "Se descargó el reporte general Excel del periodo " + periodoPlanilla.getNombre()
        );
    }

    private int agregarDato(Sheet sheet, int rowIndex, String label, String value, CellStyle style) {
        Row row = sheet.createRow(rowIndex);

        Cell labelCell = row.createCell(0);
        labelCell.setCellValue(label + ":");
        labelCell.setCellStyle(style);

        Cell valueCell = row.createCell(1);
        valueCell.setCellValue(value);
        valueCell.setCellStyle(style);

        return rowIndex + 1;
    }

    private int agregarTotal(Sheet sheet, int rowIndex, String label, Integer value, CellStyle style) {
        Row row = sheet.createRow(rowIndex);

        Cell labelCell = row.createCell(4);
        labelCell.setCellValue(label + ":");
        labelCell.setCellStyle(style);

        Cell valueCell = row.createCell(5);
        valueCell.setCellValue(value != null ? value : 0);
        valueCell.setCellStyle(style);

        return rowIndex + 1;
    }

    private int agregarTotalMonto(Sheet sheet, int rowIndex, String label, BigDecimal value, CellStyle moneyStyle) {
        Row row = sheet.createRow(rowIndex);

        Cell labelCell = row.createCell(4);
        labelCell.setCellValue(label + ":");

        crearCeldaMonto(row, 5, value, moneyStyle);

        return rowIndex + 1;
    }

    private void crearCeldaMonto(Row row, int columnIndex, BigDecimal value, CellStyle moneyStyle) {
        Cell cell = row.createCell(columnIndex);
        cell.setCellValue(value != null ? value.doubleValue() : 0.00);
        cell.setCellStyle(moneyStyle);
    }

    private CellStyle crearTituloStyle(Workbook workbook) {
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 16);

        CellStyle style = workbook.createCellStyle();
        style.setFont(font);

        return style;
    }

    private CellStyle crearHeaderStyle(Workbook workbook) {
        Font font = workbook.createFont();
        font.setBold(true);

        CellStyle style = workbook.createCellStyle();
        style.setFont(font);
        style.setBorderBottom(BorderStyle.THIN);

        return style;
    }

    private CellStyle crearMoneyStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        DataFormat format = workbook.createDataFormat();
        style.setDataFormat(format.getFormat("#,##0.00"));

        return style;
    }

    private CellStyle crearNormalStyle(Workbook workbook) {
        return workbook.createCellStyle();
    }
}