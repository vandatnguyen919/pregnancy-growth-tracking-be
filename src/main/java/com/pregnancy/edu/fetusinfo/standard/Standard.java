package com.pregnancy.edu.fetusinfo.standard;

import com.pregnancy.edu.fetusinfo.metric.Metric;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Standard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "metric_id")
    private Metric metric;

    private Integer week;

    private Double min;

    private Double max;
}