package com.pregnancy.edu.fetusinfo.fetusmetric;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pregnancy.edu.fetusinfo.fetus.Fetus;
import com.pregnancy.edu.fetusinfo.fetus.FetusService;
import com.pregnancy.edu.fetusinfo.fetusmetric.converter.DtoToFetusMetricConverter;
import com.pregnancy.edu.fetusinfo.fetusmetric.converter.DtoToFetusMetricResponseConverter;
import com.pregnancy.edu.fetusinfo.fetusmetric.converter.FetusMetricToDtoConverter;
import com.pregnancy.edu.fetusinfo.fetusmetric.dto.FetusMetricDto;
import com.pregnancy.edu.fetusinfo.fetusmetric.dto.FetusMetricResponse;
import com.pregnancy.edu.fetusinfo.metric.Metric;
import com.pregnancy.edu.fetusinfo.metric.MetricService;
import com.pregnancy.edu.system.StatusCode;
import com.pregnancy.edu.system.exception.ObjectNotFoundException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class FetusMetricControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    FetusMetricService fetusMetricService;

    @MockitoBean
    FetusMetricToDtoConverter toFetusMetricDtoConverter;

    @MockitoBean
    DtoToFetusMetricConverter toFetusMetricConverter;

    @MockitoBean
    DtoToFetusMetricResponseConverter dtoToFetusMetricResponseConverter;

    @MockitoBean
    FetusService fetusService;

    @MockitoBean
    MetricService metricService;

    private List<FetusMetric> fetusMetrics;
    private String baseUrl = "http://localhost:8080/api/v1";

    @BeforeEach
    void setUp() {
        this.fetusMetrics = new ArrayList<>();

        Fetus fetus = new Fetus();
        fetus.setId(1L);

        Metric metric1 = new Metric();
        metric1.setId(1L);
        metric1.setName("Head Circumference");

        Metric metric2 = new Metric();
        metric2.setId(2L);
        metric2.setName("Femur Length");

        FetusMetric fm1 = new FetusMetric();
        fm1.setId(1L);
        fm1.setFetus(fetus);
        fm1.setMetric(metric1);
        fm1.setValue(10.5);
        fm1.setWeek(12);

        FetusMetric fm2 = new FetusMetric();
        fm2.setId(2L);
        fm2.setFetus(fetus);
        fm2.setMetric(metric2);
        fm2.setValue(15.2);
        fm2.setWeek(12);

        this.fetusMetrics.add(fm1);
        this.fetusMetrics.add(fm2);
    }

    @Test
    void testGetAllFetusMetricsSuccess() throws Exception {
        // Given
        Page<FetusMetric> fetusMetricPage = new PageImpl<>(fetusMetrics);
        given(fetusMetricService.findAll(any(Pageable.class))).willReturn(fetusMetricPage);

        // Create sample DTOs for mocking converter
        List<FetusMetricDto> dtos = fetusMetrics.stream()
                .map(fm -> new FetusMetricDto(
                        fm.getId(),
                        fm.getFetus().getId(),
                        fm.getMetric().getId(),
                        fm.getValue()
                ))
                .toList();

        for (int i = 0; i < fetusMetrics.size(); i++) {
            given(toFetusMetricDtoConverter.convert(fetusMetrics.get(i))).willReturn(dtos.get(i));
        }

        // When and then
        mockMvc.perform(get(baseUrl + "/fetus-metrics").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data.content", Matchers.hasSize(2)))
                .andExpect(jsonPath("$.data.content[0].fetusId").value(1L))
                .andExpect(jsonPath("$.data.content[0].metricId").value(1L));
    }

    @Test
    void testGetFetusMetricByIdSuccess() throws Exception {
        // Given
        FetusMetric fetusMetric = fetusMetrics.get(0);
        given(fetusMetricService.findById(1L)).willReturn(fetusMetric);

        FetusMetricDto dto = new FetusMetricDto(
                fetusMetric.getId(),
                fetusMetric.getFetus().getId(),
                fetusMetric.getMetric().getId(),
                fetusMetric.getValue()
        );
        given(toFetusMetricDtoConverter.convert(fetusMetric)).willReturn(dto);

        // When and then
        mockMvc.perform(get(baseUrl + "/fetus-metrics/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.fetusId").value(1L));
    }

    @Test
    void testGetFetusMetricByIdNotFound() throws Exception {
        // Given
        given(fetusMetricService.findById(10L)).willThrow(new ObjectNotFoundException("fetusMetric", 10L));

        // When and then
        mockMvc.perform(get(baseUrl + "/fetus-metrics/10").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find fetusMetric with Id 10 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testAddFetusMetricSuccess() throws Exception {
        // Given
        FetusMetricDto newDto = new FetusMetricDto(null, 1L, 1L, 10.5);
        FetusMetric savedFetusMetric = fetusMetrics.get(0);
        FetusMetricDto savedDto = new FetusMetricDto(
                savedFetusMetric.getId(),
                savedFetusMetric.getFetus().getId(),
                savedFetusMetric.getMetric().getId(),
                savedFetusMetric.getValue()
        );
        FetusMetricResponse response = new FetusMetricResponse(1L, "Head Circumference", 10.5);

        given(fetusMetricService.saveOrUpdate(any(FetusMetricDto.class), eq(12))).willReturn(savedFetusMetric);
        given(toFetusMetricDtoConverter.convert(savedFetusMetric)).willReturn(savedDto);
        given(dtoToFetusMetricResponseConverter.convert(savedDto, 12)).willReturn(response);

        String json = objectMapper.writeValueAsString(List.of(newDto));

        // When and then
        mockMvc.perform(post(baseUrl + "/fetus-metrics")
                        .param("week", "12")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Added 1 metrics for week 12"))
                .andExpect(jsonPath("$.data[0].fetusId").value(1L));
    }

    @Test
    void testUpdateFetusMetricSuccess() throws Exception {
        // Given
        FetusMetricDto updateDto = new FetusMetricDto(1L, 1L, 1L, 11.0);
        FetusMetric updatedFetusMetric = fetusMetrics.get(0);
        updatedFetusMetric.setValue(11.0);

        Fetus fetus = new Fetus();
        fetus.setId(1L);
        Metric metric = new Metric();
        metric.setId(1L);

        given(fetusService.findById(1L)).willReturn(fetus);
        given(metricService.findById(1L)).willReturn(metric);
        given(toFetusMetricConverter.convert(updateDto)).willReturn(updatedFetusMetric);
        given(fetusMetricService.update(eq(1L), any(FetusMetric.class))).willReturn(updatedFetusMetric);

        FetusMetricDto updatedDto = new FetusMetricDto(
                updatedFetusMetric.getId(),
                updatedFetusMetric.getFetus().getId(),
                updatedFetusMetric.getMetric().getId(),
                updatedFetusMetric.getValue()
        );
        given(toFetusMetricDtoConverter.convert(updatedFetusMetric)).willReturn(updatedDto);

        String json = objectMapper.writeValueAsString(updateDto);

        // When and then
        mockMvc.perform(put(baseUrl + "/fetus-metrics/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update Success"))
                .andExpect(jsonPath("$.data.value").value(11.0));
    }

    @Test
    void testDeleteFetusMetricSuccess() throws Exception {
        // Given
        doNothing().when(fetusMetricService).delete(1L);

        // When and then
        mockMvc.perform(delete(baseUrl + "/fetus-metrics/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete Success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}