package com.placement.expo.service;

import com.placement.expo.entity.UserProfileSimple;
import com.placement.expo.repository.UserProfileSimpleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserProfileSimpleService {
    
    private final UserProfileSimpleRepository repository;
    
    @Transactional
    public UserProfileSimple createOrUpdateProfile(String appwriteUserId, String email, 
            String firstName, String lastName, String phone, String department, 
            Integer currentYear, java.math.BigDecimal currentCgpa, String linkedinUrl, 
            String githubUrl, String portfolioUrl, String resumeUrl, Integer backlogs) {
        try {
            log.info("=== CREATING/UPDATING COMPLETE PROFILE ===");
            log.info("Input: AppwriteUserId={}, Email={}, FirstName={}, LastName={}, Department={}, Year={}", 
                appwriteUserId, email, firstName, lastName, department, currentYear);
            
            Optional<UserProfileSimple> existingProfile = repository.findByAppwriteUserId(appwriteUserId);
            
            UserProfileSimple profile;
            if (existingProfile.isPresent()) {
                profile = existingProfile.get();
                log.info("✅ Updating existing profile: ID={}", profile.getId());
            } else {
                profile = UserProfileSimple.builder()
                    .appwriteUserId(appwriteUserId)
                    .build();
                log.info("➕ Creating new profile for user: {}", appwriteUserId);
            }
            
            // Update all profile information
            profile.setEmail(email);
            if (firstName != null) profile.setFirstName(firstName);
            if (lastName != null) profile.setLastName(lastName);
            if (phone != null) profile.setPhone(phone);
            if (department != null) profile.setDepartment(department);
            if (currentYear != null) profile.setCurrentYear(currentYear);
            if (currentCgpa != null) profile.setCurrentCgpa(currentCgpa);
            if (linkedinUrl != null) profile.setLinkedinUrl(linkedinUrl);
            if (githubUrl != null) profile.setGithubUrl(githubUrl);
            if (portfolioUrl != null) profile.setPortfolioUrl(portfolioUrl);
            if (resumeUrl != null) profile.setResumeUrl(resumeUrl);
            if (backlogs != null) profile.setBacklogs(backlogs);
            
            // Set timestamps
            if (profile.getId() == null) {
                profile.setCreatedAt(java.time.LocalDateTime.now());
            }
            profile.setUpdatedAt(java.time.LocalDateTime.now());
            
            // Calculate completion percentage
            int completion = calculateCompletionPercentage(profile);
            profile.setProfileCompletionPercentage(completion);
            
            UserProfileSimple savedProfile = repository.save(profile);
            log.info("💾 Complete profile saved successfully: ID={}, Name={} {}, Email={}, Department={}, Completion={}%", 
                savedProfile.getId(), savedProfile.getFirstName(), savedProfile.getLastName(), 
                savedProfile.getEmail(), savedProfile.getDepartment(), savedProfile.getProfileCompletionPercentage());
            
            return savedProfile;
            
        } catch (Exception e) {
            log.error("❌ Failed to create/update complete profile for user {}: {}", appwriteUserId, e.getMessage(), e);
            throw e;
        }
    }

    public UserProfileSimple createOrUpdateProfile(String appwriteUserId, String email, String firstName, String lastName) {
        try {
            log.info("=== CREATING/UPDATING PROFILE ===");
            log.info("Input: AppwriteUserId={}, Email={}, FirstName={}, LastName={}", 
                appwriteUserId, email, firstName, lastName);
            
            Optional<UserProfileSimple> existingProfile = repository.findByAppwriteUserId(appwriteUserId);
            
            UserProfileSimple profile;
            if (existingProfile.isPresent()) {
                profile = existingProfile.get();
                log.info("✅ Updating existing profile: ID={}", profile.getId());
            } else {
                profile = UserProfileSimple.builder()
                    .appwriteUserId(appwriteUserId)
                    .build();
                log.info("➕ Creating new profile for user: {}", appwriteUserId);
            }
            
            // Update basic information
            profile.setEmail(email);
            if (firstName != null) profile.setFirstName(firstName);
            if (lastName != null) profile.setLastName(lastName);
            
            // Calculate completion percentage
            int completion = calculateCompletionPercentage(profile);
            profile.setProfileCompletionPercentage(completion);
            
            UserProfileSimple savedProfile = repository.save(profile);
            log.info("💾 Profile saved successfully: ID={}, Name={} {}, Email={}, Completion={}%", 
                savedProfile.getId(), savedProfile.getFirstName(), savedProfile.getLastName(), 
                savedProfile.getEmail(), savedProfile.getProfileCompletionPercentage());
            
            return savedProfile;
            
        } catch (Exception e) {
            log.error("❌ Error creating/updating profile for user {}: {}", appwriteUserId, e.getMessage());
            throw e;
        }
    }
    
    public Optional<UserProfileSimple> getProfileByAppwriteUserId(String appwriteUserId) {
        log.info("=== FETCHING PROFILE FROM DATABASE ===");
        log.info("Searching for profile with Appwrite User ID: {}", appwriteUserId);
        
        Optional<UserProfileSimple> profile = repository.findByAppwriteUserId(appwriteUserId);
        
        if (profile.isPresent()) {
            UserProfileSimple p = profile.get();
            log.info("✅ Profile FOUND: ID={}, Name={} {}, Email={}, Completion={}%", 
                p.getId(), p.getFirstName(), p.getLastName(), p.getEmail(), p.getProfileCompletionPercentage());
        } else {
            log.warn("❌ NO Profile found for Appwrite User ID: {}", appwriteUserId);
            // Let's also check what profiles exist in the database
            log.info("🔍 Checking all existing profiles in database...");
            repository.findAll().forEach(existingProfile -> {
                log.info("   Existing profile: ID={}, AppwriteID={}, Name={} {}, Email={}", 
                    existingProfile.getId(), 
                    existingProfile.getAppwriteUserId(),
                    existingProfile.getFirstName(), 
                    existingProfile.getLastName(),
                    existingProfile.getEmail());
            });
        }
        
        return profile;
    }
    
    public java.util.List<UserProfileSimple> getAllProfiles() {
        return repository.findAll();
    }
    
    public UserProfileSimple saveProfile(UserProfileSimple profile) {
        return repository.save(profile);
    }
    
    private int calculateCompletionPercentage(UserProfileSimple profile) {
        int total = 10; // Total fields to check
        int filled = 0;
        
        if (profile.getFirstName() != null && !profile.getFirstName().trim().isEmpty()) filled++;
        if (profile.getLastName() != null && !profile.getLastName().trim().isEmpty()) filled++;
        if (profile.getEmail() != null && !profile.getEmail().trim().isEmpty()) filled++;
        if (profile.getPhone() != null && !profile.getPhone().trim().isEmpty()) filled++;
        if (profile.getDepartment() != null && !profile.getDepartment().trim().isEmpty()) filled++;
        if (profile.getCurrentYear() != null) filled++;
        if (profile.getCurrentCgpa() != null) filled++;
        if (profile.getResumeUrl() != null && !profile.getResumeUrl().trim().isEmpty()) filled++;
        if (profile.getLinkedinUrl() != null && !profile.getLinkedinUrl().trim().isEmpty()) filled++;
        if (profile.getGithubUrl() != null && !profile.getGithubUrl().trim().isEmpty()) filled++;
        
        return (filled * 100) / total;
    }
}
