package com.pregnancy.edu.fetusinfo.metric;

import com.pregnancy.edu.system.common.base.BaseCrudService;
import com.pregnancy.edu.system.exception.ObjectNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MetricService implements BaseCrudService<Metric, Long> {

    private final MetricRepository metricRepository;

    public MetricService(MetricRepository metricRepository) {
        this.metricRepository = metricRepository;
    }

    @Override
    public List<Metric> findAll() {
        return metricRepository.findAll();
    }

    @Override
    public Metric findById(Long metricId) {
        return this.metricRepository.findById(metricId)
                .orElseThrow(() -> new ObjectNotFoundException("metric", metricId));
    }

    @Override
    public Metric save(Metric newMetric) {
        return this.metricRepository.save(newMetric);
    }

    @Override
    public Metric update(Long metricId, Metric metric) {
        return this.metricRepository.findById(metricId)
                .map(oldMetric -> {
                    oldMetric.setName(metric.getName());
                    oldMetric.setDataType(metric.getDataType());
                    oldMetric.setUnit(metric.getUnit());
                    return this.metricRepository.save(oldMetric);
                })
                .orElseThrow(() -> new ObjectNotFoundException("metric", metricId));
    }

    @Override
    public void delete(Long metricId) {
        this.metricRepository.findById(metricId)
                .orElseThrow(() -> new ObjectNotFoundException("metric", metricId));
        this.metricRepository.deleteById(metricId);
    }

    public Page<Metric> findAll(Pageable pageable) {
        return this.metricRepository.findAll(pageable);
    }

    public List<Metric> findAllByStandardWeek(Integer week) {
        return this.metricRepository.findAllByStandardsWeek(week);
    }
}