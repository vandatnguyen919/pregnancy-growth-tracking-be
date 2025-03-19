package com.pregnancy.edu.fetusinfo.metric;

import com.pregnancy.edu.fetusinfo.metric.converter.DtoToMetricResponseConverter;
import com.pregnancy.edu.fetusinfo.metric.converter.MetricDtoToMetricConverter;
import com.pregnancy.edu.fetusinfo.metric.converter.MetricToMetricDtoConverter;
import com.pregnancy.edu.fetusinfo.metric.dto.MetricDto;
import com.pregnancy.edu.fetusinfo.metric.dto.MetricResponse;
import com.pregnancy.edu.system.Result;
import com.pregnancy.edu.system.StatusCode;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/metrics")
public class MetricController {

    private final MetricService metricService;
    private final MetricToMetricDtoConverter metricToMetricDtoConverter;
    private final MetricDtoToMetricConverter metricDtoToMetricConverter;
    private final DtoToMetricResponseConverter dtoToMetricResponseConverter;

    public MetricController(MetricService metricService,
                            MetricToMetricDtoConverter metricToMetricDtoConverter,
                            MetricDtoToMetricConverter metricDtoToMetricConverter, DtoToMetricResponseConverter dtoToMetricResponseConverter) {
        this.metricService = metricService;
        this.metricToMetricDtoConverter = metricToMetricDtoConverter;
        this.metricDtoToMetricConverter = metricDtoToMetricConverter;
        this.dtoToMetricResponseConverter = dtoToMetricResponseConverter;
    }

    @GetMapping
    public Result getAllMetrics(Pageable pageable) {
        Page<Metric> metricPage = metricService.findAll(pageable);
        Page<MetricResponse> metricResponsePage = metricPage.map(metric -> {
            MetricDto metricDto = metricToMetricDtoConverter.convert(metric);
            return dtoToMetricResponseConverter.convert(metricDto);
        });
        return new Result(true, StatusCode.SUCCESS, "Find All Success", metricResponsePage);
    }

    @GetMapping("/{metricId}")
    public Result getMetricById(@PathVariable Long metricId) {
        Metric metric = metricService.findById(metricId);
        MetricDto metricDto = metricToMetricDtoConverter.convert(metric);
        MetricResponse metricResponse = dtoToMetricResponseConverter.convert(metricDto);
        return new Result(true, StatusCode.SUCCESS, "Find One Success", metricResponse);
    }

    @GetMapping("/week/{week}")
    public Result getMetricsByWeek(@PathVariable Integer week) {
        List<Metric> metrics = metricService.findAllByStandardWeek(week);
        List<MetricResponse> metricResponses = metrics.stream()
                .map(metricToMetricDtoConverter::convert)
                .map(metricDto -> dtoToMetricResponseConverter.convert(metricDto, week))
                .collect(Collectors.toList());
        return new Result(true, StatusCode.SUCCESS, "Find By Week Success", metricResponses);
    }

    @PostMapping
    public Result addMetric(@Valid @RequestBody MetricDto newMetricDto) {
        Metric newMetric = metricDtoToMetricConverter.convert(newMetricDto);
        Metric savedMetric = metricService.save(newMetric);
        MetricDto savedMetricDto = metricToMetricDtoConverter.convert(savedMetric);
        return new Result(true, StatusCode.SUCCESS, "Add Success", savedMetricDto);
    }

    @PutMapping("/{metricId}")
    public Result updateMetric(@PathVariable Long metricId, @Valid @RequestBody MetricDto metricDto) {
        Metric update = metricDtoToMetricConverter.convert(metricDto);
        Metric updatedMetric = metricService.update(metricId, update);
        MetricDto updatedMetricDto = metricToMetricDtoConverter.convert(updatedMetric);
        return new Result(true, StatusCode.SUCCESS, "Update Success", updatedMetricDto);
    }

    @DeleteMapping("/{metricId}")
    public Result deleteMetric(@PathVariable Long metricId) {
        this.metricService.delete(metricId);
        return new Result(true, StatusCode.SUCCESS, "Delete Success");
    }
}