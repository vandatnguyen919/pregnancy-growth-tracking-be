package com.pregnancy.edu.fetusinfo.metric;

import com.pregnancy.edu.fetusinfo.metric.converter.DtoToMetricConverter;
import com.pregnancy.edu.fetusinfo.metric.converter.MetricToDtoConverter;
import com.pregnancy.edu.fetusinfo.metric.dto.MetricDto;
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
    private final MetricToDtoConverter metricToDtoConverter;
    private final DtoToMetricConverter dtoToMetricConverter;

    public MetricController(MetricService metricService,
                            MetricToDtoConverter metricToDtoConverter,
                            DtoToMetricConverter dtoToMetricConverter) {
        this.metricService = metricService;
        this.metricToDtoConverter = metricToDtoConverter;
        this.dtoToMetricConverter = dtoToMetricConverter;
    }

    @GetMapping
    public Result getAllMetrics(Pageable pageable) {
        Page<Metric> metricPage = metricService.findAll(pageable);
        Page<MetricDto> metricDtoPage = metricPage.map(this.metricToDtoConverter::convert);
        return new Result(true, StatusCode.SUCCESS, "Find All Success", metricDtoPage);
    }

    @GetMapping("/{metricId}")
    public Result getMetricById(@PathVariable Long metricId) {
        Metric metric = metricService.findById(metricId);
        MetricDto metricDto = metricToDtoConverter.convert(metric);
        return new Result(true, StatusCode.SUCCESS, "Find One Success", metricDto);
    }

    @GetMapping("/week/{week}")
    public Result getMetricsByWeek(@PathVariable Integer week) {
        List<Metric> metrics = metricService.findAllByStandardWeek(week);
        List<MetricDto> metricDtos = metrics.stream()
                .map(metricToDtoConverter::convert)
                .collect(Collectors.toList());
        return new Result(true, StatusCode.SUCCESS, "Find By Week Success", metricDtos);
    }

    @PostMapping
    public Result addMetric(@Valid @RequestBody MetricDto newMetricDto) {
        Metric newMetric = dtoToMetricConverter.convert(newMetricDto);
        Metric savedMetric = metricService.save(newMetric);
        MetricDto savedMetricDto = metricToDtoConverter.convert(savedMetric);
        return new Result(true, StatusCode.SUCCESS, "Add Success", savedMetricDto);
    }

    @PutMapping("/{metricId}")
    public Result updateMetric(@PathVariable Long metricId, @Valid @RequestBody MetricDto metricDto) {
        Metric update = dtoToMetricConverter.convert(metricDto);
        Metric updatedMetric = metricService.update(metricId, update);
        MetricDto updatedMetricDto = metricToDtoConverter.convert(updatedMetric);
        return new Result(true, StatusCode.SUCCESS, "Update Success", updatedMetricDto);
    }

    @DeleteMapping("/{metricId}")
    public Result deleteMetric(@PathVariable Long metricId) {
        this.metricService.delete(metricId);
        return new Result(true, StatusCode.SUCCESS, "Delete Success");
    }
}