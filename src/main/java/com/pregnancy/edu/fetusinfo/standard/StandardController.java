package com.pregnancy.edu.fetusinfo.standard;

import com.pregnancy.edu.fetusinfo.metric.MetricService;
import com.pregnancy.edu.fetusinfo.standard.converter.StandardDtoToStandardConverter;
import com.pregnancy.edu.fetusinfo.standard.converter.StandardToStandardDtoConverter;
import com.pregnancy.edu.fetusinfo.standard.dto.StandardDto;
import com.pregnancy.edu.system.Result;
import com.pregnancy.edu.system.StatusCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
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

    @Operation(
            summary = "Get all standards",
            description = "Retrieves a paginated list of standards, with optional filtering by week"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Standards retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Result.class)))
    })
    @GetMapping
    public Result getAllStandards(
            @Parameter(description = "Optional parameter to filter standards by specific week")
            @RequestParam(required = false) Integer week,
            @Parameter(description = "Page number (zero-based)")
            @RequestParam(required = false, defaultValue = "0") int page,
            @Parameter(description = "Page size")
            @RequestParam(required = false, defaultValue = "20") int size,
            @Parameter(description = "Sort field and direction (e.g. week,asc)")
            @RequestParam(required = false) String sort,
            @Parameter(hidden = true) Pageable pageable) {
        Page<Standard> standardPage;
        if (week == null) {
            standardPage = standardService.findAll(pageable);
        } else {
            standardPage = standardService.findAllByWeek(week, pageable);
        }
        Page<StandardDto> standardDtoPage = standardPage.map(this.toStandardDtoConverter::convert);
        return new Result(true, StatusCode.SUCCESS, "Find Success", standardDtoPage);
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