package com.pregnancy.edu.fetusinfo.standard;

import com.pregnancy.edu.system.common.base.BaseCrudService;
import com.pregnancy.edu.system.exception.ObjectNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StandardService implements BaseCrudService<Standard, Long> {

    private final StandardRepository standardRepository;

    public StandardService(StandardRepository standardRepository) {
        this.standardRepository = standardRepository;
    }

    @Override
    public List<Standard> findAll() {
        return standardRepository.findAll();
    }

    public Page<Standard> findAll(Pageable pageable) {
        return this.standardRepository.findAll(pageable);
    }

    public Page<Standard> findAllByMetricId(Long metricId, Pageable pageable) {
        return this.standardRepository.findAllByMetricId(metricId, pageable);
    }

    public Page<Standard> findAllByWeek(Integer week, Pageable pageable) {
        return this.standardRepository.findAllByWeek(week, pageable);
    }

    @Override
    public Standard findById(Long standardId) {
        return this.standardRepository.findById(standardId)
                .orElseThrow(() -> new ObjectNotFoundException("standard", standardId));
    }

    @Override
    public Standard save(Standard newStandard) {
        return this.standardRepository.save(newStandard);
    }

    @Override
    public Standard update(Long standardId, Standard standard) {
        return this.standardRepository.findById(standardId)
                .map(oldStandard -> {
                    oldStandard.setMetric(standard.getMetric());
                    oldStandard.setWeek(standard.getWeek());
                    oldStandard.setMin(standard.getMin());
                    oldStandard.setMax(standard.getMax());
                    return this.standardRepository.save(oldStandard);
                })
                .orElseThrow(() -> new ObjectNotFoundException("standard", standardId));
    }

    @Override
    public void delete(Long standardId) {
        this.standardRepository.findById(standardId)
                .orElseThrow(() -> new ObjectNotFoundException("standard", standardId));
        this.standardRepository.deleteById(standardId);
    }

    public Standard findByMetricIdAndWeek(Long metricId, Integer week) {
        return standardRepository.findByMetricIdAndWeek(metricId, week).orElseThrow(
                () -> new ObjectNotFoundException("standard", "metricId: " + metricId + ", week: " + week)
        );
    }

}