package com.pregnancy.edu.fetusinfo.fetus;

import com.pregnancy.edu.fetusinfo.fetusgrowthrecord.FetusGrowthRecord;
import com.pregnancy.edu.myuser.MyUser;
import com.pregnancy.edu.pregnancy.Pregnancy;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Fetus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;
    private String gender;
    private Integer fetusNumber;
    private LocalDateTime createdAt;

    @ManyToOne
    private MyUser user;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "fetus")
    private List<FetusGrowthRecord> records;

    @ManyToOne
    private Pregnancy pregnancy;
}
