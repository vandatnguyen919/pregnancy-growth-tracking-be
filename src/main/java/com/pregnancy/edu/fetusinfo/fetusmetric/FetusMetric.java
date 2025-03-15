package com.pregnancy.edu.fetusinfo.fetusmetric;

import com.pregnancy.edu.fetusinfo.fetus.Fetus;
import com.pregnancy.edu.fetusinfo.metric.Metric;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = {"fetus_id", "metric_id", "week"},
                     name = "uk_fetus_metric_week")
})
public class FetusMetric {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "fetus_id")
    private Fetus fetus;

    @ManyToOne
    @JoinColumn(name = "metric_id")
    private Metric metric;

    private Integer week;

    @Column(name="fetus_metric_value")
    private Double value;
}
