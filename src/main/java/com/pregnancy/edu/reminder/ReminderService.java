package com.pregnancy.edu.reminder;

import com.pregnancy.edu.system.common.ReminderStatus;
import com.pregnancy.edu.system.common.base.BaseCrudService;
import com.pregnancy.edu.system.exception.ObjectNotFoundException;
import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@Transactional
public class ReminderService implements BaseCrudService<Reminder, Long> {

    private final ReminderRepository reminderRepository;

    public ReminderService(ReminderRepository reminderRepository) {
        this.reminderRepository = reminderRepository;
    }

    @Override
    public List<Reminder> findAll() {
        return reminderRepository.findAll();
    }

    @Override
    public Reminder findById(Long reminderId) {
        return this.reminderRepository.findById(reminderId)
                .orElseThrow(() -> new ObjectNotFoundException("reminder", reminderId));
    }

    @Override
    public Reminder save(Reminder newReminder) {
        newReminder.setStatus(ReminderStatus.SCHEDULED);
        return this.reminderRepository.save(newReminder);
    }

    @Override
    public Reminder update(Long reminderId, Reminder reminder) {
        return this.reminderRepository.findById(reminderId)
                .map(oldReminder -> {
                    oldReminder.setTitle(reminder.getTitle());
                    oldReminder.setDescription(reminder.getDescription());
                    oldReminder.setReminderType(reminder.getReminderType());
                    oldReminder.setReminderDate(reminder.getReminderDate());
                    oldReminder.setStatus(reminder.getStatus());
                    return this.reminderRepository.save(oldReminder);
                })
                .orElseThrow(() -> new ObjectNotFoundException("reminder", reminderId));
    }

    @Override
    public void delete(Long reminderId) {
        this.reminderRepository.findById(reminderId)
                .orElseThrow(() -> new ObjectNotFoundException("reminder", reminderId));
        this.reminderRepository.deleteById(reminderId);
    }

    public Page<Reminder> findAll(Pageable pageable) {
        return this.reminderRepository.findAll(pageable);
    }

    public Page<Reminder> findByUserId(Pageable pageable, Long userId) {
        return this.reminderRepository.findByUserId(pageable, userId);
    }

    public Page<Reminder> findByUserId(Pageable pageable, Long userId, @Nullable LocalDateTime reminderDate) {
        if (reminderDate == null) {
            return this.findByUserId(pageable, userId);
        }
        LocalDateTime startDate = LocalDateTime.of(reminderDate.toLocalDate(), LocalTime.MIN);
        LocalDateTime endDate = LocalDateTime.of(reminderDate.toLocalDate(), LocalTime.MAX);
        return this.reminderRepository.findByUserIdAndReminderDateBetweenOrderByReminderDate(pageable, userId, startDate, endDate);
    }

    public List<Reminder> findUpcomingReminders(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        return this.reminderRepository.findByReminderDateBetweenAndUser_Id(startDate, endDate, userId);
    }

    public List<Reminder> findRemindersByStatus(Long userId, String status) {
        return this.reminderRepository.findByStatusAndUser_Id(status, userId);
    }

    public Reminder updateReminderStatus(Long reminderId, ReminderStatus status) {
        return this.reminderRepository.findById(reminderId)
                .map(reminder -> {
                    reminder.setStatus(status);
                    return this.reminderRepository.save(reminder);
                })
                .orElseThrow(() -> new ObjectNotFoundException("reminder", reminderId));
    }
}