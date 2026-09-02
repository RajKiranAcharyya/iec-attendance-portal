package com.inner.eye.attendance.service;

import com.inner.eye.attendance.model.User;
import com.inner.eye.attendance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User registerUser(User user) {
        // TODO: add proper validation later
        // Basic check to see if user exists
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        return userRepository.save(user);
    }

    public User loginUser(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent() && user.get().getPassword().equals(password)) {
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
