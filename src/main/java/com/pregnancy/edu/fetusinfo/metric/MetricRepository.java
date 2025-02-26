package com.pregnancy.edu.fetusinfo.metric;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MetricRepository extends JpaRepository<Metric, Long> {
    @Query("SELECT DISTINCT m FROM Metric m JOIN m.standards s WHERE s.week = :week")
    List<Metric> findAllByStandardsWeek(@Param("week") Integer week);
}
