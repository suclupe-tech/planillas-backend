package com.planillapro.backend.dashboard;

import com.planillapro.backend.dashboard.dto.DashboardPlanillaDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardPlanillaController {

    private final DashboardPlanillaService dashboardPlanillaService;

    public DashboardPlanillaController(DashboardPlanillaService dashboardPlanillaService) {
        this.dashboardPlanillaService = dashboardPlanillaService;
    }

    @GetMapping("/planillas")
    public DashboardPlanillaDTO obtenerDashboardPlanillas() {
        return dashboardPlanillaService.obtenerDashboardPlanillas();
    }
}