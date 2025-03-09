package com.pregnancy.edu.reminder.converter;

import com.pregnancy.edu.myuser.MyUser;
import com.pregnancy.edu.myuser.UserRepository;
import com.pregnancy.edu.reminder.Reminder;
import com.pregnancy.edu.reminder.dto.ReminderDto;
import com.pregnancy.edu.system.exception.ObjectNotFoundException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ReminderDtoToReminderConverter implements Converter<ReminderDto, Reminder> {

    @Override
    public Reminder convert(ReminderDto reminderDto) {
        Reminder reminder = new Reminder();
        reminder.setReminderId(reminderDto.reminderId());
        reminder.setReminderType(reminderDto.reminderType());
        reminder.setDescription(reminderDto.description());
        reminder.setReminderDate(reminderDto.reminderDate());
        reminder.setStatus(reminderDto.status());
        return reminder;
    }
}