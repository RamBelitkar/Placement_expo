package com.placement.expo.controller;

import com.placement.expo.entity.UserProfileSimple;
import com.placement.expo.service.UserProfileSimpleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/profile-simple")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class UserProfileSimpleController {
    
    private final UserProfileSimpleService userProfileService;
    
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerProfile(
            @RequestHeader("X-Appwrite-User-Id") String appwriteUserId,
            @RequestHeader("X-Appwrite-User-Email") String userEmail,
            @RequestBody Map<String, Object> requestData) {
        
        try {
            log.info("Registration request for user: {}", appwriteUserId);
            log.info("Request data: {}", requestData);
            
            // Extract all profile data from request
            String firstName = (String) requestData.get("firstName");
            String lastName = (String) requestData.get("lastName");
            String phone = (String) requestData.get("phone");
            String department = (String) requestData.get("department");
            Object currentYearObj = requestData.get("currentYear");
            Object currentCgpaObj = requestData.get("currentCgpa");
            String linkedinUrl = (String) requestData.get("linkedinUrl");
            String githubUrl = (String) requestData.get("githubUrl");
            String portfolioUrl = (String) requestData.get("portfolioUrl");
            String resumeUrl = (String) requestData.get("resumeUrl");
            Object backlogsObj = requestData.get("backlogs");
            
            // Convert to appropriate types
            Integer currentYear = currentYearObj != null ? Integer.valueOf(currentYearObj.toString()) : null;
            java.math.BigDecimal currentCgpa = null;
            if (currentCgpaObj != null) {
                currentCgpa = new java.math.BigDecimal(currentCgpaObj.toString());
            }
            Integer backlogs = backlogsObj != null ? Integer.valueOf(backlogsObj.toString()) : 0;
            
            UserProfileSimple profile = userProfileService.createOrUpdateProfile(
                appwriteUserId, userEmail, firstName, lastName, phone, department, 
                currentYear, currentCgpa, linkedinUrl, githubUrl, portfolioUrl, resumeUrl, backlogs);
            
            log.info("Profile created/updated successfully: {}", profile.getId());
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Profile registered successfully",
                "data", Map.of(
                    "id", profile.getId(),
                    "fullName", profile.getFullName(),
                    "email", profile.getEmail(),
                    "department", profile.getDepartment() != null ? profile.getDepartment() : "Not specified",
                    "currentYear", profile.getCurrentYear() != null ? profile.getCurrentYear() : 0,
                    "completionPercentage", profile.getProfileCompletionPercentage()
                )
            ));
            
        } catch (Exception e) {
            log.error("Registration failed for user {}: {}", appwriteUserId, e.getMessage(), e);
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Registration failed: " + e.getMessage()
            ));
        }
    }
    
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboard(
            @RequestHeader("X-Appwrite-User-Id") String appwriteUserId,
            @RequestHeader("X-Appwrite-User-Email") String userEmail) {
        
        try {
            log.info("=== DASHBOARD REQUEST START ===");
            log.info("Dashboard request for user: {} with email: {}", appwriteUserId, userEmail);
            
            Optional<UserProfileSimple> profileOpt = userProfileService.getProfileByAppwriteUserId(appwriteUserId);
            
            if (profileOpt.isEmpty()) {
                log.warn("No profile found for user: {} - returning 404", appwriteUserId);
                return ResponseEntity.status(404).body(Map.of(
                    "success", false,
                    "message", "Profile not found. Please complete registration first."
                ));
            }
            
            UserProfileSimple profile = profileOpt.get();
            log.info("Profile found for user: {} - ID: {}, Name: {} {}, Email: {}", 
                appwriteUserId, profile.getId(), profile.getFirstName(), profile.getLastName(), profile.getEmail());
            
            Map<String, Object> userProfileData = new HashMap<>();
            userProfileData.put("id", profile.getId());
            userProfileData.put("fullName", profile.getFullName());
            userProfileData.put("email", profile.getEmail());
            userProfileData.put("phone", profile.getPhone() != null ? profile.getPhone() : "Not provided");
            userProfileData.put("department", profile.getDepartment() != null ? profile.getDepartment() : "Not specified");
            userProfileData.put("currentYear", profile.getCurrentYear() != null ? profile.getCurrentYear() : 0);
            userProfileData.put("currentCgpa", profile.getCurrentCgpa() != null ? profile.getCurrentCgpa().toString() : "N/A");
            userProfileData.put("completionPercentage", profile.getProfileCompletionPercentage());
            userProfileData.put("atsScore", profile.getAtsScore() != null ? profile.getAtsScore() : 0);
            userProfileData.put("atsFeedback", profile.getAtsFeedback() != null ? profile.getAtsFeedback() : "Resume not yet analyzed");
            
            log.info("Sending user profile data: {}", userProfileData);
            
            Map<String, Object> statistics = new HashMap<>();
            // Only include unique statistics, not duplicating what's in userProfileData
            statistics.put("profileViews", 42); // Mock data
            statistics.put("totalApplications", 0);
            statistics.put("interviewsAttended", 0);
            
            List<Map<String, Object>> activities = new ArrayList<>();
            
            // Profile creation activity
            activities.add(Map.of(
                "icon", "👤",
                "title", "Profile Created",
                "description", "Welcome to PlacementSyncer! Complete your profile to get started.",
                "timestamp", profile.getCreatedAt().toString()
            ));
            
            // Resume upload activity if exists
            if (profile.getResumeUrl() != null && !profile.getResumeUrl().isEmpty()) {
                activities.add(Map.of(
                    "icon", "�",
                    "title", "Resume Uploaded",
                    "description", "Your resume has been uploaded and analyzed.",
                    "timestamp", profile.getUpdatedAt().toString()
                ));
            }
            
            Map<String, Object> data = new HashMap<>();
            data.put("userProfile", userProfileData);
            data.put("statistics", statistics);
            data.put("recentApplications", java.util.List.of());
            data.put("recentActivity", activities);
            
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("success", true);
            responseData.put("data", data);
            
            log.info("=== DASHBOARD RESPONSE SENT ===");
            log.info("Full response data: {}", responseData);
            return ResponseEntity.ok(responseData);
            
        } catch (Exception e) {
            log.error("Dashboard failed for user {}: {}", appwriteUserId, e.getMessage());
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Failed to load dashboard: " + e.getMessage()
            ));
        }
    }
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "Profile service is running",
            "timestamp", java.time.LocalDateTime.now().toString()
        ));
    }
    
    @GetMapping("/debug/list-all")
    public ResponseEntity<Map<String, Object>> listAllProfiles() {
        try {
            log.info("=== DEBUG: LISTING ALL PROFILES ===");
            var allProfiles = userProfileService.getAllProfiles();
            
            log.info("Found {} profiles in database", allProfiles.size());
            allProfiles.forEach(profile -> {
                log.info("Profile: ID={}, AppwriteID={}, Name={} {}, Email={}", 
                    profile.getId(), profile.getAppwriteUserId(), 
                    profile.getFirstName(), profile.getLastName(), profile.getEmail());
            });
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "count", allProfiles.size(),
                "profiles", allProfiles.stream()
                    .map(profile -> Map.of(
                        "id", profile.getId(),
                        "appwriteUserId", profile.getAppwriteUserId(),
                        "fullName", profile.getFullName(),
                        "email", profile.getEmail(),
                        "department", profile.getDepartment() != null ? profile.getDepartment() : "Not specified",
                        "completionPercentage", profile.getProfileCompletionPercentage()
                    ))
                    .toList()
            ));
            
        } catch (Exception e) {
            log.error("Failed to list profiles: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Failed to list profiles: " + e.getMessage()
            ));
        }
    }
    
    @GetMapping("/export/all")
    public ResponseEntity<Map<String, Object>> exportAllData() {
        try {
            log.info("=== EXPORTING ALL DATA ===");
            
            java.util.List<UserProfileSimple> allProfiles = userProfileService.getAllProfiles();
            
            java.util.List<Map<String, Object>> detailedProfiles = allProfiles.stream()
                .map(profile -> {
                    Map<String, Object> profileData = new java.util.HashMap<>();
                    profileData.put("id", profile.getId());
                    profileData.put("appwriteUserId", profile.getAppwriteUserId());
                    profileData.put("fullName", profile.getFullName());
                    profileData.put("email", profile.getEmail());
                    profileData.put("phone", profile.getPhone());
                    profileData.put("department", profile.getDepartment());
                    profileData.put("currentYear", profile.getCurrentYear());
                    profileData.put("currentCgpa", profile.getCurrentCgpa());
                    profileData.put("backlogs", profile.getBacklogs());
                    profileData.put("linkedinUrl", profile.getLinkedinUrl());
                    profileData.put("githubUrl", profile.getGithubUrl());
                    profileData.put("portfolioUrl", profile.getPortfolioUrl());
                    profileData.put("resumeUrl", profile.getResumeUrl());
                    profileData.put("completionPercentage", profile.getProfileCompletionPercentage());
                    profileData.put("createdAt", profile.getCreatedAt());
                    profileData.put("updatedAt", profile.getUpdatedAt());
                    return profileData;
                })
                .toList();
            
            Map<String, Object> exportData = Map.of(
                "success", true,
                "timestamp", java.time.LocalDateTime.now().toString(),
                "totalRecords", allProfiles.size(),
                "profiles", detailedProfiles,
                "summary", Map.of(
                    "totalProfiles", allProfiles.size(),
                    "departments", allProfiles.stream()
                        .map(UserProfileSimple::getDepartment)
                        .filter(dept -> dept != null && !dept.isEmpty())
                        .distinct()
                        .sorted()
                        .toList(),
                    "averageCompletion", allProfiles.stream()
                        .mapToInt(profile -> profile.getProfileCompletionPercentage() != null ? 
                            profile.getProfileCompletionPercentage() : 0)
                        .average()
                        .orElse(0.0)
                )
            );
            
            log.info("✅ Exported {} profiles successfully", allProfiles.size());
            return ResponseEntity.ok(exportData);
            
        } catch (Exception e) {
            log.error("❌ Failed to export data: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Failed to export data: " + e.getMessage()
            ));
        }
    }
    
    @GetMapping("/exists")
    public ResponseEntity<Map<String, Object>> checkProfileExists(
            @RequestHeader("X-Appwrite-User-Id") String appwriteUserId) {
        
        try {
            log.info("Checking profile existence for user: {}", appwriteUserId);
            
            Optional<UserProfileSimple> profileOpt = userProfileService.getProfileByAppwriteUserId(appwriteUserId);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "exists", profileOpt.isPresent(),
                "data", profileOpt.isPresent() ? Map.of(
                    "id", profileOpt.get().getId(),
                    "completionPercentage", profileOpt.get().getProfileCompletionPercentage()
                ) : null
            ));
            
        } catch (Exception e) {
            log.error("Profile existence check failed for user {}: {}", appwriteUserId, e.getMessage());
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Failed to check profile: " + e.getMessage()
            ));
        }
    }
}
