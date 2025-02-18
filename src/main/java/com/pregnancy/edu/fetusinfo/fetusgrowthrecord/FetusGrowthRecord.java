package com.pregnancy.edu.fetusinfo.fetusgrowthrecord;

import com.pregnancy.edu.fetusinfo.fetus.Fetus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class FetusGrowthRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer weekNumber;
    private Double fetalWeightGrams;
    private Double fetalLengthCm;
    private Integer fetalHeartRate;
    private String symptoms;
    private String diagnosis;
    private LocalDateTime createdAt;

    @ManyToOne
    private Fetus fetus;
}