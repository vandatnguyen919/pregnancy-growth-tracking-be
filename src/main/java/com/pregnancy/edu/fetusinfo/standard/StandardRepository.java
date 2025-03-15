package com.pregnancy.edu.fetusinfo.standard;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StandardRepository extends JpaRepository<Standard, Long> {
    Page<Standard> findAllByMetricId(Long metricId, Pageable pageable);

    Page<Standard> findAllByWeek(Integer week, Pageable pageable);

    Standard findByMetricIdAndWeek(Long metricId, Integer week);
}