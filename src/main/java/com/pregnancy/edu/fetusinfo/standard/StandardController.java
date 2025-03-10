package com.pregnancy.edu.fetusinfo.standard;

import com.pregnancy.edu.fetusinfo.metric.MetricService;
import com.pregnancy.edu.fetusinfo.standard.converter.StandardDtoToStandardConverter;
import com.pregnancy.edu.fetusinfo.standard.converter.StandardToStandardDtoConverter;
import com.pregnancy.edu.fetusinfo.standard.dto.StandardDto;
import com.pregnancy.edu.system.Result;
import com.pregnancy.edu.system.StatusCode;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/standards")
public class StandardController {

    private final StandardService standardService;
    private final MetricService metricService;
    private final StandardToStandardDtoConverter toStandardDtoConverter;
    private final StandardDtoToStandardConverter toStandardConverter;

    public StandardController(StandardService standardService, MetricService metricService,
                              StandardToStandardDtoConverter toStandardDtoConverter,
                              StandardDtoToStandardConverter toStandardConverter) {
        this.standardService = standardService;
        this.metricService = metricService;
        this.toStandardDtoConverter = toStandardDtoConverter;
        this.toStandardConverter = toStandardConverter;
    }

    @GetMapping
    public Result getAllStandards(Pageable pageable) {
        Page<Standard> standardPage = standardService.findAll(pageable);
        Page<StandardDto> standardDtoPage = standardPage.map(this.toStandardDtoConverter::convert);
        return new Result(true, StatusCode.SUCCESS, "Find All Success", standardDtoPage);
    }

    @GetMapping("/{standardId}")
    public Result getStandardById(@PathVariable Long standardId) {
        Standard standard = standardService.findById(standardId);
        StandardDto standardDto = toStandardDtoConverter.convert(standard);
        return new Result(true, StatusCode.SUCCESS, "Find One Success", standardDto);
    }

    @GetMapping("/metric/{metricId}")
    public Result getStandardsByMetricId(@PathVariable Long metricId, Pageable pageable) {
        Page<Standard> standardPage = standardService.findAllByMetricId(metricId, pageable);
        Page<StandardDto> standardDtoPage = standardPage.map(this.toStandardDtoConverter::convert);
        return new Result(true, StatusCode.SUCCESS, "Find By Metric Success", standardDtoPage);
    }

    @GetMapping("/week/{week}")
    public Result getStandardsByWeek(@PathVariable Integer week, Pageable pageable) {
        Page<Standard> standardPage = standardService.findAllByWeek(week, pageable);
        Page<StandardDto> standardDtoPage = standardPage.map(this.toStandardDtoConverter::convert);
        return new Result(true, StatusCode.SUCCESS, "Find By Week Success", standardDtoPage);
    }

    @PostMapping
    public Result addStandard(@Valid @RequestBody StandardDto newStandardDto) {
        Standard newStandard = toStandardConverter.convert(newStandardDto);
        newStandard.setMetric(metricService.findById(newStandardDto.metricId()));
        Standard savedStandard = standardService.save(newStandard);
        StandardDto savedStandardDto = toStandardDtoConverter.convert(savedStandard);
        return new Result(true, StatusCode.SUCCESS, "Add Success", savedStandardDto);
    }

    @PutMapping("/{standardId}")
    public Result updateStandard(@PathVariable Long standardId, @Valid @RequestBody StandardDto standardDto) {
        Standard update = toStandardConverter.convert(standardDto);
        update.setMetric(metricService.findById(standardDto.metricId()));
        Standard updatedStandard = standardService.update(standardId, update);
        StandardDto updatedStandardDto = toStandardDtoConverter.convert(updatedStandard);
        return new Result(true, StatusCode.SUCCESS, "Update Success", updatedStandardDto);
    }

    @DeleteMapping("/{standardId}")
    public Result deleteStandard(@PathVariable Long standardId) {
        this.standardService.delete(standardId);
        return new Result(true, StatusCode.SUCCESS, "Delete Success");
    }
}