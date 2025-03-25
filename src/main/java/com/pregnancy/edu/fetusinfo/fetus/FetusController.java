package com.pregnancy.edu.fetusinfo.fetus;

import com.pregnancy.edu.fetusinfo.fetus.converter.FetusDtoToFetusConverter;
import com.pregnancy.edu.fetusinfo.fetus.converter.FetusToFetusDtoConverter;
import com.pregnancy.edu.fetusinfo.fetus.dto.FetusDto;
import com.pregnancy.edu.fetusinfo.fetus.dto.FetusWeekMetricsResponse;
import com.pregnancy.edu.fetusinfo.fetus.helper.FetusHelper;
import com.pregnancy.edu.fetusinfo.metric.Metric;
import com.pregnancy.edu.fetusinfo.metric.MetricService;
import com.pregnancy.edu.myuser.UserService;
import com.pregnancy.edu.pregnancy.Pregnancy;
import com.pregnancy.edu.pregnancy.PregnancyService;
import com.pregnancy.edu.system.Result;
import com.pregnancy.edu.system.StatusCode;
import com.pregnancy.edu.system.exception.ObjectNotFoundException;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/fetuses")
public class FetusController {

    private final FetusService fetusService;
    private final MetricService metricService;
    private final FetusToFetusDtoConverter fetusToDtoConverter;
    private final FetusDtoToFetusConverter dtoToFetusConverter;
    private final FetusHelper fetusHelper;
    private final UserService userService;
    private final PregnancyService pregnancyService;

    public FetusController(FetusService fetusService, MetricService metricService,
                           FetusToFetusDtoConverter fetusToDtoConverter,
                           FetusDtoToFetusConverter dtoToFetusConverter, UserService userService, PregnancyService pregnancyService) {
        this.fetusService = fetusService;
        this.metricService = metricService;
        this.fetusToDtoConverter = fetusToDtoConverter;
        this.dtoToFetusConverter = dtoToFetusConverter;
        this.fetusHelper = new FetusHelper();
        this.userService = userService;
        this.pregnancyService = pregnancyService;
    }

    @GetMapping("/user/{userId}")
    public Result getAllFetusOfUser(@PathVariable Long userId) {
        List<Fetus> fetusList = fetusService.findAllByUserId(userId);
        List<Fetus> processedFetusList = fetusList.stream()
                .map(fetus -> {
                    if (fetus.getPregnancy() != null) {
                        try {
                            Pregnancy pregnancy = pregnancyService.findById(fetus.getPregnancy().getId());
                            fetus.setPregnancy(pregnancy);
                        } catch (ObjectNotFoundException e) {
                            fetus.setPregnancy(null);
                        }
                    }
                    return fetus;
                })
                .collect(Collectors.toList());

        List<FetusDto> fetusDtoList = processedFetusList.stream()
                .map(this.fetusToDtoConverter::convert)
                .collect(Collectors.toList());
        return new Result(true, StatusCode.SUCCESS, "Find All User's Fetuses Success", fetusDtoList);
    }


    @GetMapping("/{fetusId}")
    public Result getFetusById(@PathVariable Long fetusId) {
        Fetus fetus = fetusService.findById(fetusId);
        FetusDto fetusDto = fetusToDtoConverter.convert(fetus);
        return new Result(true, StatusCode.SUCCESS, "Find One Success", fetusDto);
    }

    @PostMapping
    public Result addFetus(@Valid @RequestBody FetusDto newFetusDto) {
        Fetus newFetus = dtoToFetusConverter.convert(newFetusDto);
        newFetus.setUser(userService.findById(newFetusDto.userId()));
        newFetus.setPregnancy(pregnancyService.findById(newFetusDto.pregnancyId()));
        Fetus savedFetus = fetusService.save(newFetus);
        FetusDto savedFetusDto = fetusToDtoConverter.convert(savedFetus);
        return new Result(true, StatusCode.SUCCESS, "Add Success", savedFetusDto);
    }

    @PutMapping("/{fetusId}")
    public Result updateFetus(@PathVariable Long fetusId, @Valid @RequestBody FetusDto fetusDto) {
        Fetus update = dtoToFetusConverter.convert(fetusDto);
        update.setUser(userService.findById(fetusDto.userId()));
        update.setPregnancy(pregnancyService.findById(fetusDto.pregnancyId()));
        Fetus updatedFetus = fetusService.update(fetusId, update);
        FetusDto updatedFetusDto = fetusToDtoConverter.convert(updatedFetus);
        return new Result(true, StatusCode.SUCCESS, "Update Success", updatedFetusDto);
    }

    @DeleteMapping("/{fetusId}")
    public Result deleteFetus(@PathVariable Long fetusId) {
        this.fetusService.delete(fetusId);
        return new Result(true, StatusCode.SUCCESS, "Delete Success");
    }

    @GetMapping("/{fetusId}/week/{week}")
    public Result getFetusMetricsForWeek(@PathVariable Long fetusId, @PathVariable Integer week) {
        Fetus fetus = fetusService.findById(fetusId);

        List<Metric> metrics = metricService.findAllByStandardWeek(week);

        FetusWeekMetricsResponse response = fetusHelper.buildFetusWeekResponse(fetus, metrics, week);

        return new Result(true, StatusCode.SUCCESS,
                "Fetus metrics for week " + week + " retrieved successfully", response);
    }
}
