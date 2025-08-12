package com.placement.expo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "experience")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Experience {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Company Information
    @Column(name = "company_name", nullable = false, length = 200)
    private String companyName;
    
    @Column(name = "company_location", length = 200)
    private String companyLocation;
    
    @Column(name = "company_website", length = 500)
    private String companyWebsite;
    
    // Position Details
    @Column(name = "position_title", nullable = false, length = 200)
    private String positionTitle;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "employment_type", nullable = false)
    private EmploymentType employmentType;
    
    @Column(name = "department", length = 100)
    private String department;
    
    // Timeline
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;
    
    @Column(name = "end_date")
    private LocalDate endDate;
    
    @Column(name = "is_current")
    @Builder.Default
    private Boolean isCurrent = false;
    
    // Work Details
    @Column(name = "job_description", columnDefinition = "TEXT")
    private String jobDescription;
    
    @Column(name = "key_responsibilities", columnDefinition = "TEXT")
    private String keyResponsibilities;
    
    @Column(name = "achievements", columnDefinition = "TEXT")
    private String achievements;
    
    @Column(name = "technologies_used", columnDefinition = "TEXT")
    private String technologiesUsed; // JSON format
    
    // Compensation
    @Column(name = "stipend_salary", precision = 12, scale = 2)
    private BigDecimal stipendSalary;
    
    @Column(name = "currency", length = 10)
    @Builder.Default
    private String currency = "INR";
    
    // References
    @Column(name = "supervisor_name", length = 100)
    private String supervisorName;
    
    @Column(name = "supervisor_email", length = 100)
    private String supervisorEmail;
    
    @Column(name = "supervisor_phone", length = 20)
    private String supervisorPhone;
    
    // Documents
    @Column(name = "offer_letter_url", length = 500)
    private String offerLetterUrl;
    
    @Column(name = "experience_letter_url", length = 500)
    private String experienceLetterUrl;
    
    // Timestamps
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_profile_id", nullable = false)
    @JsonIgnore
    private UserProfile userProfile;
    
    // Enums
    public enum EmploymentType {
        INTERNSHIP, PART_TIME, FULL_TIME, CONTRACT, FREELANCE
    }
    
    // Utility methods
    public String getDurationString() {
        if (startDate == null) return "Unknown duration";
        
        if (Boolean.TRUE.equals(isCurrent) || endDate == null) {
            return startDate.toString() + " to Present";
        }
        
        return startDate.toString() + " to " + endDate.toString();
    }
}
