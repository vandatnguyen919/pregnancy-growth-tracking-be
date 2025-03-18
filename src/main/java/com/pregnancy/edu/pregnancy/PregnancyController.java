package com.pregnancy.edu.pregnancy;

import com.pregnancy.edu.fetusinfo.fetus.Fetus;
import com.pregnancy.edu.fetusinfo.fetus.FetusService;
import com.pregnancy.edu.fetusinfo.fetus.converter.FetusDtoToFetusConverter;
import com.pregnancy.edu.fetusinfo.fetus.converter.FetusToFetusDtoConverter;
import com.pregnancy.edu.fetusinfo.fetus.dto.FetusDto;
import com.pregnancy.edu.myuser.MyUser;
import com.pregnancy.edu.myuser.UserService;
import com.pregnancy.edu.pregnancy.converter.PregnancyDtoToPregnancyConverter;
import com.pregnancy.edu.pregnancy.converter.PregnancyToPregnancyDtoConverter;
import com.pregnancy.edu.pregnancy.dto.PregnancyDto;
import com.pregnancy.edu.system.Result;
import com.pregnancy.edu.system.StatusCode;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/pregnancies")
public class PregnancyController {

    private final PregnancyService pregnancyService;
    private final FetusService fetusService;
    private final UserService userService;
    private final PregnancyToPregnancyDtoConverter pregnancyToDtoConverter;
    private final PregnancyDtoToPregnancyConverter dtoToPregnancyConverter;
    private final FetusToFetusDtoConverter fetusToDtoConverter;
    private final FetusDtoToFetusConverter dtoToFetusConverter;

    public PregnancyController(PregnancyService pregnancyService,
                               FetusService fetusService, UserService userService,
                               PregnancyToPregnancyDtoConverter pregnancyToDtoConverter,
                               PregnancyDtoToPregnancyConverter dtoToPregnancyConverter,
                               FetusToFetusDtoConverter fetusToDtoConverter,
                               FetusDtoToFetusConverter dtoToFetusConverter) {
        this.pregnancyService = pregnancyService;
        this.fetusService = fetusService;
        this.userService = userService;
        this.pregnancyToDtoConverter = pregnancyToDtoConverter;
        this.dtoToPregnancyConverter = dtoToPregnancyConverter;
        this.fetusToDtoConverter = fetusToDtoConverter;
        this.dtoToFetusConverter = dtoToFetusConverter;
    }

    @GetMapping("/me")
    public Result getCurrentPregnancy(@RequestParam(value = "status", required = false) String status, JwtAuthenticationToken jwtAuthenticationToken) {
        Jwt jwt = jwtAuthenticationToken.getToken();
        Long userId = jwt.getClaim("userId");
        List<Pregnancy> pregnancies = pregnancyService.findByUserId(userId);

        pregnancies.sort(Comparator.comparing(Pregnancy::getEstimatedDueDate));

        if(status != null) {
            List<Pregnancy> filtereds = pregnancies.stream()
                    .filter(pregnancy -> status.equals(pregnancy.getStatus()))
                    .toList();

            List<PregnancyDto> filteredDtos = filtereds.stream()
                    .map(this.pregnancyToDtoConverter::convert)
                    .toList();

            return new Result(true, StatusCode.SUCCESS, "Find Your Pregnancies successfully", filteredDtos);
        }

        List<PregnancyDto> pregnancyDtos = pregnancies.stream()
                .map(this.pregnancyToDtoConverter::convert)
                .toList();
        return new Result(true, StatusCode.SUCCESS, "Find Your Pregnancies successfully", pregnancyDtos);
    }

    @GetMapping
    public Result getAllPregnancies(Pageable pageable) {
        Page<Pregnancy> pregnancyPage = pregnancyService.findAll(pageable);
        Page<PregnancyDto> pregnancyDtoPage = pregnancyPage.map(this.pregnancyToDtoConverter::convert);
        return new Result(true, StatusCode.SUCCESS, "Find All Success", pregnancyDtoPage);
    }

    @GetMapping("/{pregnancyId}")
    public Result getPregnancyById(@PathVariable Long pregnancyId) {
        Pregnancy pregnancy = pregnancyService.findById(pregnancyId);
        PregnancyDto pregnancyDto = pregnancyToDtoConverter.convert(pregnancy);
        return new Result(true, StatusCode.SUCCESS, "Find One Success", pregnancyDto);
    }

