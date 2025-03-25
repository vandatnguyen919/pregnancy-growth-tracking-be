package com.pregnancy.edu.fetusinfo.fetusmetric;

import com.pregnancy.edu.fetusinfo.fetus.Fetus;
import com.pregnancy.edu.fetusinfo.fetus.FetusRepository;
import com.pregnancy.edu.fetusinfo.fetusmetric.dto.FetusMetricDto;
import com.pregnancy.edu.fetusinfo.metric.Metric;
import com.pregnancy.edu.fetusinfo.metric.MetricRepository;
import com.pregnancy.edu.fetusinfo.standard.Standard;
import com.pregnancy.edu.fetusinfo.standard.StandardRepository;
import com.pregnancy.edu.system.exception.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FetusMetricServiceTest {

    @Mock
    FetusMetricRepository fetusMetricRepository;

    @Mock
    FetusRepository fetusRepository;

    @Mock
    MetricRepository metricRepository;

    @Mock
    StandardRepository standardRepository;

    @InjectMocks
    FetusMetricService fetusMetricService;

    private List<FetusMetric> fetusMetrics;
    private Fetus fetus;
    private Metric metric;

    @BeforeEach
    void setUp() {
        this.fetus = new Fetus();
        fetus.setId(1L);

        this.metric = new Metric();
        metric.setId(1L);
        metric.setName("Head Circumference");

        this.fetusMetrics = new ArrayList<>();

        FetusMetric fm1 = new FetusMetric();
        fm1.setId(1L);
        fm1.setFetus(fetus);
        fm1.setMetric(metric);
        fm1.setValue(10.5);
        fm1.setWeek(12);

        FetusMetric fm2 = new FetusMetric();
        fm2.setId(2L);
        fm2.setFetus(fetus);
        fm2.setMetric(metric);
        fm2.setValue(15.2);
        fm2.setWeek(12);

        this.fetusMetrics.add(fm1);
        this.fetusMetrics.add(fm2);
    }

    @Test
    void testFindAllSuccess() {
        // Given
        given(fetusMetricRepository.findAll()).willReturn(fetusMetrics);

        // When
        List<FetusMetric> foundMetrics = fetusMetricService.findAll();

        // Then
        assertThat(foundMetrics).hasSize(2);
        verify(fetusMetricRepository, times(1)).findAll();
    }

    @Test
    void testFindAllWithPaginationSuccess() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<FetusMetric> fetusMetricPage = new PageImpl<>(fetusMetrics, pageable, fetusMetrics.size());
        given(fetusMetricRepository.findAll(pageable)).willReturn(fetusMetricPage);

        // When
        Page<FetusMetric> foundMetrics = fetusMetricService.findAll(pageable);

        // Then
        assertThat(foundMetrics.getContent()).hasSize(2);
        verify(fetusMetricRepository, times(1)).findAll(pageable);
    }

    @Test
    void testFindByIdSuccess() {
        // Given
        given(fetusMetricRepository.findById(1L)).willReturn(Optional.of(fetusMetrics.get(0)));

        // When
        FetusMetric foundMetric = fetusMetricService.findById(1L);

        // Then
        assertThat(foundMetric.getId()).isEqualTo(1L);
        assertThat(foundMetric.getValue()).isEqualTo(10.5);
        verify(fetusMetricRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        // Given
        given(fetusMetricRepository.findById(10L)).willReturn(Optional.empty());

        // When
        ObjectNotFoundException ex = assertThrows(ObjectNotFoundException.class, () -> {
            fetusMetricService.findById(10L);
        });

        // Then
        assertThat(ex.getMessage()).contains("Could not find fetusMetric with Id 10");
        verify(fetusMetricRepository, times(1)).findById(10L);
    }

    @Test
    void testSaveSuccess() {
        // Given
        FetusMetric newMetric = new FetusMetric();
        newMetric.setFetus(fetus);
        newMetric.setMetric(metric);
        newMetric.setValue(12.3);
        newMetric.setWeek(12);

        given(fetusMetricRepository.save(newMetric)).willReturn(newMetric);

        // When
        FetusMetric savedMetric = fetusMetricService.save(newMetric);

        // Then
        assertThat(savedMetric.getValue()).isEqualTo(12.3);
        verify(fetusMetricRepository, times(1)).save(newMetric);
    }

    @Test
    void testUpdateSuccess() {
        // Given
        FetusMetric existingMetric = fetusMetrics.get(0);
        FetusMetric updateMetric = new FetusMetric();
        updateMetric.setFetus(fetus);
        updateMetric.setMetric(metric);
        updateMetric.setValue(15.0);
        updateMetric.setWeek(14);

        given(fetusMetricRepository.findById(1L)).willReturn(Optional.of(existingMetric));
        given(fetusMetricRepository.save(existingMetric)).willReturn(existingMetric);

        // When
        FetusMetric updatedMetric = fetusMetricService.update(1L, updateMetric);

        // Then
        assertThat(updatedMetric.getValue()).isEqualTo(15.0);
        assertThat(updatedMetric.getWeek()).isEqualTo(14);
        verify(fetusMetricRepository, times(1)).findById(1L);
        verify(fetusMetricRepository, times(1)).save(existingMetric);
    }

    @Test
    void testUpdateNotFound() {
        // Given
        FetusMetric updateMetric = new FetusMetric();
        given(fetusMetricRepository.findById(10L)).willReturn(Optional.empty());

        // When
        ObjectNotFoundException ex = assertThrows(ObjectNotFoundException.class, () -> {
            fetusMetricService.update(10L, updateMetric);
        });

        // Then
        assertThat(ex.getMessage()).contains("Could not find fetusMetric with Id 10");
        verify(fetusMetricRepository, times(1)).findById(10L);
    }

    @Test
    void testDeleteSuccess() {
        // Given
        given(fetusMetricRepository.findById(1L)).willReturn(Optional.of(fetusMetrics.get(0)));
        doNothing().when(fetusMetricRepository).deleteById(1L);

        // When
        fetusMetricService.delete(1L);

        // Then
        verify(fetusMetricRepository, times(1)).findById(1L);
        verify(fetusMetricRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteNotFound() {
        // Given
        given(fetusMetricRepository.findById(10L)).willReturn(Optional.empty());

        // When
        ObjectNotFoundException ex = assertThrows(ObjectNotFoundException.class, () -> {
            fetusMetricService.delete(10L);
        });

        // Then
        assertThat(ex.getMessage()).contains("Could not find fetusMetric with Id 10");
        verify(fetusMetricRepository, times(1)).findById(10L);
    }

    @Test
    void testSaveOrUpdateNewMetric() {
        // Given
        FetusMetricDto dto = new FetusMetricDto(null, 1L, 1L, 12.5);

        given(fetusMetricRepository.findByFetusIdAndMetricIdAndWeek(1L, 1L, 12))
                .willReturn(Optional.empty());
        given(fetusRepository.findById(1L)).willReturn(Optional.of(fetus));
        given(metricRepository.findById(1L)).willReturn(Optional.of(metric));
        given(fetusMetricRepository.save(any(FetusMetric.class))).willAnswer(invocation -> {
            FetusMetric savedMetric = invocation.getArgument(0);
            savedMetric.setId(3L);
            return savedMetric;
        });

        // When
        FetusMetric savedMetric = fetusMetricService.saveOrUpdate(dto, 12);

        // Then
        assertThat(savedMetric.getValue()).isEqualTo(12.5);
        assertThat(savedMetric.getFetus()).isEqualTo(fetus);
        assertThat(savedMetric.getMetric()).isEqualTo(metric);
        assertThat(savedMetric.getWeek()).isEqualTo(12);
        verify(fetusMetricRepository, times(1)).save(any(FetusMetric.class));
    }

    @Test
    void testSaveOrUpdateExistingMetric() {
        // Given
        FetusMetricDto dto = new FetusMetricDto(1L, 1L, 1L, 15.5);
        FetusMetric existingMetric = fetusMetrics.get(0);

        given(fetusMetricRepository.findByFetusIdAndMetricIdAndWeek(1L, 1L, 12))
                .willReturn(Optional.of(existingMetric));
        given(fetusMetricRepository.save(existingMetric)).willReturn(existingMetric);

        // When
        FetusMetric updatedMetric = fetusMetricService.saveOrUpdate(dto, 12);

        // Then
        assertThat(updatedMetric.getValue()).isEqualTo(15.5);
        verify(fetusMetricRepository, times(1)).save(existingMetric);
    }
}