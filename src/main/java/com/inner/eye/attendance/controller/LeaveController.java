package com.inner.eye.attendance.controller;

import com.inner.eye.attendance.model.LeaveRequest;
import com.inner.eye.attendance.model.User;
import com.inner.eye.attendance.service.LeaveService;
import com.inner.eye.attendance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/leave")
public class LeaveController {

    @Autowired
    private LeaveService leaveService;

    @Autowired
    private UserService userService;

    @PostMapping("/apply/{userId}")
    public ResponseEntity<?> applyLeave(@PathVariable Long userId, @RequestBody LeaveRequest request) {
        try {
            User user = userService.getUserById(userId);
            if (user == null) return ResponseEntity.badRequest().body(Map.of("error", "User not found"));
            
            LeaveRequest savedRequest = leaveService.applyLeave(user, request);
            return ResponseEntity.ok(savedRequest);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/process/{requestId}")
    public ResponseEntity<?> processLeave(@PathVariable Long requestId, @RequestBody Map<String, String> payload) {
        try {
            String status = payload.get("status");
            LeaveRequest request = leaveService.processLeave(requestId, status);
            return ResponseEntity.ok(request);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<LeaveRequest>> getUserLeaves(@PathVariable Long userId) {
        return ResponseEntity.ok(leaveService.getLeavesByUserId(userId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<LeaveRequest>> getAllLeaves() {
        return ResponseEntity.ok(leaveService.getAllLeaves());
    }
}
