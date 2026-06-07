package com.planillapro.backend.planilla;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.planillapro.backend.planilla.dto.ResumenPlanillaPeriodoDTO;
import com.planillapro.backend.planilla.dto.ResumenPlanillaTrabajadorDTO;
import org.springframework.stereotype.Service;
import com.planillapro.backend.periodo.PeriodoPlanilla;
import com.planillapro.backend.periodo.PeriodoPlanillaRepository;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class ReportePlanillaPdfService {

    private final DetallePlanillaService detallePlanillaService;
    private final AuditoriaPlanillaService auditoriaPlanillaService;
    private final PeriodoPlanillaRepository periodoPlanillaRepository;

    public ReportePlanillaPdfService(
            DetallePlanillaService detallePlanillaService,
            AuditoriaPlanillaService auditoriaPlanillaService,
            PeriodoPlanillaRepository periodoPlanillaRepository
    ) {
        this.detallePlanillaService = detallePlanillaService;
        this.auditoriaPlanillaService = auditoriaPlanillaService;
        this.periodoPlanillaRepository = periodoPlanillaRepository;
    }

    public byte[] generarReportePlanillaPdf(Long periodoPlanillaId) {
        ResumenPlanillaPeriodoDTO resumen =
                detallePlanillaService.calcularResumenPeriodo(periodoPlanillaId);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Document document = new Document(PageSize.A4, 36, 36, 36, 36);
        PdfWriter.getInstance(document, outputStream);

        document.open();

        agregarTitulo(document);
        agregarDatosPeriodo(document, resumen);
        agregarTablaTrabajadores(document, resumen);
        agregarTotales(document, resumen);

        document.close();

        PeriodoPlanilla periodoPlanilla = periodoPlanillaRepository.findById(periodoPlanillaId)
                .orElseThrow(() -> new RuntimeException("Periodo de planilla no encontrado"));

        auditoriaPlanillaService.registrar(
                periodoPlanilla.getEmpresa(),
                periodoPlanilla,
                null,
                null,
                "DESCARGAR_REPORTE_PLANILLA_PDF",
                "Se descargó el reporte general PDF del periodo " + periodoPlanilla.getNombre()
        );

        return outputStream.toByteArray();
    }

    private void agregarTitulo(Document document) {
        Font tituloFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);

        Paragraph titulo = new Paragraph("REPORTE GENERAL DE PLANILLA", tituloFont);
        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setSpacingAfter(18);

        document.add(titulo);
    }

    private void agregarDatosPeriodo(Document document, ResumenPlanillaPeriodoDTO resumen) {
        Font labelFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
        Font valueFont = FontFactory.getFont(FontFactory.HELVETICA, 10);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingAfter(16);
        table.setWidths(new float[]{1.2f, 3f});

        agregarCeldaDato(table, "Periodo:", labelFont);
        agregarCeldaDato(table, resumen.getPeriodoNombre(), valueFont);

        agregarCeldaDato(table, "Tipo:", labelFont);
        agregarCeldaDato(table, resumen.getTipo(), valueFont);

        agregarCeldaDato(table, "Estado:", labelFont);
        agregarCeldaDato(table, resumen.getEstado(), valueFont);

        agregarCeldaDato(table, "Fecha emisión:", labelFont);
        agregarCeldaDato(table, LocalDate.now().format(formatter), valueFont);

        document.add(table);
    }

    private void agregarTablaTrabajadores(Document document, ResumenPlanillaPeriodoDTO resumen) {
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9);
        Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 9);

        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        table.setSpacingAfter(16);
        table.setWidths(new float[]{0.7f, 2.8f, 1.5f, 1.4f, 1.4f, 1.4f});

        agregarHeader(table, "N°", headerFont);
        agregarHeader(table, "Trabajador", headerFont);
        agregarHeader(table, "Documento", headerFont);
        agregarHeader(table, "Ingresos", headerFont);
        agregarHeader(table, "Descuentos", headerFont);
        agregarHeader(table, "Neto", headerFont);

        int contador = 1;

        for (ResumenPlanillaTrabajadorDTO trabajador : resumen.getTrabajadores()) {
            String nombreCompleto = trabajador.getTrabajadorNombres()
                    + " "
                    + trabajador.getTrabajadorApellidos();

            agregarCelda(table, String.valueOf(contador), bodyFont, Element.ALIGN_CENTER);
            agregarCelda(table, nombreCompleto, bodyFont, Element.ALIGN_LEFT);
            agregarCelda(table, trabajador.getTrabajadorDocumento(), bodyFont, Element.ALIGN_CENTER);
            agregarCelda(table, formatoMonto(trabajador.getTotalIngresos()), bodyFont, Element.ALIGN_RIGHT);
            agregarCelda(table, formatoMonto(trabajador.getTotalDescuentos()), bodyFont, Element.ALIGN_RIGHT);
            agregarCelda(table, formatoMonto(trabajador.getNetoPagar()), bodyFont, Element.ALIGN_RIGHT);

            contador++;
        }

        document.add(table);
    }

    private void agregarTotales(Document document, ResumenPlanillaPeriodoDTO resumen) {
        Font labelFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
        Font valueFont = FontFactory.getFont(FontFactory.HELVETICA, 10);

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(45);
        table.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.setWidths(new float[]{2f, 1.5f});

        agregarCeldaDato(table, "Total trabajadores:", labelFont);
        agregarCeldaDato(table, String.valueOf(resumen.getCantidadTrabajadores()), valueFont);

        agregarCeldaDato(table, "Total ingresos:", labelFont);
        agregarCeldaDato(table, formatoMonto(resumen.getTotalIngresos()), valueFont);

        agregarCeldaDato(table, "Total descuentos:", labelFont);
        agregarCeldaDato(table, formatoMonto(resumen.getTotalDescuentos()), valueFont);

        agregarCeldaDato(table, "Total neto a pagar:", labelFont);
        agregarCeldaDato(table, formatoMonto(resumen.getTotalNetoPagar()), valueFont);

        document.add(table);
    }

    private void agregarHeader(PdfPTable table, String texto, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(texto, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(6);
        table.addCell(cell);
    }

    private void agregarCelda(PdfPTable table, String texto, Font font, int align) {
        PdfPCell cell = new PdfPCell(new Phrase(texto, font));
        cell.setHorizontalAlignment(align);
        cell.setPadding(5);
        table.addCell(cell);
    }

    private void agregarCeldaDato(PdfPTable table, String texto, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(texto, font));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setPadding(4);
        table.addCell(cell);
    }

    private String formatoMonto(BigDecimal monto) {
        if (monto == null) {
            return "0.00";
        }

        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        return decimalFormat.format(monto);
    }
}