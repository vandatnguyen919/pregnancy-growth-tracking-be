package com.pregnancy.edu.fetusinfo.fetusmetric;

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

    public FetusMetricService(FetusMetricRepository fetusMetricRepository) {
        this.fetusMetricRepository = fetusMetricRepository;
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
}