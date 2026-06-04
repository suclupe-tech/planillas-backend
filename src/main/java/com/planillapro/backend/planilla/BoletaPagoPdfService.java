package com.planillapro.backend.planilla;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.planillapro.backend.planilla.dto.BoletaPagoDetalleDTO;
import com.planillapro.backend.planilla.dto.BoletaPagoTrabajadorDTO;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;

@Service
public class BoletaPagoPdfService {

    private final DetallePlanillaService detallePlanillaService;

    public BoletaPagoPdfService(DetallePlanillaService detallePlanillaService) {
        this.detallePlanillaService = detallePlanillaService;
    }

    public byte[] generarPdfBoleta(Long periodoPlanillaId, Long trabajadorId) {
        BoletaPagoTrabajadorDTO boleta = detallePlanillaService.generarBoletaTrabajador(
                periodoPlanillaId,
                trabajadorId
        );

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            Document document = new Document(PageSize.A4, 36, 36, 36, 36);
            PdfWriter.getInstance(document, outputStream);

            document.open();

            agregarTitulo(document);
            agregarDatosEmpresa(document, boleta);
            agregarDatosTrabajador(document, boleta);
            agregarTablaIngresos(document, boleta);
            agregarTablaDescuentos(document, boleta);
            agregarTotales(document, boleta);
            agregarFirma(document);

            document.close();

            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error al generar PDF de boleta de pago", e);
        }
    }

    private void agregarTitulo(Document document) throws Exception {
        Font tituloFont = new Font(Font.HELVETICA, 16, Font.BOLD);

        Paragraph titulo = new Paragraph("BOLETA DE PAGO", tituloFont);
        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setSpacingAfter(15);

        document.add(titulo);
    }

    private void agregarDatosEmpresa(Document document, BoletaPagoTrabajadorDTO boleta) throws Exception {
        Font labelFont = new Font(Font.HELVETICA, 10, Font.BOLD);
        Font textFont = new Font(Font.HELVETICA, 10, Font.NORMAL);

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{30, 70});
        table.setSpacingAfter(12);

        agregarFilaDato(table, "Empresa:", boleta.getEmpresaRazonSocial(), labelFont, textFont);
        agregarFilaDato(table, "RUC:", boleta.getEmpresaRuc(), labelFont, textFont);
        agregarFilaDato(table, "Dirección:", boleta.getEmpresaDireccion(), labelFont, textFont);
        agregarFilaDato(table, "Periodo:", boleta.getPeriodoNombre(), labelFont, textFont);
        agregarFilaDato(table, "Tipo:", boleta.getPeriodoTipo(), labelFont, textFont);
        agregarFilaDato(table, "Fecha emisión:", formatearFecha(boleta.getFechaEmision()), labelFont, textFont);

        document.add(table);
    }

    private void agregarDatosTrabajador(Document document, BoletaPagoTrabajadorDTO boleta) throws Exception {
        Font labelFont = new Font(Font.HELVETICA, 10, Font.BOLD);
        Font textFont = new Font(Font.HELVETICA, 10, Font.NORMAL);

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{30, 70});
        table.setSpacingAfter(12);

        String trabajador = boleta.getTrabajadorNombres() + " " + boleta.getTrabajadorApellidos();

        agregarFilaDato(table, "Trabajador:", trabajador, labelFont, textFont);
        agregarFilaDato(table, "Documento:", boleta.getTrabajadorDocumento(), labelFont, textFont);
        agregarFilaDato(table, "Cargo:", boleta.getCargo(), labelFont, textFont);
        agregarFilaDato(table, "Área:", boleta.getArea(), labelFont, textFont);

        document.add(table);
    }

    private void agregarTablaIngresos(Document document, BoletaPagoTrabajadorDTO boleta) throws Exception {
        agregarSubtitulo(document, "INGRESOS");
        agregarTablaDetalle(document, boleta.getIngresos());
    }

    private void agregarTablaDescuentos(Document document, BoletaPagoTrabajadorDTO boleta) throws Exception {
        agregarSubtitulo(document, "DESCUENTOS");
        agregarTablaDetalle(document, boleta.getDescuentos());
    }

    private void agregarTablaDetalle(Document document, java.util.List<BoletaPagoDetalleDTO> detalles) throws Exception {
        Font headerFont = new Font(Font.HELVETICA, 9, Font.BOLD);
        Font textFont = new Font(Font.HELVETICA, 9, Font.NORMAL);

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{20, 40, 20, 20});
        table.setSpacingAfter(10);

        agregarCeldaHeader(table, "Código", headerFont);
        agregarCeldaHeader(table, "Concepto", headerFont);
        agregarCeldaHeader(table, "Tipo", headerFont);
        agregarCeldaHeader(table, "Monto", headerFont);

        for (BoletaPagoDetalleDTO detalle : detalles) {
            agregarCeldaTexto(table, detalle.getConceptoCodigo(), textFont);
            agregarCeldaTexto(table, detalle.getConceptoNombre(), textFont);
            agregarCeldaTexto(table, detalle.getTipo(), textFont);
            agregarCeldaTextoDerecha(table, formatearMonto(detalle.getMonto()), textFont);
        }

        document.add(table);
    }

    private void agregarTotales(Document document, BoletaPagoTrabajadorDTO boleta) throws Exception {
        Font labelFont = new Font(Font.HELVETICA, 10, Font.BOLD);
        Font textFont = new Font(Font.HELVETICA, 10, Font.NORMAL);

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(45);
        table.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.setWidths(new float[]{60, 40});
        table.setSpacingBefore(10);

        agregarFilaTotal(table, "Total ingresos:", formatearMonto(boleta.getTotalIngresos()), labelFont, textFont);
        agregarFilaTotal(table, "Total descuentos:", formatearMonto(boleta.getTotalDescuentos()), labelFont, textFont);
        agregarFilaTotal(table, "Neto a pagar:", formatearMonto(boleta.getNetoPagar()), labelFont, labelFont);

        document.add(table);
    }

    private void agregarFirma(Document document) throws Exception {
        Font textFont = new Font(Font.HELVETICA, 10, Font.NORMAL);

        Paragraph espacio = new Paragraph("\n\n\n");
        document.add(espacio);

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{50, 50});

        PdfPCell firmaEmpresa = new PdfPCell(new Phrase("____________________________\nFirma Empresa", textFont));
        firmaEmpresa.setBorder(PdfPCell.NO_BORDER);
        firmaEmpresa.setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell firmaTrabajador = new PdfPCell(new Phrase("____________________________\nFirma Trabajador", textFont));
        firmaTrabajador.setBorder(PdfPCell.NO_BORDER);
        firmaTrabajador.setHorizontalAlignment(Element.ALIGN_CENTER);

        table.addCell(firmaEmpresa);
        table.addCell(firmaTrabajador);

        document.add(table);
    }

    private void agregarSubtitulo(Document document, String texto) throws Exception {
        Font font = new Font(Font.HELVETICA, 11, Font.BOLD);

        Paragraph paragraph = new Paragraph(texto, font);
        paragraph.setSpacingBefore(8);
        paragraph.setSpacingAfter(5);

        document.add(paragraph);
    }

    private void agregarFilaDato(
            PdfPTable table,
            String label,
            String value,
            Font labelFont,
            Font textFont
    ) {
        agregarCeldaSinBorde(table, label, labelFont);
        agregarCeldaSinBorde(table, value != null ? value : "", textFont);
    }

    private void agregarFilaTotal(
            PdfPTable table,
            String label,
            String value,
            Font labelFont,
            Font textFont
    ) {
        agregarCeldaTexto(table, label, labelFont);
        agregarCeldaTextoDerecha(table, value, textFont);
    }

    private void agregarCeldaHeader(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(new Color(230, 230, 230));
        cell.setPadding(5);
        table.addCell(cell);
    }

    private void agregarCeldaTexto(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text != null ? text : "", font));
        cell.setPadding(5);
        table.addCell(cell);
    }

    private void agregarCeldaTextoDerecha(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text != null ? text : "", font));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setPadding(5);
        table.addCell(cell);
    }

    private void agregarCeldaSinBorde(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text != null ? text : "", font));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setPadding(3);
        table.addCell(cell);
    }

    private String formatearMonto(BigDecimal monto) {
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        return decimalFormat.format(monto != null ? monto : BigDecimal.ZERO);
    }

    private String formatearFecha(java.time.LocalDate fecha) {
        if (fecha == null) {
            return "";
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return fecha.format(formatter);
    }
}