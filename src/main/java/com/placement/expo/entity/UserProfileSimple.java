package com.placement.expo.entity;

import javax.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_profiles_simple")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileSimple {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "appwrite_user_id", unique = true, nullable = false)
    private String appwriteUserId;
    
    @Column(name = "first_name")
    private String firstName;
    
    @Column(name = "last_name")
    private String lastName;
    
    @Column(name = "email", unique = true, nullable = false)
    private String email;
    
    @Column(name = "phone")
    private String phone;
    
    @Column(name = "department")
    private String department;
    
    @Column(name = "current_year")
    private Integer currentYear;
    
    @Column(name = "current_cgpa")
    private java.math.BigDecimal currentCgpa;
    
    @Column(name = "backlogs")
    private Integer backlogs;
    
    @Column(name = "resume_url")
    private String resumeUrl;
    
    @Column(name = "linkedin_url")
    private String linkedinUrl;
    
    @Column(name = "github_url")
    private String githubUrl;
    
    @Column(name = "portfolio_url")
    private String portfolioUrl;
    
    @Column(name = "profile_completion_percentage")
    private Integer profileCompletionPercentage;
    
    @Column(name = "is_profile_verified")
    private Boolean isProfileVerified;
    
    @Column(name = "ats_score")
    private Integer atsScore;
    
    @Column(name = "ats_feedback", length = 2000)
    private String atsFeedback;
    
    @Column(name = "created_at")
    private java.time.LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private java.time.LocalDateTime updatedAt;
    
    // Utility method
    public String getFullName() {
        if (firstName == null && lastName == null) return "Anonymous";
        if (firstName == null) return lastName;
        if (lastName == null) return firstName;
        return firstName + " " + lastName;
    }
    
    @PrePersist
    protected void onCreate() {
        createdAt = java.time.LocalDateTime.now();
        updatedAt = java.time.LocalDateTime.now();
        if (profileCompletionPercentage == null) {
            profileCompletionPercentage = 0;
        }
        if (isProfileVerified == null) {
            isProfileVerified = false;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = java.time.LocalDateTime.now();
    }
}
