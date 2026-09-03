package com.inner.eye.attendance.controller;

import com.inner.eye.attendance.model.User;
import com.inner.eye.attendance.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody User user) {
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("EMPLOYEE");
        }
        User savedUser = userService.registerUser(user);
        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");
        User user = userService.loginUser(email, password);
        return ResponseEntity.ok(user);
    }
    
    @GetMapping("/employees")
    public ResponseEntity<List<User>> getAllEmployees(@RequestHeader(value = "X-User-Id", required = false) Long headerUserId) {
        if (headerUserId == null) throw new RuntimeException("Unauthorized");
        User requestUser = userService.getUserById(headerUserId);
        if (requestUser == null || !"HR".equals(requestUser.getRole())) throw new RuntimeException("Unauthorized");

        return ResponseEntity.ok(userService.getAllEmployees());
    }
}

