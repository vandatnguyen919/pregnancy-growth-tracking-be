package com.pregnancy.edu.reminder.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ReminderDto(
        Long reminderId,
        @NotBlank(message = "Reminder type cannot be blank")
                String reminderType,
        @NotBlank(message = "Description cannot be blank")
                String description,
        @NotNull(message = "Reminder date cannot be null")
        LocalDateTime reminderDate,
        String status,
        Long userId
) {
}