package com.pregnancy.edu.fetusinfo.fetus;

import com.pregnancy.edu.fetusinfo.fetusmetric.FetusMetricRepository;
import com.pregnancy.edu.fetusinfo.metric.MetricRepository;
import com.pregnancy.edu.myuser.UserRepository;
import com.pregnancy.edu.system.common.base.BaseCrudService;
import com.pregnancy.edu.system.exception.ObjectNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FetusService implements BaseCrudService<Fetus, Long> {

    private final FetusRepository fetusRepository;
    private final UserRepository userRepository;

    public FetusService(FetusRepository fetusRepository, MetricRepository metricRepository, UserRepository userRepository, FetusMetricRepository fetusMetricRepository) {
        this.fetusRepository = fetusRepository;
        this.userRepository = userRepository;
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

    public List<Fetus> findAllByUserId(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("user", userId));

        return this.fetusRepository.findAllByUserId(userId);
    }

}