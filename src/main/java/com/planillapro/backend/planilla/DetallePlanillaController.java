package com.planillapro.backend.planilla;

import com.planillapro.backend.planilla.dto.BoletaPagoTrabajadorDTO;
import com.planillapro.backend.planilla.dto.DetallePlanillaRequestDTO;
import com.planillapro.backend.planilla.dto.DetallePlanillaResponseDTO;
import com.planillapro.backend.planilla.dto.ResumenPlanillaPeriodoDTO;
import com.planillapro.backend.planilla.dto.ResumenPlanillaTrabajadorDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.List;

        @RestController
        @RequestMapping("/api/detalles-planilla")
        public class DetallePlanillaController {

        private final DetallePlanillaService detallePlanillaService;
        private final BoletaPagoPdfService boletaPagoPdfService;
        private final ReportePlanillaPdfService reportePlanillaPdfService;
        private final ReportePlanillaExcelService reportePlanillaExcelService;
        

        public DetallePlanillaController(
                DetallePlanillaService detallePlanillaService,
                BoletaPagoPdfService boletaPagoPdfService,
                ReportePlanillaPdfService reportePlanillaPdfService,
                ReportePlanillaExcelService reportePlanillaExcelService
        ) {
                this.detallePlanillaService = detallePlanillaService;
                this.boletaPagoPdfService = boletaPagoPdfService;
                this.reportePlanillaPdfService = reportePlanillaPdfService;
                this.reportePlanillaExcelService = reportePlanillaExcelService;
        }

        @PostMapping
        @ResponseStatus(HttpStatus.CREATED)
        public DetallePlanillaResponseDTO crear(@Valid @RequestBody DetallePlanillaRequestDTO request) {
                return detallePlanillaService.crear(request);
        }

        @GetMapping("/periodo/{periodoPlanillaId}")
        public List<DetallePlanillaResponseDTO> listarPorPeriodo(
                @PathVariable Long periodoPlanillaId
        ) {
                return detallePlanillaService.listarPorPeriodo(periodoPlanillaId);
        }

        @GetMapping("/trabajador/{trabajadorId}")
        public List<DetallePlanillaResponseDTO> listarPorTrabajador(
                @PathVariable Long trabajadorId
        ) {
                return detallePlanillaService.listarPorTrabajador(trabajadorId);
        }

        @GetMapping("/periodo/{periodoPlanillaId}/trabajador/{trabajadorId}")
        public List<DetallePlanillaResponseDTO> listarPorPeriodoYTrabajador(
                @PathVariable Long periodoPlanillaId,
                @PathVariable Long trabajadorId
        ) {
                return detallePlanillaService.listarPorPeriodoYTrabajador(
                        periodoPlanillaId,
                        trabajadorId
                );
        }

        @GetMapping("/periodo/{periodoPlanillaId}/trabajador/{trabajadorId}/resumen")
        public ResumenPlanillaTrabajadorDTO calcularResumenTrabajador(
                @PathVariable Long periodoPlanillaId,
                @PathVariable Long trabajadorId
        ) {
                return detallePlanillaService.calcularResumenTrabajador(
                        periodoPlanillaId,
                        trabajadorId
                );
        }

        @GetMapping("/periodo/{periodoPlanillaId}/resumen")
        public ResumenPlanillaPeriodoDTO calcularResumenPeriodo(
                @PathVariable Long periodoPlanillaId
        ) {
                return detallePlanillaService.calcularResumenPeriodo(periodoPlanillaId);
        }

        @PutMapping("/{id}")
        public DetallePlanillaResponseDTO actualizar(
                @PathVariable Long id,
                @Valid @RequestBody DetallePlanillaRequestDTO request
        ) {
                return detallePlanillaService.actualizar(id, request);
        }

        @DeleteMapping("/{id}")
        @ResponseStatus(HttpStatus.NO_CONTENT)
        public void eliminar(@PathVariable Long id) {
                detallePlanillaService.eliminar(id);
        }

        @PostMapping("/periodo/{periodoPlanillaId}/generar")
        public ResumenPlanillaPeriodoDTO generarPlanillaPeriodo(
                @PathVariable Long periodoPlanillaId
        ) {
                return detallePlanillaService.generarPlanillaPeriodo(periodoPlanillaId);
        }

        @GetMapping("/periodo/{periodoPlanillaId}/trabajador/{trabajadorId}/boleta")
        public BoletaPagoTrabajadorDTO generarBoletaTrabajador(
                @PathVariable Long periodoPlanillaId,
                @PathVariable Long trabajadorId
        ) {
                return detallePlanillaService.generarBoletaTrabajador(
                        periodoPlanillaId,
                        trabajadorId
                );
        }
        
        @GetMapping("/periodo/{periodoPlanillaId}/trabajador/{trabajadorId}/boleta/pdf")
        public ResponseEntity<byte[]> descargarBoletaPdf(
                @PathVariable Long periodoPlanillaId,
                @PathVariable Long trabajadorId
        ) {
                byte[] pdf = boletaPagoPdfService.generarPdfBoleta(
                        periodoPlanillaId,
                        trabajadorId
                );

                String nombreArchivo = "boleta-pago-" + periodoPlanillaId + "-" + trabajadorId + ".pdf";

                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + nombreArchivo)
                        .contentType(MediaType.APPLICATION_PDF)
                        .body(pdf);
        }

        @GetMapping("/periodo/{periodoPlanillaId}/reporte/pdf")
        public ResponseEntity<byte[]> descargarReportePlanillaPdf(
                        @PathVariable Long periodoPlanillaId
                ) {
                byte[] pdf = reportePlanillaPdfService.generarReportePlanillaPdf(periodoPlanillaId);

                String filename = "reporte-planilla-periodo-" + periodoPlanillaId + ".pdf";

                return ResponseEntity.ok()
                        .header(
                                HttpHeaders.CONTENT_DISPOSITION,
                                "attachment; filename=" + filename
                        )
                        .contentType(MediaType.APPLICATION_PDF)
                        .body(pdf);
        }
        
        @GetMapping("/periodo/{periodoPlanillaId}/reporte/excel")
        public ResponseEntity<byte[]> descargarReportePlanillaExcel(
                        @PathVariable Long periodoPlanillaId
                ) {
                byte[] excel = reportePlanillaExcelService.generarReportePlanillaExcel(periodoPlanillaId);

                String filename = "reporte-planilla-periodo-" + periodoPlanillaId + ".xlsx";

                return ResponseEntity.ok()
                        .header(
                                HttpHeaders.CONTENT_DISPOSITION,
                                "attachment; filename=" + filename
                        )
                        .contentType(MediaType.parseMediaType(
                                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                        ))
                        .body(excel);
                }
                

}