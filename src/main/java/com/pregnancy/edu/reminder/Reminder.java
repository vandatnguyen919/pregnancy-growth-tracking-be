package com.pregnancy.edu.reminder;

import com.pregnancy.edu.myuser.MyUser;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Reminder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reminderId;

    private String reminderType;
    private String description;
    private LocalDateTime reminderDate;
    private String status;

    @ManyToOne
    private MyUser user;
}
