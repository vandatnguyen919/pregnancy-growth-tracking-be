package com.pregnancy.edu.fetusinfo.standard;

import com.pregnancy.edu.myuser.MyUser;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Standard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String region;
    private Double minWeightGrams;
    private Double maxWeightGrams;
    private Double minLengthCm;
    private Double maxLengthCm;
    private Double headCircumferenceMinCm;
    private Double headCircumferenceMaxCm;
    private Double abdominalCircumferenceMinCm;
    private Double abdominalCircumferenceMaxCm;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private Long createdById;

    @OneToOne(mappedBy = "standard")
    private MyUser user;
}