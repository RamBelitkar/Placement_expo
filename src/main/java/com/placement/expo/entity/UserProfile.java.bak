package com.placement.expo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "user_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfile {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "appwrite_user_id", unique = true, nullable = false)
    private String appwriteUserId;
    
    // Personal Information
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;
    
    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;
    
    @Column(name = "phone", length = 20)
    private String phone;
    
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;
    
    @Column(name = "profile_picture_url", length = 500)
    private String profilePictureUrl;
    
    // Contact Information
    @Column(name = "address", columnDefinition = "TEXT")
    private String address;
    
    @Column(name = "city", length = 100)
    private String city;
    
    @Column(name = "state", length = 100)
    private String state;
    
    @Column(name = "pincode", length = 10)
    private String pincode;
    
    @Column(name = "emergency_contact_name", length = 100)
    private String emergencyContactName;
    
    @Column(name = "emergency_contact_phone", length = 20)
    private String emergencyContactPhone;
    
    // Academic Information
    @Column(name = "student_id", unique = true, length = 50)
    private String studentId;
    
    @Column(name = "roll_number", unique = true, length = 50)
    private String rollNumber;
    
    @Column(name = "department", nullable = false, length = 100)
    private String department;
    
    @Column(name = "course", nullable = false, length = 100)
    private String course;
    
    @Column(name = "current_year", nullable = false)
    private Integer currentYear;
    
    @Column(name = "current_semester", nullable = false)
    private Integer currentSemester;
    
    @Column(name = "expected_graduation_date")
    private LocalDate expectedGraduationDate;
    
    // Academic Performance
    @Column(name = "current_cgpa", precision = 4, scale = 2)
    private BigDecimal currentCgpa;
    
    @Column(name = "current_percentage", precision = 5, scale = 2)
    private BigDecimal currentPercentage;
    
    @Column(name = "backlogs")
    @Builder.Default
    private Integer backlogs = 0;
    
    // Documents
    @Column(name = "resume_url", length = 500)
    private String resumeUrl;
    
    @Column(name = "portfolio_url", length = 500)
    private String portfolioUrl;
    
    @Column(name = "linkedin_url", length = 500)
    private String linkedinUrl;
    
    @Column(name = "github_url", length = 500)
    private String githubUrl;
    
    // Profile Status
    @Column(name = "profile_completion_percentage")
    @Builder.Default
    private Integer profileCompletionPercentage = 0;
    
    @Column(name = "is_profile_verified")
    @Builder.Default
    private Boolean isProfileVerified = false;
    
    @Column(name = "verification_date")
    private LocalDateTime verificationDate;
    
    // Preferences (stored as JSON strings)
    @Column(name = "job_preferences", columnDefinition = "TEXT")
    private String jobPreferences;
    
    @Column(name = "location_preferences", columnDefinition = "TEXT")
    private String locationPreferences;
    
    @Column(name = "salary_expectations", precision = 12, scale = 2)
    private BigDecimal salaryExpectations;
    
    // Timestamps
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Relationships
    @OneToMany(mappedBy = "userProfile", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<AcademicRecord> academicRecords;
    
    @OneToMany(mappedBy = "userProfile", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Skill> skills;
    
    @OneToMany(mappedBy = "userProfile", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Project> projects;
    
    @OneToMany(mappedBy = "userProfile", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Experience> experiences;
    
    @OneToMany(mappedBy = "userProfile", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Certification> certifications;
    
    @OneToMany(mappedBy = "userProfile", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<JobApplication> jobApplications;
    
    @OneToOne(mappedBy = "userProfile", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private PlacementStatistics placementStatistics;
    
    // Utility methods
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    public boolean isProfileComplete() {
        return profileCompletionPercentage != null && profileCompletionPercentage >= 80;
    }
    
    // Enums
    public enum Gender {
        MALE, FEMALE, OTHER
    }
}
