package com.pregnancy.edu.fetusinfo.standard;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StandardRepository extends JpaRepository<Standard, Long> {
    Page<Standard> findAllByMetricId(Long metricId, Pageable pageable);
    Page<Standard> findAllByWeek(Integer week, Pageable pageable);
}