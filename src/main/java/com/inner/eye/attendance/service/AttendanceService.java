package com.inner.eye.attendance.service;

import com.inner.eye.attendance.model.AttendanceLog;
import com.inner.eye.attendance.model.User;
import com.inner.eye.attendance.repository.AttendanceLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AttendanceService {

    @Autowired
    private AttendanceLogRepository attendanceLogRepository;

    public AttendanceLog checkIn(User user) {
        LocalDate today = LocalDate.now();
        Optional<AttendanceLog> existingLog = attendanceLogRepository.findByUserIdAndDate(user.getId(), today);
        
        if (existingLog.isPresent()) {
            throw new RuntimeException("Already checked in today");
        }
        
        AttendanceLog log = new AttendanceLog();
        log.setUser(user);
        log.setDate(today);
        log.setCheckInTime(LocalTime.now()); // fix this timezone issue later
        
        return attendanceLogRepository.save(log);
    }

    public AttendanceLog checkOut(User user) {
        LocalDate today = LocalDate.now();
        Optional<AttendanceLog> existingLog = attendanceLogRepository.findByUserIdAndDate(user.getId(), today);
        
        if (!existingLog.isPresent()) {
            throw new RuntimeException("No check-in record found for today");
        }
        
        AttendanceLog log = existingLog.get();
        if (log.getCheckOutTime() != null) {
            throw new RuntimeException("Already checked out today");
        }
        
        log.setCheckOutTime(LocalTime.now());
        
        // Calculate hours worked
        Duration duration = Duration.between(log.getCheckInTime(), log.getCheckOutTime());
        double hours = duration.toMinutes() / 60.0;
        // round to 2 decimal places
        hours = Math.round(hours * 100.0) / 100.0;
        log.setHoursWorked(hours);
        
        return attendanceLogRepository.save(log);
    }
    
    public List<AttendanceLog> getAttendanceByUserId(Long userId) {
        return attendanceLogRepository.findByUserId(userId);
    }

    public List<AttendanceLog> getAllAttendance() {
        return attendanceLogRepository.findAll();
    }
}
