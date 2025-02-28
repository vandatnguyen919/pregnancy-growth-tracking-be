package com.pregnancy.edu.fetusinfo.fetus;

import com.pregnancy.edu.fetusinfo.fetus.dto.MetricValueRequest;
import com.pregnancy.edu.fetusinfo.fetusmetric.FetusMetric;
import com.pregnancy.edu.fetusinfo.fetusmetric.FetusMetricRepository;
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
public class FetusService implements BaseCrudService<Fetus, Long> {

    private final FetusRepository fetusRepository;
    private final MetricRepository metricRepository;
    private final FetusMetricRepository fetusMetricRepository;

    public FetusService(FetusRepository fetusRepository, MetricRepository metricRepository, FetusMetricRepository fetusMetricRepository) {
        this.fetusRepository = fetusRepository;
        this.metricRepository = metricRepository;
        this.fetusMetricRepository = fetusMetricRepository;
    }

    @Override
    public List<Fetus> findAll() {
        return fetusRepository.findAll();
    }

    @Override
    public Fetus findById(Long fetusId) {
        return this.fetusRepository.findById(fetusId)
                .orElseThrow(() -> new ObjectNotFoundException("fetus", fetusId));
    }

    @Override
    public Fetus save(Fetus newFetus) {
        return this.fetusRepository.save(newFetus);
    }

    @Override
    public Fetus update(Long fetusId, Fetus fetus) {
        return this.fetusRepository.findById(fetusId)
                .map(oldFetus -> {
                    oldFetus.setNickName(fetus.getNickName());
                    oldFetus.setGender(fetus.getGender());
                    oldFetus.setFetusNumber(fetus.getFetusNumber());
                    oldFetus.setUser(fetus.getUser());
                    oldFetus.setPregnancy(fetus.getPregnancy());
                    return this.fetusRepository.save(oldFetus);
                })
                .orElseThrow(() -> new ObjectNotFoundException("fetus", fetusId));
    }

    @Override
    public void delete(Long fetusId) {
        this.fetusRepository.findById(fetusId)
                .orElseThrow(() -> new ObjectNotFoundException("fetus", fetusId));
        this.fetusRepository.deleteById(fetusId);
    }

    public Page<Fetus> findAll(Pageable pageable) {
        return this.fetusRepository.findAll(pageable);
    }
}