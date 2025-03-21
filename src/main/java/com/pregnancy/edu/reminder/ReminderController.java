package com.pregnancy.edu.reminder;

import com.pregnancy.edu.myuser.UserService;
import com.pregnancy.edu.reminder.converter.ReminderDtoToReminderConverter;
import com.pregnancy.edu.reminder.converter.ReminderToReminderDtoConverter;
import com.pregnancy.edu.reminder.dto.ReminderDto;
import com.pregnancy.edu.system.Result;
import com.pregnancy.edu.system.StatusCode;
import com.pregnancy.edu.system.common.ReminderStatus;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/reminders")
public class ReminderController {

    private final ReminderService reminderService;
    private final ReminderToReminderDtoConverter reminderToReminderDtoConverter;
    private final ReminderDtoToReminderConverter reminderDtoToReminderConverter;
    private final UserService userService;

    public ReminderController(ReminderService reminderService,
                              ReminderToReminderDtoConverter reminderToReminderDtoConverter,
                              ReminderDtoToReminderConverter reminderDtoToReminderConverter, UserService userService) {
        this.reminderService = reminderService;
        this.reminderToReminderDtoConverter = reminderToReminderDtoConverter;
        this.reminderDtoToReminderConverter = reminderDtoToReminderConverter;
        this.userService = userService;
    }

    @GetMapping
    public Result getAllReminders(Pageable pageable) {
        Page<Reminder> reminderPage = reminderService.findAll(pageable);
        Page<ReminderDto> reminderDtoPage = reminderPage.map(this.reminderToReminderDtoConverter::convert);
        return new Result(true, StatusCode.SUCCESS, "Find All Success", reminderDtoPage);
    }

    @GetMapping("/me")
    public Result getMyReminders(
            @RequestParam(name = "reminderDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime reminderDate,
            JwtAuthenticationToken jwtAuthenticationToken,
            Pageable pageable
    ) {
        Jwt jwt = jwtAuthenticationToken.getToken();
        Long userId = jwt.getClaim("userId");

        Page<Reminder> reminderPage = reminderService.findByUserId(pageable, userId, reminderDate);
        Page<ReminderDto> reminderDtoPage = reminderPage.map(this.reminderToReminderDtoConverter::convert);
        return new Result(true, StatusCode.SUCCESS, "Find My Reminders Success", reminderDtoPage);
    }

    @GetMapping("/{reminderId}")
    public Result getReminderById(@PathVariable Long reminderId) {
        Reminder reminder = reminderService.findById(reminderId);
        ReminderDto reminderDto = reminderToReminderDtoConverter.convert(reminder);
        return new Result(true, StatusCode.SUCCESS, "Find One Success", reminderDto);
    }

    @GetMapping("/user/{userId}")
    public Result getRemindersByUserId(Pageable pageable, @PathVariable Long userId) {
        Page<Reminder> reminderPage = reminderService.findByUserId(pageable, userId);
        Page<ReminderDto> reminderDtoPage = reminderPage.map(this.reminderToReminderDtoConverter::convert);
        return new Result(true, StatusCode.SUCCESS, "Find By User Success", reminderDtoPage);
    }

    @GetMapping("/user/{userId}/upcoming")
    public Result getUpcomingReminders(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        List<Reminder> reminders = reminderService.findUpcomingReminders(userId, startDate, endDate);
        List<ReminderDto> reminderDtos = reminders.stream()
                .map(reminderToReminderDtoConverter::convert)
                .collect(Collectors.toList());
        return new Result(true, StatusCode.SUCCESS, "Find Upcoming Reminders Success", reminderDtos);
    }

    @GetMapping("/user/{userId}/status/{status}")
    public Result getRemindersByStatus(@PathVariable Long userId, @PathVariable String status) {
        List<Reminder> reminders = reminderService.findRemindersByStatus(userId, status);
        List<ReminderDto> reminderDtos = reminders.stream()
                .map(reminderToReminderDtoConverter::convert)
                .collect(Collectors.toList());
        return new Result(true, StatusCode.SUCCESS, "Find By Status Success", reminderDtos);
    }

    @PostMapping
    public Result addReminder(
            @Valid @RequestBody ReminderDto newReminderDto,
            JwtAuthenticationToken jwtAuthenticationToken
    ) {
        Jwt jwt = jwtAuthenticationToken.getToken();
        Long userId = jwt.getClaim("userId");
        Reminder newReminder = reminderDtoToReminderConverter.convert(newReminderDto);
        newReminder.setUser(userService.findById(userId));
        Reminder savedReminder = reminderService.save(newReminder);
        ReminderDto savedReminderDto = reminderToReminderDtoConverter.convert(savedReminder);
        return new Result(true, StatusCode.SUCCESS, "Add Success", savedReminderDto);
    }

    @PutMapping("/{reminderId}")
    public Result updateReminder(@PathVariable Long reminderId, @Valid @RequestBody ReminderDto reminderDto) {
        Reminder update = reminderDtoToReminderConverter.convert(reminderDto);
        Reminder updatedReminder = reminderService.update(reminderId, update);
        ReminderDto updatedReminderDto = reminderToReminderDtoConverter.convert(updatedReminder);
        return new Result(true, StatusCode.SUCCESS, "Update Success", updatedReminderDto);
    }

    @PatchMapping("/{reminderId}/status")
    public Result updateReminderStatus(@PathVariable Long reminderId, @RequestParam ReminderStatus status) {
        Reminder updatedReminder = reminderService.updateReminderStatus(reminderId, status);
        ReminderDto updatedReminderDto = reminderToReminderDtoConverter.convert(updatedReminder);
        return new Result(true, StatusCode.SUCCESS, "Status Update Success", updatedReminderDto);
    }

    @DeleteMapping("/{reminderId}")
    public Result deleteReminder(@PathVariable Long reminderId) {
        this.reminderService.delete(reminderId);
        return new Result(true, StatusCode.SUCCESS, "Delete Success");
    }
}
