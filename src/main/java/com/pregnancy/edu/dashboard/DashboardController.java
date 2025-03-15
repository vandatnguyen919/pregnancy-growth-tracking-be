package com.pregnancy.edu.dashboard;

import com.pregnancy.edu.dashboard.dto.RadarDataDto;
import com.pregnancy.edu.system.Result;
import com.pregnancy.edu.system.StatusCode;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/radar/fetuses/{fetusId}")
    public Result getRadarData(
            @PathVariable("fetusId") Long fetusId,
            @RequestParam Integer week
    ) {
        RadarDataDto radarDataDto = dashboardService.getRadarData(fetusId, week);
        return new Result(true, StatusCode.SUCCESS, "Find Radar Data Success", radarDataDto);
    }
}
