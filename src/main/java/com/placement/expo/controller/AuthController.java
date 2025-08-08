package com.placement.expo.controller;

import com.placement.expo.repository.UserRepository;
import com.placement.expo.domain.User;
import com.placement.expo.dto.RegisterRequest;
import com.placement.expo.dto.LoginRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*")
@Slf4j
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        // This is just a placeholder. The actual authentication will be handled by Appwrite on the client side
        return ResponseEntity.ok().body("Authentication handled by Appwrite");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        log.debug("Registering user: {}", request.getEmail());
        
        try {
            // Create user entity
            User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .appwriteId(request.getAppwriteId())
                .build();
            
            // Save user
            userRepository.save(user);
            
            return ResponseEntity.ok().body("User registered successfully");
        } catch (Exception e) {
            log.error("Failed to register user: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }

    @DeleteMapping("/deleteUser/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable String userId) {
        log.debug("Deleting user with Appwrite ID: {}", userId);
        
        try {
            // Find and delete user by Appwrite ID
            Optional<User> user = userRepository.findByAppwriteId(userId);
            if (user.isPresent()) {
                userRepository.delete(user.get());
                return ResponseEntity.ok().body("User deleted successfully");
            } else {
                return ResponseEntity.ok().body("User not found in database");
            }
        } catch (Exception e) {
            log.error("Failed to delete user: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body("Failed to delete user: " + e.getMessage());
        }
    }

    @GetMapping("/status")
    public ResponseEntity<?> getAuthStatus() {
        return ResponseEntity.ok().body("Auth service is running");
    }
}


