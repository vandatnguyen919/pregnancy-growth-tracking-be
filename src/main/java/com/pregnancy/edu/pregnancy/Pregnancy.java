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
        LocalDate today = LocalDate.now();
        long daysBetween = ChronoUnit.DAYS.between(pregnancyStartDate, today);
        return (int) (daysBetween / 7) + 1; // Add 1 because first week is week 1, not 0
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
