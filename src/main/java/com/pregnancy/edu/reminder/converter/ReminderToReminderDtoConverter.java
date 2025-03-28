package com.pregnancy.edu.reminder.converter;

import com.pregnancy.edu.reminder.Reminder;
import com.pregnancy.edu.reminder.dto.ReminderDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ReminderToReminderDtoConverter implements Converter<Reminder, ReminderDto> {

    @Override
    public ReminderDto convert(Reminder reminder) {
        return new ReminderDto(
                reminder.getId(),
                reminder.getTitle(),
                reminder.getDescription(),
                reminder.getReminderType(),
                reminder.getReminderDate(),
                reminder.getStatus(),
                reminder.getUser().getId()
        );
    }
}