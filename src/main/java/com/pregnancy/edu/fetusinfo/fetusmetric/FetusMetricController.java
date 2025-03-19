package com.pregnancy.edu.fetusinfo.fetusmetric;

import com.pregnancy.edu.fetusinfo.fetus.FetusService;
import com.pregnancy.edu.fetusinfo.fetusmetric.converter.DtoToFetusMetricConverter;
import com.pregnancy.edu.fetusinfo.fetusmetric.converter.DtoToFetusMetricResponseConverter;
import com.pregnancy.edu.fetusinfo.fetusmetric.converter.FetusMetricToDtoConverter;
import com.pregnancy.edu.fetusinfo.fetusmetric.dto.FetusMetricDto;
import com.pregnancy.edu.fetusinfo.fetusmetric.dto.FetusMetricResponse;
import com.pregnancy.edu.fetusinfo.metric.MetricService;
import com.pregnancy.edu.system.Result;
import com.pregnancy.edu.system.StatusCode;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/fetus-metrics")
public class FetusMetricController {

    private final FetusMetricService fetusMetricService;
    private final FetusMetricToDtoConverter toFetusMetricDtoConverter;
    private final DtoToFetusMetricConverter toFetusMetricConverter;
    private final DtoToFetusMetricResponseConverter dtoToFetusMetricResponseConverter;
    private final FetusService fetusService;
    private final MetricService metricService;

    public FetusMetricController(FetusMetricService fetusMetricService,
                                 FetusMetricToDtoConverter toFetusMetricDtoConverter,
                                 DtoToFetusMetricConverter toFetusMetricConverter, DtoToFetusMetricResponseConverter dtoToFetusMetricResponseConverter, FetusService fetusService, MetricService metricService) {
        this.fetusMetricService = fetusMetricService;
        this.toFetusMetricDtoConverter = toFetusMetricDtoConverter;
        this.toFetusMetricConverter = toFetusMetricConverter;
        this.dtoToFetusMetricResponseConverter = dtoToFetusMetricResponseConverter;
        this.fetusService = fetusService;
        this.metricService = metricService;
    }

    @GetMapping
    public Result getAllFetusMetrics(Pageable pageable) {
        Page<FetusMetric> fetusMetricPage = fetusMetricService.findAll(pageable);
        Page<FetusMetricDto> fetusMetricDtoPage = fetusMetricPage.map(this.toFetusMetricDtoConverter::convert);
        return new Result(true, StatusCode.SUCCESS, "Find All Success", fetusMetricDtoPage);
    }

    @GetMapping("/{fetusMetricId}")
    public Result getFetusMetricById(@PathVariable Long fetusMetricId) {
        FetusMetric fetusMetric = fetusMetricService.findById(fetusMetricId);
        FetusMetricDto fetusMetricDto = toFetusMetricDtoConverter.convert(fetusMetric);
        return new Result(true, StatusCode.SUCCESS, "Find One Success", fetusMetricDto);
    }

    @GetMapping("/fetus/{fetusId}")
    public Result getFetusMetricsByFetusId(@PathVariable Long fetusId, Pageable pageable) {
        Page<FetusMetric> fetusMetricPage = fetusMetricService.findAllByFetusId(fetusId, pageable);
        Page<FetusMetricDto> fetusMetricDtoPage = fetusMetricPage.map(this.toFetusMetricDtoConverter::convert);
        return new Result(true, StatusCode.SUCCESS, "Find By Fetus Success", fetusMetricDtoPage);
    }

    @PostMapping
    public Result addFetusMetric(@Valid @RequestBody List<FetusMetricDto> newFetusMetricDtos, @RequestParam Integer week) {
        List<FetusMetric> savedFetusMetrics = new ArrayList<>();

        for (FetusMetricDto dto : newFetusMetricDtos) {
            FetusMetric newFetusMetric = toFetusMetricConverter.convert(dto);
            newFetusMetric.setFetus(fetusService.findById(dto.fetusId()));
            newFetusMetric.setMetric(metricService.findById(dto.metricId()));
            newFetusMetric.setWeek(week);

            FetusMetric savedFetusMetric = fetusMetricService.save(newFetusMetric);
            savedFetusMetrics.add(savedFetusMetric);
        }

        List<FetusMetricDto> savedFetusMetricDtos = savedFetusMetrics.stream()
                .map(metric -> toFetusMetricDtoConverter.convert(metric))
                .collect(Collectors.toList());

        List<FetusMetricResponse> responses = savedFetusMetricDtos.stream()
                .map(dto -> dtoToFetusMetricResponseConverter.convert(dto, week))
                .collect(Collectors.toList());

        return new Result(true, StatusCode.SUCCESS, "Added " + responses.size() + " metrics for week " + week, responses);
    }

    @GetMapping("/fetus/{fetusId}/weeks")
    public Result getWeeksWithMetricsForFetus(@PathVariable Long fetusId) {
        Set<Integer> weeks = fetusMetricService.findWeeksWithMetricsForFetus(fetusId);
        return new Result(true, StatusCode.SUCCESS, "Find Weeks With Metrics Success", weeks);
    }

    @GetMapping("/fetus/{fetusId}/weeks/{week}")
    public Result getFetusMetricsByWeek(@PathVariable Long fetusId, @PathVariable Integer week) {
        List<FetusMetricResponse> responses = fetusMetricService.findFetusMetricResponsesByWeek(fetusId, week);
        return new Result(true, StatusCode.SUCCESS, "Find Metrics By Week Success", responses);
    }

    @PutMapping("/{fetusMetricId}")
    public Result updateFetusMetric(@PathVariable Long fetusMetricId, @Valid @RequestBody FetusMetricDto fetusMetricDto) {
        FetusMetric update = toFetusMetricConverter.convert(fetusMetricDto);
        update.setFetus(fetusService.findById(fetusMetricDto.fetusId()));
        update.setMetric(metricService.findById(fetusMetricDto.metricId()));
        FetusMetric updatedFetusMetric = fetusMetricService.update(fetusMetricId, update);
        FetusMetricDto updatedFetusMetricDto = toFetusMetricDtoConverter.convert(updatedFetusMetric);
        return new Result(true, StatusCode.SUCCESS, "Update Success", updatedFetusMetricDto);
    }

    @DeleteMapping("/{fetusMetricId}")
    public Result deleteFetusMetric(@PathVariable Long fetusMetricId) {
        this.fetusMetricService.delete(fetusMetricId);
        return new Result(true, StatusCode.SUCCESS, "Delete Success");
    }
}