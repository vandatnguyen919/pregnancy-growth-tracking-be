package com.pregnancy.edu.dashboard;

import com.pregnancy.edu.dashboard.dto.BarDataDto;
import com.pregnancy.edu.dashboard.dto.ColumnDataDto;
import com.pregnancy.edu.dashboard.dto.LineDataDto;
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

    @GetMapping("/radar")
    public Result getRadarData(
            @RequestParam Long fetusId,
            @RequestParam Integer week
    ) {
        RadarDataDto radarDataDto = dashboardService.getRadarData(fetusId, week);
        return new Result(true, StatusCode.SUCCESS, "Find Radar Data Success", radarDataDto);
    }

    @GetMapping("/column")
    public Result getColumnData(
            @RequestParam Long fetusId,
            @RequestParam Long metricId,
            @RequestParam Integer week
    ) {
        ColumnDataDto columnDataDto = dashboardService.getColumnData(fetusId, metricId, week);
        return new Result(true, StatusCode.SUCCESS, "Find Column Data Success", columnDataDto);
    }


    @GetMapping("/bar")
    public Result getBarData(
            @RequestParam Long fetusId,
            @RequestParam Integer week
    ) {
        BarDataDto barDataDto = dashboardService.getBarData(fetusId, week);
        return new Result(true, StatusCode.SUCCESS, "Find Bar Data Success", barDataDto);
    }

    @GetMapping("/line")
    public Result getLineData(
            @RequestParam Long fetusId,
            @RequestParam Long metricId
    ) {
        LineDataDto lineDataDto = dashboardService.getLineData(fetusId, metricId);
        return new Result(true, StatusCode.SUCCESS, "Find Line Data Success", lineDataDto);
    }
}
