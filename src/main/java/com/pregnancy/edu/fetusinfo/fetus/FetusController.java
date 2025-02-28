package com.pregnancy.edu.fetusinfo.fetus;

import com.pregnancy.edu.fetusinfo.fetus.converter.FetusDtoToFetusConverter;
import com.pregnancy.edu.fetusinfo.fetus.converter.FetusToFetusDtoConverter;
import com.pregnancy.edu.fetusinfo.fetus.dto.FetusDto;
import com.pregnancy.edu.system.Result;
import com.pregnancy.edu.system.StatusCode;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/fetuses")
public class FetusController {

    private final FetusService fetusService;
    private final FetusToFetusDtoConverter fetusToDtoConverter;
    private final FetusDtoToFetusConverter dtoToFetusConverter;

    public FetusController(FetusService fetusService,
                           FetusToFetusDtoConverter fetusToDtoConverter,
                           FetusDtoToFetusConverter dtoToFetusConverter) {
        this.fetusService = fetusService;
        this.fetusToDtoConverter = fetusToDtoConverter;
        this.dtoToFetusConverter = dtoToFetusConverter;
    }

    @GetMapping
    public Result getAllFetuses(Pageable pageable) {
        Page<Fetus> fetusPage = fetusService.findAll(pageable);
        Page<FetusDto> fetusDtoPage = fetusPage.map(this.fetusToDtoConverter::convert);
        return new Result(true, StatusCode.SUCCESS, "Find All Success", fetusDtoPage);
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
        Fetus savedFetus = fetusService.save(newFetus);
        FetusDto savedFetusDto = fetusToDtoConverter.convert(savedFetus);
        return new Result(true, StatusCode.SUCCESS, "Add Success", savedFetusDto);
    }

    @PutMapping("/{fetusId}")
    public Result updateFetus(@PathVariable Long fetusId, @Valid @RequestBody FetusDto fetusDto) {
        Fetus update = dtoToFetusConverter.convert(fetusDto);
        Fetus updatedFetus = fetusService.update(fetusId, update);
        FetusDto updatedFetusDto = fetusToDtoConverter.convert(updatedFetus);
        return new Result(true, StatusCode.SUCCESS, "Update Success", updatedFetusDto);
    }

    @DeleteMapping("/{fetusId}")
    public Result deleteFetus(@PathVariable Long fetusId) {
        this.fetusService.delete(fetusId);
        return new Result(true, StatusCode.SUCCESS, "Delete Success");
    }
}
