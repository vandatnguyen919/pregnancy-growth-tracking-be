package com.pregnancy.edu.fetusinfo.fetusmetric;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FetusMetricRepository extends JpaRepository<FetusMetric, Long> {
    Page<FetusMetric> findAllByFetusId(Long fetusId, Pageable pageable);
}