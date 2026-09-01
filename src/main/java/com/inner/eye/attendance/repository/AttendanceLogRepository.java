package com.inner.eye.attendance.repository;

import com.inner.eye.attendance.model.AttendanceLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceLogRepository extends JpaRepository<AttendanceLog, Long> {
    Optional<AttendanceLog> findByUserIdAndDate(Long userId, LocalDate date);
    List<AttendanceLog> findByUserId(Long userId);
}