    @GetMapping("/user/{userId}")
    public Result getPregnanciesByUserId(@PathVariable Long userId) {
        List<Pregnancy> pregnancies = pregnancyService.findByUserId(userId);
        List<PregnancyDto> pregnancyDtos = pregnancies.stream().map(this.pregnancyToDtoConverter::convert).toList();
        return new Result(true, StatusCode.SUCCESS, "Find By User Success", pregnancyDtos);
    }

    @PostMapping
    public Result addPregnancy(@Valid @RequestBody PregnancyDto newPregnancyDto, JwtAuthenticationToken jwtAuthenticationToken) {
        Jwt jwt = jwtAuthenticationToken.getToken();
        Long userId = jwt.getClaim("userId");
        Pregnancy newPregnancy = dtoToPregnancyConverter.convert(newPregnancyDto);
        newPregnancy.setUser(userService.findById(userId));
        newPregnancy.setFetuses(fetusService.findAllByUserId(userId));
        newPregnancy.setMaternalAge(newPregnancyDto.dueDate().getYear() - userService.findById(userId).getDateOfBirth().getYear());

        Pregnancy savedPregnancy = pregnancyService.save(newPregnancy);
        PregnancyDto savedPregnancyDto = pregnancyToDtoConverter.convert(savedPregnancy);
        return new Result(true, StatusCode.SUCCESS, "Add Success", savedPregnancyDto);
    }

    @PutMapping("/{pregnancyId}")
    public Result updatePregnancy(@PathVariable Long pregnancyId, @Valid @RequestBody PregnancyDto pregnancyDto, JwtAuthenticationToken jwtAuthenticationToken) {
        Jwt jwt = jwtAuthenticationToken.getToken();
        Long userId = jwt.getClaim("userId");
        Pregnancy update = dtoToPregnancyConverter.convert(pregnancyDto);
        assert update != null;
        update.setUser(userService.findById(userId));
        if(Objects.equals(update.getUser().getId(), userId)){
            update.setStatus(pregnancyDto.status());
            update.setFetuses(fetusService.findAllByUserId(userId));
            Pregnancy updatedPregnancy = pregnancyService.update(pregnancyId, update);
            PregnancyDto updatedPregnancyDto = pregnancyToDtoConverter.convert(updatedPregnancy);
            return new Result(true, StatusCode.SUCCESS, "Update Success", updatedPregnancyDto);
        }
        return null;
    }

    @DeleteMapping("/{pregnancyId}")
    public Result deletePregnancy(@PathVariable Long pregnancyId) {
        this.pregnancyService.delete(pregnancyId);
        return new Result(true, StatusCode.SUCCESS, "Delete Success");
    }

    @GetMapping("/{pregnancyId}/fetuses")
    public Result getFetusesForPregnancy(@PathVariable Long pregnancyId) {
        Pregnancy pregnancy = pregnancyService.findById(pregnancyId);
        List<FetusDto> fetusDtos = pregnancy.getFetuses().stream()
                .map(fetusToDtoConverter::convert)
                .collect(Collectors.toList());
        return new Result(true, StatusCode.SUCCESS, "Find Fetuses Success", fetusDtos);
    }

    @PostMapping("/{pregnancyId}/fetuses")
    public Result addFetusToPregnancy(@PathVariable Long pregnancyId, @Valid @RequestBody FetusDto fetusDto, JwtAuthenticationToken jwtAuthenticationToken) {
        Jwt jwt = jwtAuthenticationToken.getToken();
        Long userId = jwt.getClaim("userId");

        Fetus fetus = dtoToFetusConverter.convert(fetusDto);

        MyUser user = userService.findById(userId);
        fetus.setUser(user);

        Pregnancy updatedPregnancy = pregnancyService.addFetusToPregnancy(pregnancyId, fetus);
        PregnancyDto updatedPregnancyDto = pregnancyToDtoConverter.convert(updatedPregnancy);
        return new Result(true, StatusCode.SUCCESS, "Add Fetus Success", updatedPregnancyDto);
    }

    @DeleteMapping("/{pregnancyId}/fetuses/{fetusId}")
    public Result removeFetusFromPregnancy(@PathVariable Long pregnancyId, @PathVariable Long fetusId) {
        Pregnancy updatedPregnancy = pregnancyService.removeFetusFromPregnancy(pregnancyId, fetusId);
        PregnancyDto updatedPregnancyDto = pregnancyToDtoConverter.convert(updatedPregnancy);
        return new Result(true, StatusCode.SUCCESS, "Remove Fetus Success", updatedPregnancyDto);
    }
}