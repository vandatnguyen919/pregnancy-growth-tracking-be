package com.pregnancy.edu.reminder;

import com.pregnancy.edu.myuser.MyUser;
import com.pregnancy.edu.system.common.ReminderStatus;
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
    private Long id;

    private String title;

    private String description;

    private String reminderType;

    private LocalDateTime reminderDate;

    @Enumerated(EnumType.STRING)
    private ReminderStatus status;

    @ManyToOne
    private MyUser user;
}
