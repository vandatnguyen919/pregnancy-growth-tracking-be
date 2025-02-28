package com.pregnancy.edu.fetusinfo.fetusmetric;

import com.pregnancy.edu.fetusinfo.fetusmetric.converter.DtoToFetusMetricConverter;
import com.pregnancy.edu.fetusinfo.fetusmetric.converter.FetusMetricToDtoConverter;
import com.pregnancy.edu.fetusinfo.fetusmetric.dto.FetusMetricDto;
import com.pregnancy.edu.system.Result;
import com.pregnancy.edu.system.StatusCode;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/fetus-metrics")
public class FetusMetricController {

    private final FetusMetricService fetusMetricService;
    private final FetusMetricToDtoConverter toFetusMetricDtoConverter;
    private final DtoToFetusMetricConverter toFetusMetricConverter;

    public FetusMetricController(FetusMetricService fetusMetricService,
                                 FetusMetricToDtoConverter toFetusMetricDtoConverter,
                                 DtoToFetusMetricConverter toFetusMetricConverter) {
        this.fetusMetricService = fetusMetricService;
        this.toFetusMetricDtoConverter = toFetusMetricDtoConverter;
        this.toFetusMetricConverter = toFetusMetricConverter;
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
    public Result addFetusMetric(@Valid @RequestBody FetusMetricDto newFetusMetricDto) {
        FetusMetric newFetusMetric = toFetusMetricConverter.convert(newFetusMetricDto);
        FetusMetric savedFetusMetric = fetusMetricService.save(newFetusMetric);
        FetusMetricDto savedFetusMetricDto = toFetusMetricDtoConverter.convert(savedFetusMetric);
        return new Result(true, StatusCode.SUCCESS, "Add Success", savedFetusMetricDto);
    }

    @PutMapping("/{fetusMetricId}")
    public Result updateFetusMetric(@PathVariable Long fetusMetricId, @Valid @RequestBody FetusMetricDto fetusMetricDto) {
        FetusMetric update = toFetusMetricConverter.convert(fetusMetricDto);
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