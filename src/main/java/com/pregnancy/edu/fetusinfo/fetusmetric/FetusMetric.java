package com.pregnancy.edu.fetusinfo.fetusmetric;

import com.pregnancy.edu.fetusinfo.fetus.Fetus;
import com.pregnancy.edu.fetusinfo.metric.Metric;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class FetusMetric {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Fetus fetus;

    @ManyToOne
    private Metric metric;

    @Column(name = "metric_value")
    private Double value;

    private Integer week;
}
