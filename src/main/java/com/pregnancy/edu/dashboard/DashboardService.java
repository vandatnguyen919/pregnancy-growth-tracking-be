package com.pregnancy.edu.dashboard;

import com.pregnancy.edu.dashboard.dto.*;
import com.pregnancy.edu.fetusinfo.fetus.FetusService;
import com.pregnancy.edu.fetusinfo.fetusmetric.FetusMetric;
import com.pregnancy.edu.fetusinfo.fetusmetric.FetusMetricService;
import com.pregnancy.edu.fetusinfo.metric.Metric;
import com.pregnancy.edu.fetusinfo.metric.MetricService;
import com.pregnancy.edu.fetusinfo.metric.converter.MetricToMetricDtoConverter;
import com.pregnancy.edu.fetusinfo.standard.Standard;
import com.pregnancy.edu.fetusinfo.standard.StandardService;
import com.pregnancy.edu.system.utils.MathUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DashboardService {

    private final FetusService fetusService;
    private final FetusMetricService fetusMetricService;
    private final MetricService metricService;
    private final StandardService standardService;

    private final MetricToMetricDtoConverter metricToMetricDtoConverter;

    private final String MAX = "max";
    private final String VALUE = "value";
    private final String MIN = "min";

    public DashboardService(FetusService fetusService, FetusMetricService fetusMetricService, MetricService metricService, StandardService standardService, MetricToMetricDtoConverter metricToMetricDtoConverter) {
        this.fetusService = fetusService;
        this.fetusMetricService = fetusMetricService;
        this.metricService = metricService;
        this.standardService = standardService;
        this.metricToMetricDtoConverter = metricToMetricDtoConverter;
    }

    public RadarChartDto getRadarData(Long fetusId, Integer week) {
        fetusService.findById(fetusId);

        List<FetusMetric> fetusMetrics = fetusMetricService.findByFetusIdAndWeek(fetusId, week);
        if (fetusMetrics.size() < 3) {
            throw new IllegalArgumentException("Fetus metrics are not enough to generate radar data");
        }

        List<ChartDataItem> data = new ArrayList<>();

        // Iterate all fetal metric data in a specific week
        for (FetusMetric fetusMetric : fetusMetrics) {
            Metric metric = fetusMetric.getMetric();
            String name = metric.getName() + " (" + metric.getUnit() + ")";

            data.addAll(getChartData(metric.getId(), week, name, fetusMetric.getValue()));
        }
        double max = Double.MIN_VALUE;
        for (ChartDataItem item : data) {
            max = Double.max(max, item.value());
        }
        return new RadarChartDto(max, data);
    }

    public ChartDto getBarData(Long fetusId, Integer week) {
        fetusService.findById(fetusId);

        List<FetusMetric> fetusMetrics = fetusMetricService.findByFetusIdAndWeek(fetusId, week);

        List<ChartDataItem> data = new ArrayList<>();

        // Iterate all fetal metric data in a specific week
        for (FetusMetric fetusMetric : fetusMetrics) {
            Metric metric = fetusMetric.getMetric();
            String name = metric.getName() + " (" + metric.getUnit() + ")";

            data.addAll(getChartData(metric.getId(), week, name, fetusMetric.getValue()));
        }
        return new ChartDto(data);
    }

    public SingleMetricChartDto getColumnData(Long fetusId, Long metricId, Integer week) {
        fetusService.findById(fetusId);
        Metric metric = metricService.findById(metricId);

        FetusMetric fetusMetric = fetusMetricService.findByFetusIdAndMetricIdAndWeek(fetusId, metricId, week);

        // Get the fetal metric data for a specific week
        List<ChartDataItem> data = getChartData(metricId, week, "Week " + week, fetusMetric.getValue());

        return new SingleMetricChartDto(metricToMetricDtoConverter.convert(metric), data);
    }

    public SingleMetricChartDto getLineData(Long fetusId, Long metricId) {
        fetusService.findById(fetusId);
        Metric metric = metricService.findById(metricId);

        List<ChartDataItem> data = new ArrayList<>();
        List<FetusMetric> fetusMetrics = fetusMetricService.findByFetusIdAndMetricId(fetusId, metricId);

        // Iterate the fetal metric data through each week
        for (FetusMetric fetusMetric : fetusMetrics) {
            int week = fetusMetric.getWeek();

            data.addAll(getChartData(metricId, week, "Week " + week, fetusMetric.getValue()));
        }
        return new SingleMetricChartDto(metricToMetricDtoConverter.convert(metric), data);
    }

    // Helper method to get the max, current, and min values for a fetus metric
    private List<ChartDataItem> getChartData(Long metricId, Integer week, String name, Double myValue) {
        List<ChartDataItem> data = new ArrayList<>();
        Standard standard = standardService.findByMetricIdAndWeek(metricId, week);

        // Round the values to 2 decimal places
        double roundedMax = MathUtils.round(2, standard.getMax());
        double roundedCurrent = MathUtils.round(2, myValue);
        double roundedMin = MathUtils.round(2, standard.getMin());

        data.add(new ChartDataItem(MAX, name, roundedMax));
        data.add(new ChartDataItem(VALUE, name, roundedCurrent));
        data.add(new ChartDataItem(MIN, name, roundedMin));
        return data;
    }
}
