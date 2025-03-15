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

    public RadarDataDto getRadarData(Long fetusId, Integer week) {
        assert fetusService.findById(fetusId) != null;

        List<FetusMetric> fetusMetrics = fetusMetricService.findByFetusIdAndWeek(fetusId, week);
        if (fetusMetrics.size() < 3) {
            throw new IllegalArgumentException("Fetus metrics are not enough to generate radar data");
        }
        double max = Double.MIN_VALUE;
        List<RadarDataItem> data = new ArrayList<>();
        for (FetusMetric fetusMetric : fetusMetrics) {
            Metric metric = fetusMetric.getMetric();
            Standard standard = standardService.findByMetricIdAndWeek(metric.getId(), fetusMetric.getWeek());
            max = Double.max(max, Double.max(standard.getMax(), fetusMetric.getValue()));

            // Round the values to 2 decimal places
            double roundedMax = MathUtils.round(2, standard.getMax());
            double roundedCurrent = MathUtils.round(2, fetusMetric.getValue());
            double roundedMin = MathUtils.round(2, standard.getMin());

            String name = metric.getName() + " (" + metric.getUnit() + ")";
            data.add(new RadarDataItem(name, MAX, roundedMax));
            data.add(new RadarDataItem(name, VALUE, roundedCurrent));
            data.add(new RadarDataItem(name, MIN, roundedMin));
        }
        // Round the final max value as well
        double roundedMax = MathUtils.round(2, max);
        return new RadarDataDto(roundedMax, data);
    }

    public BarDataDto getBarData(Long fetusId, Integer week) {
        List<FetusMetric> fetusMetrics = fetusMetricService.findByFetusIdAndWeek(fetusId, week);

        List<BarDataItem> data = new ArrayList<>();
        for (FetusMetric fetusMetric : fetusMetrics) {
            Metric metric = fetusMetric.getMetric();
            Standard standard = standardService.findByMetricIdAndWeek(metric.getId(), fetusMetric.getWeek());

            // Round the values to 2 decimal places
            double roundedMax = MathUtils.round(2, standard.getMax());
            double roundedCurrent = MathUtils.round(2, fetusMetric.getValue());
            double roundedMin = MathUtils.round(2, standard.getMin());

            String name = metric.getName() + " (" + metric.getUnit() + ")";
            data.add(new BarDataItem(name, MAX, roundedMax));
            data.add(new BarDataItem(name, VALUE, roundedCurrent));
            data.add(new BarDataItem(name, MIN, roundedMin));
        }
        return new BarDataDto(data);
    }

    public ColumnDataDto getColumnData(Long fetusId, Long metricId, Integer week) {
        assert fetusService.findById(fetusId) != null;
        Metric metric = metricService.findById(metricId);

        List<ColumnDataItem> data = new ArrayList<>();

        FetusMetric fetusMetric = fetusMetricService.findByFetusIdAndMetricIdAndWeek(fetusId, metricId, week);
        Standard standard = standardService.findByMetricIdAndWeek(metricId, week);

        String name = "Week " + week;

        // Round the values to 2 decimal places
        double roundedMax = MathUtils.round(2, standard.getMax());
        double roundedCurrent = MathUtils.round(2, fetusMetric.getValue());
        double roundedMin = MathUtils.round(2, standard.getMin());

        data.add(new ColumnDataItem(name, MAX, roundedMax));
        data.add(new ColumnDataItem(name, VALUE, roundedCurrent));
        data.add(new ColumnDataItem(name, MIN, roundedMin));

        return new ColumnDataDto(metricToMetricDtoConverter.convert(metric), data);
    }

    public LineDataDto getLineData(Long fetusId, Long metricId) {
        assert fetusService.findById(fetusId) != null;
        Metric metric = metricService.findById(metricId);

        List<LineDataItem> data = new ArrayList<>();
        List<FetusMetric> fetusMetrics = fetusMetricService.findByFetusIdAndMetricId(fetusId, metricId);
        for (FetusMetric fetusMetric : fetusMetrics) {
            int week = fetusMetric.getWeek();
            Standard standard = standardService.findByMetricIdAndWeek(metricId, week);

            // Round the values to 2 decimal places
            double roundedMax = MathUtils.round(2, standard.getMax());
            double roundedCurrent = MathUtils.round(2, fetusMetric.getValue());
            double roundedMin = MathUtils.round(2, standard.getMin());

            String date = "Week " + week;
            data.add(new LineDataItem(date, MAX, roundedMax));
            data.add(new LineDataItem(date, VALUE, roundedCurrent));
            data.add(new LineDataItem(date, MIN, roundedMin));
        }
        return new LineDataDto(metricToMetricDtoConverter.convert(metric), data);
    }
}
