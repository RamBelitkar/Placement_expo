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
@Table(name = "job_applications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobApplication {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Application Details
    @Column(name = "position_title", nullable = false, length = 200)
    private String positionTitle;
    
    @Column(name = "job_description", columnDefinition = "TEXT")
    private String jobDescription;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "application_source")
    @Builder.Default
    private ApplicationSource applicationSource = ApplicationSource.CAMPUS;
    
    // Application Status
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @Builder.Default
    private ApplicationStatus status = ApplicationStatus.APPLIED;
    
    @Column(name = "current_round", length = 100)
    private String currentRound;
    
    // Dates
    @CreationTimestamp
    @Column(name = "application_date")
    private LocalDateTime applicationDate;
    
    @Column(name = "interview_date")
    private LocalDateTime interviewDate;
    
    @Column(name = "result_date")
    private LocalDate resultDate;
    
    @Column(name = "joining_date")
    private LocalDate joiningDate;
    
    // Package Details
    @Column(name = "offered_package", precision = 12, scale = 2)
    private BigDecimal offeredPackage;
    
    @Column(name = "package_currency", length = 10)
    @Builder.Default
    private String packageCurrency = "INR";
    
    @Column(name = "package_breakdown", columnDefinition = "TEXT")
    private String packageBreakdown; // JSON format
    
    // Interview Details
    @Column(name = "interview_feedback", columnDefinition = "TEXT")
    private String interviewFeedback;
    
    @Column(name = "interview_score", precision = 5, scale = 2)
    private BigDecimal interviewScore;
    
    @Column(name = "interviewer_notes", columnDefinition = "TEXT")
    private String interviewerNotes;
    
    // Documents
    @Column(name = "application_form_url", length = 500)
    private String applicationFormUrl;
    
    @Column(name = "resume_submitted_url", length = 500)
    private String resumeSubmittedUrl;
    
    @Column(name = "offer_letter_url", length = 500)
    private String offerLetterUrl;
    
    // Additional Info
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    @Column(name = "follow_up_date")
    private LocalDate followUpDate;
    
    // Timestamp
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_profile_id", nullable = false)
    @JsonIgnore
    private UserProfile userProfile;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    @JsonIgnore
    private Company company;
    
    // Enums
    public enum ApplicationSource {
        CAMPUS, ONLINE, REFERRAL, WALK_IN
    }
    
    public enum ApplicationStatus {
        APPLIED, SCREENING, INTERVIEW, SELECTED, REJECTED, WITHDRAWN
    }
    
    // Utility methods
    public boolean isActive() {
        return status == ApplicationStatus.APPLIED || 
               status == ApplicationStatus.SCREENING || 
               status == ApplicationStatus.INTERVIEW;
    }
    
    public boolean isSuccessful() {
        return status == ApplicationStatus.SELECTED;
    }
    
    public String getStatusColor() {
        return switch (status) {
            case APPLIED -> "blue";
            case SCREENING -> "orange";
            case INTERVIEW -> "purple";
            case SELECTED -> "green";
            case REJECTED -> "red";
            case WITHDRAWN -> "gray";
        };
    }
}
