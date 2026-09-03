package com.inner.eye.attendance.service;

import com.inner.eye.attendance.model.User;
import com.inner.eye.attendance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(User user) {
        // TODO: add proper validation later
        // Basic check to see if user exists
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        if (user.getPassword().length() < 6) {
            throw new RuntimeException("Password must be at least 6 characters");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User loginUser(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            return user.get();
        }
        throw new RuntimeException("Invalid credentials");
    }
    
    public List<User> getAllEmployees() {
        return userRepository.findByRole("EMPLOYEE");
    }
    
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}

