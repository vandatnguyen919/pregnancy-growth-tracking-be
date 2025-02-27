package com.pregnancy.edu.fetusinfo.standard;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StandardRepository extends JpaRepository<Standard, Long> {
    Page<Standard> findAllByMetricId(Long metricId, Pageable pageable);

    Page<Standard> findAllByWeek(Integer week, Pageable pageable);

    @Query("SELECT s FROM Standard s WHERE s.metric.id = :metricId AND s.week = :week")
    Standard findByMetricIdAndWeek(@Param("metricId") Long metricId, @Param("week") Integer week);
}