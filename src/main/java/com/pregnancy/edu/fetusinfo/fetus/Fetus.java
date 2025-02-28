package com.pregnancy.edu.fetusinfo.fetus;

import com.pregnancy.edu.fetusinfo.fetusmetric.FetusMetric;
import com.pregnancy.edu.myuser.MyUser;
import com.pregnancy.edu.pregnancy.Pregnancy;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Fetus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private MyUser user;

    @ManyToOne
    @JoinColumn(name = "pregnancy_id")
    private Pregnancy pregnancy;

    private String nickName;

    private String gender;

    private Integer fetusNumber;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "fetus", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<FetusMetric> fetusMetrics = new ArrayList<>();
}