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
@Table(name = "projects")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Project {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Project Information
    @Column(name = "project_name", nullable = false, length = 200)
    private String projectName;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "project_type", nullable = false)
    private ProjectType projectType;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "detailed_description", columnDefinition = "TEXT")
    private String detailedDescription;
    
    // Timeline
    @Column(name = "start_date")
    private LocalDate startDate;
    
    @Column(name = "end_date")
    private LocalDate endDate;
    
    @Column(name = "is_ongoing")
    @Builder.Default
    private Boolean isOngoing = false;
    
    // Technical Details
    @Column(name = "technologies_used", columnDefinition = "TEXT")
    private String technologiesUsed; // JSON format
    
    @Column(name = "project_role", length = 100)
    private String projectRole;
    
    @Column(name = "team_size")
    private Integer teamSize;
    
    // Links and Files
    @Column(name = "project_url", length = 500)
    private String projectUrl;
    
    @Column(name = "github_url", length = 500)
    private String githubUrl;
    
    @Column(name = "demo_url", length = 500)
    private String demoUrl;
    
    @Column(name = "documentation_url", length = 500)
    private String documentationUrl;
    
    // Impact
    @Column(name = "achievements", columnDefinition = "TEXT")
    private String achievements;
    
    @Column(name = "challenges_faced", columnDefinition = "TEXT")
    private String challengesFaced;
    
    @Column(name = "learning_outcomes", columnDefinition = "TEXT")
    private String learningOutcomes;
    
    // Visibility
    @Column(name = "is_featured")
    @Builder.Default
    private Boolean isFeatured = false;
    
    @Column(name = "display_order")
    @Builder.Default
    private Integer displayOrder = 0;
    
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
    public enum ProjectType {
        ACADEMIC, PERSONAL, INTERNSHIP, FREELANCE, OPEN_SOURCE
    }
    
    // Utility methods
    public String getDurationString() {
        if (startDate == null) return "Unknown duration";
        
        if (Boolean.TRUE.equals(isOngoing) || endDate == null) {
            return "Started " + startDate.toString();
        }
        
        return startDate.toString() + " to " + endDate.toString();
    }
}
