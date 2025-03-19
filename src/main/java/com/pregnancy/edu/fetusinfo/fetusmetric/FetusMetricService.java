package com.pregnancy.edu.fetusinfo.fetusmetric;

import com.pregnancy.edu.fetusinfo.fetus.Fetus;
import com.pregnancy.edu.fetusinfo.fetus.FetusRepository;
import com.pregnancy.edu.fetusinfo.fetus.dto.MetricValueRequest;
import com.pregnancy.edu.fetusinfo.fetusmetric.dto.FetusMetricResponse;
import com.pregnancy.edu.fetusinfo.metric.Metric;
import com.pregnancy.edu.fetusinfo.metric.MetricRepository;
import com.pregnancy.edu.fetusinfo.standard.Standard;
import com.pregnancy.edu.fetusinfo.standard.StandardRepository;
import com.pregnancy.edu.system.common.base.BaseCrudService;
import com.pregnancy.edu.system.exception.ObjectNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class FetusMetricService implements BaseCrudService<FetusMetric, Long> {

    private final FetusMetricRepository fetusMetricRepository;
    private final FetusRepository fetusRepository;
    private final MetricRepository metricRepository;
    private final StandardRepository standardRepository;

    public FetusMetricService(FetusMetricRepository fetusMetricRepository, FetusRepository fetusRepository, MetricRepository metricRepository, StandardRepository standardRepository) {
        this.fetusMetricRepository = fetusMetricRepository;
        this.fetusRepository = fetusRepository;
        this.metricRepository = metricRepository;
        this.standardRepository = standardRepository;
    }

    @Override
    public List<FetusMetric> findAll() {
        return fetusMetricRepository.findAll();
    }

    public Page<FetusMetric> findAll(Pageable pageable) {
        return this.fetusMetricRepository.findAll(pageable);
    }

    public Page<FetusMetric> findAllByFetusId(Long fetusId, Pageable pageable) {
        return this.fetusMetricRepository.findAllByFetusId(fetusId, pageable);
    }

    public List<FetusMetric> findByFetusIdAndWeek(Long fetusId, int week) {
        return fetusMetricRepository.findByFetusIdAndWeek(fetusId, week);
    }

    public List<FetusMetric> findByFetusIdAndMetricId(Long fetusId, Long metricId) {
        return fetusMetricRepository.findByFetusIdAndMetricId(fetusId, metricId);
    }

    public FetusMetric findByFetusIdAndMetricIdAndWeek(Long fetusId, Long metricId, int week) {
        return fetusMetricRepository.findByFetusIdAndMetricIdAndWeek(fetusId, metricId, week).orElseThrow(
                () -> new ObjectNotFoundException("FetusMetric", ", fetusId: " + fetusId + ", metricId: " + metricId + ", week: " + week)
        );
    }

    public Set<Integer> findWeeksWithMetricsForFetus(Long fetusId) {
        fetusRepository.findById(fetusId)
                .orElseThrow(() -> new ObjectNotFoundException("Fetus", fetusId));

        return fetusMetricRepository.findDistinctWeeksByFetusId(fetusId);
    }

    public List<FetusMetricResponse> findFetusMetricResponsesByWeek(Long fetusId, Integer week) {
        fetusRepository.findById(fetusId)
                .orElseThrow(() -> new ObjectNotFoundException("Fetus", fetusId));

        List<FetusMetric> metrics = fetusMetricRepository.findByFetusIdAndWeek(fetusId, week);

        return metrics.stream()
                .map(metric -> {
                    Optional<Standard> standard = standardRepository.findByMetricIdAndWeek(
                            metric.getMetric().getId(), week);

                    Double min = standard.map(Standard::getMin).orElse(null);
                    Double max = standard.map(Standard::getMax).orElse(null);

                    return new FetusMetricResponse(
                            fetusId,
                            metric.getMetric().getName(),
                            metric.getValue(),
                            min,
                            max
                    );
                })
                .collect(Collectors.toList());
    }

    @Override
    public FetusMetric findById(Long fetusMetricId) {
        return this.fetusMetricRepository.findById(fetusMetricId)
                .orElseThrow(() -> new ObjectNotFoundException("fetusMetric", fetusMetricId));
    }

    @Override
    public FetusMetric save(FetusMetric newFetusMetric) {
        return this.fetusMetricRepository.save(newFetusMetric);
    }

    @Override
    public FetusMetric update(Long fetusMetricId, FetusMetric fetusMetric) {
        return this.fetusMetricRepository.findById(fetusMetricId)
                .map(oldFetusMetric -> {
                    oldFetusMetric.setFetus(fetusMetric.getFetus());
                    oldFetusMetric.setMetric(fetusMetric.getMetric());
                    oldFetusMetric.setValue(fetusMetric.getValue());
                    oldFetusMetric.setWeek(fetusMetric.getWeek());
                    return this.fetusMetricRepository.save(oldFetusMetric);
                })
                .orElseThrow(() -> new ObjectNotFoundException("fetusMetric", fetusMetricId));
    }

    @Override
    public void delete(Long fetusMetricId) {
        this.fetusMetricRepository.findById(fetusMetricId)
                .orElseThrow(() -> new ObjectNotFoundException("fetusMetric", fetusMetricId));
        this.fetusMetricRepository.deleteById(fetusMetricId);
    }

    public void saveMetricValues(Long fetusId, Integer week, List<MetricValueRequest> metricValues) {
        Fetus fetus = fetusRepository.findById(fetusId)
                .orElseThrow(() -> new ObjectNotFoundException("Fetus not found with ID: ", fetusId));

        for (MetricValueRequest request : metricValues) {
            Metric metric = metricRepository.findById(request.metricId())
                    .orElseThrow(() -> new ObjectNotFoundException("Metric not found with ID: ", request.metricId()));

            Optional<FetusMetric> existingMetricOpt =
                    fetusMetricRepository.findByFetusIdAndMetricIdAndWeek(fetusId, metric.getId(), week);

            if (existingMetricOpt.isPresent()) {
                FetusMetric existingMetric = existingMetricOpt.get();
                existingMetric.setValue(request.value());
                fetusMetricRepository.save(existingMetric);
            } else {
                FetusMetric newMetric = new FetusMetric();
                newMetric.setFetus(fetus);
                newMetric.setMetric(metric);
                newMetric.setValue(request.value());
                newMetric.setWeek(week);
                fetusMetricRepository.save(newMetric);
            }
        }
    }
}