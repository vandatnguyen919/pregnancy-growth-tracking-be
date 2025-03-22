package com.pregnancy.edu.dashboard;

import com.pregnancy.edu.dashboard.dto.ChartDto;
import com.pregnancy.edu.dashboard.dto.RadarChartDto;
import com.pregnancy.edu.dashboard.dto.SingleMetricChartDto;
import com.pregnancy.edu.system.Result;
import com.pregnancy.edu.system.StatusCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService
                                       dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/radar")
    public Result getRadarData(
            @RequestParam Long fetusId,
            @RequestParam Integer week
    ) {
        RadarChartDto radarChartDto = dashboardService.getRadarData(fetusId, week);
        return new Result(true, StatusCode.SUCCESS, "Find Radar Data Success", radarChartDto);
    }

    @GetMapping("/bar")
    public Result getBarData(
            @RequestParam Long fetusId,
            @RequestParam Integer week
    ) {
        ChartDto barChartData = dashboardService.getBarData(fetusId, week);
        return new Result(true, StatusCode.SUCCESS, "Find Bar Data Success", barChartData);
    }

    @GetMapping("/column")
    public Result getColumnData(
            @RequestParam Long fetusId,
            @RequestParam Long metricId,
            @RequestParam Integer week
    ) {
        SingleMetricChartDto columnChartData = dashboardService.getColumnData(fetusId, metricId, week);
        return new Result(true, StatusCode.SUCCESS, "Find Column Data Success", columnChartData);
    }

    @GetMapping("/line")
    public Result getLineData(
            @RequestParam Long fetusId,
            @RequestParam Long metricId
    ) {
        SingleMetricChartDto lineChartData = dashboardService.getLineData(fetusId, metricId);
        return new Result(true, StatusCode.SUCCESS, "Find Line Data Success", lineChartData);
    }
}
