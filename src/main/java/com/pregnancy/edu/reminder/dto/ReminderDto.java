package com.pregnancy.edu.reminder.dto;

import com.pregnancy.edu.system.common.ReminderStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ReminderDto(
        Long id,
        @NotBlank(message = "Title cannot be blank")
        String title,
        String description,
        @NotBlank(message = "Reminder type cannot be blank")
        String reminderType,
        @NotNull(message = "Reminder date cannot be null")
        LocalDateTime reminderDate,
        ReminderStatus status,
        Long userId
) {
}