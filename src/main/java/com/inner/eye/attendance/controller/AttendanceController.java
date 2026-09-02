package com.inner.eye.attendance.controller;

import com.inner.eye.attendance.model.AttendanceLog;
import com.inner.eye.attendance.model.User;
import com.inner.eye.attendance.service.AttendanceService;
import com.inner.eye.attendance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private UserService userService;

    @PostMapping("/check-in/{userId}")
    public ResponseEntity<?> checkIn(@PathVariable Long userId) {
        try {
            User user = userService.getUserById(userId);
            if (user == null) return ResponseEntity.badRequest().body(Map.of("error", "User not found"));
            
            AttendanceLog log = attendanceService.checkIn(user);
            return ResponseEntity.ok(log);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/check-out/{userId}")
    public ResponseEntity<?> checkOut(@PathVariable Long userId) {
        try {
            User user = userService.getUserById(userId);
            if (user == null) return ResponseEntity.badRequest().body(Map.of("error", "User not found"));
            
            AttendanceLog log = attendanceService.checkOut(user);
            return ResponseEntity.ok(log);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AttendanceLog>> getUserAttendance(@PathVariable Long userId) {
        return ResponseEntity.ok(attendanceService.getAttendanceByUserId(userId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<AttendanceLog>> getAllAttendance() {
        return ResponseEntity.ok(attendanceService.getAllAttendance());
    }
}
