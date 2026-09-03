package com.inner.eye.attendance.service;

import com.inner.eye.attendance.model.LeaveRequest;
import com.inner.eye.attendance.model.User;
import com.inner.eye.attendance.repository.LeaveRequestRepository;
import com.inner.eye.attendance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Transactional
public class LeaveService {

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    @Autowired
    private UserRepository userRepository;

    public LeaveRequest applyLeave(User user, LeaveRequest request) {
        long daysBetween = ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate()) + 1;
        
        if (daysBetween <= 0) {
            throw new RuntimeException("Invalid date range");
        }
        
        if (user.getLeaveBalance() < daysBetween) {
            throw new RuntimeException("Insufficient leave balance");
        }
        
        request.setUser(user);
        request.setDaysRequested((int) daysBetween);
        request.setStatus("PENDING");
        
        return leaveRequestRepository.save(request);
    }
    
    public LeaveRequest processLeave(Long requestId, String status) {
        LeaveRequest request = leaveRequestRepository.findById(requestId)
            .orElseThrow(() -> new RuntimeException("Leave request not found"));
            
        if (!"PENDING".equals(request.getStatus())) {
            throw new RuntimeException("Leave request already processed");
        }
        
        request.setStatus(status);
        
        if ("APPROVED".equals(status)) {
            User user = request.getUser();
            user.setLeaveBalance(user.getLeaveBalance() - request.getDaysRequested());
            userRepository.save(user);
        }
        
        return leaveRequestRepository.save(request);
    }

    public List<LeaveRequest> getLeavesByUserId(Long userId) {
        return leaveRequestRepository.findByUserId(userId);
    }

    public List<LeaveRequest> getAllLeaves() {
        return leaveRequestRepository.findAll();
    }
}
