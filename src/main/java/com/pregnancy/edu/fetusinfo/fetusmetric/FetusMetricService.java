package com.pregnancy.edu.fetusinfo.fetusmetric;

import com.pregnancy.edu.fetusinfo.fetus.Fetus;
import com.pregnancy.edu.fetusinfo.fetus.FetusRepository;
import com.pregnancy.edu.fetusinfo.fetus.dto.MetricValueRequest;
import com.pregnancy.edu.fetusinfo.metric.Metric;
import com.pregnancy.edu.fetusinfo.metric.MetricRepository;
import com.pregnancy.edu.system.common.base.BaseCrudService;
import com.pregnancy.edu.system.exception.ObjectNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FetusMetricService implements BaseCrudService<FetusMetric, Long> {

    private final FetusMetricRepository fetusMetricRepository;
    private final FetusRepository fetusRepository;
    private final MetricRepository metricRepository;

    public FetusMetricService(FetusMetricRepository fetusMetricRepository, FetusRepository fetusRepository, MetricRepository metricRepository) {
        this.fetusMetricRepository = fetusMetricRepository;
        this.fetusRepository = fetusRepository;
        this.metricRepository = metricRepository;
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

            FetusMetric existingMetric = fetusMetricRepository.findByFetusMetricAndWeek(
                    fetus, metric, week);

            if (existingMetric != null) {
                existingMetric.setValue(request.value());
                fetusMetricRepository.save(existingMetric);
            } else {
                // Create new
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