package com.pregnancy.edu.pregnancy;

import com.pregnancy.edu.fetusinfo.fetus.Fetus;
import com.pregnancy.edu.myuser.MyUser;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Pregnancy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer maternalAge;
    private LocalDate pregnancyStartDate;
    private LocalDate estimatedDueDate;
    private LocalDate deliveryDate;
    private String status;

    @ManyToOne
    private MyUser user;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "pregnancy")
    private List<Fetus> fetuses = new ArrayList<>();

    public int getCurrentWeek() {
        LocalDate conceptionDate = estimatedDueDate.minusWeeks(40);
        // Calculate weeks pregnant
        return (int) ChronoUnit.WEEKS.between(conceptionDate, LocalDate.now());
    }

    public void addFetus(Fetus fetus) {
        fetuses.add(fetus);
        fetus.setPregnancy(this);
    }

    public void removeFetus(Fetus fetus) {
        fetuses.remove(fetus);
        fetus.setPregnancy(null);
    }
}
