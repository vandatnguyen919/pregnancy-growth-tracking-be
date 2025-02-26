package com.pregnancy.edu.fetusinfo.metric;

import com.pregnancy.edu.fetusinfo.fetusmetric.FetusMetric;
import com.pregnancy.edu.fetusinfo.standard.Standard;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Metric {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String dataType;

    private String unit;

    @OneToMany(mappedBy = "metric", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<FetusMetric> fetusMetrics = new ArrayList<>();

    @OneToMany(mappedBy = "metric", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Standard> standards = new ArrayList<>();
}