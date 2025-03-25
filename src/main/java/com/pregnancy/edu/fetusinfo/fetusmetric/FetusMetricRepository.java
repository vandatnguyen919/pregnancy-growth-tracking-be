package com.pregnancy.edu.fetusinfo.fetusmetric;

import com.pregnancy.edu.fetusinfo.fetus.Fetus;
import com.pregnancy.edu.fetusinfo.metric.Metric;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface FetusMetricRepository extends JpaRepository<FetusMetric, Long> {
    Page<FetusMetric> findAllByFetusId(Long fetusId, Pageable pageable);

    @Query("SELECT fm FROM FetusMetric fm WHERE fm.fetus = :fetus AND fm.metric = :metric AND fm.week = :week")
    FetusMetric findByFetusMetricAndWeek(
            @Param("fetus") Fetus fetus,
            @Param("metric") Metric metric,
            @Param("week") Integer week);

    List<FetusMetric> findByFetusIdAndWeek(Long fetusId, Integer week);

    List<FetusMetric> findByFetusIdAndMetricId(Long fetusId, Long metricId);

    Optional<FetusMetric> findByFetusIdAndMetricIdAndWeek(Long fetusId, Long metricId, Integer week);

    @Query("SELECT DISTINCT fm.week FROM FetusMetric fm WHERE fm.fetus.id = :fetusId ORDER BY fm.week")
    Set<Integer> findDistinctWeeksByFetusId(@Param("fetusId") Long fetusId);

    @Query("SELECT DISTINCT fm.metric.id FROM FetusMetric fm WHERE fm.fetus.id = :fetusId")
    List<Long> findDistinctMetricIdsByFetusId(@Param("fetusId") Long fetusId);
}