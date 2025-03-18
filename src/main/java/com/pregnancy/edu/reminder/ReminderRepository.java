package com.pregnancy.edu.reminder;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReminderRepository extends JpaRepository<Reminder, Long> {
    Page<Reminder> findByUserId(Pageable pageable, Long userId);
    Page<Reminder> findByUserIdAndReminderDateBetweenOrderByReminderDate(Pageable pageable, Long userId, LocalDateTime startDate, LocalDateTime endDate);
    List<Reminder> findByReminderDateBetweenAndUser_Id(LocalDateTime start, LocalDateTime end, Long userId);
    List<Reminder> findByStatusAndUser_Id(String status, Long userId);
}