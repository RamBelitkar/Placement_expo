package com.placement.expo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "skills")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Skill {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Skill Information
    @Column(name = "skill_name", nullable = false, length = 100)
    private String skillName;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "skill_category", nullable = false)
    private SkillCategory skillCategory;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "proficiency_level", nullable = false)
    private ProficiencyLevel proficiencyLevel;
    
    // Verification
    @Column(name = "is_verified")
    @Builder.Default
    private Boolean isVerified = false;
    
    @Column(name = "verification_source", length = 200)
    private String verificationSource;
    
    @Column(name = "certificate_url", length = 500)
    private String certificateUrl;
    
    // Experience
    @Column(name = "years_of_experience", precision = 3, scale = 1)
    @Builder.Default
    private BigDecimal yearsOfExperience = BigDecimal.ZERO;
    
    @Column(name = "last_used_date")
    private LocalDate lastUsedDate;
    
    // Timestamp
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_profile_id", nullable = false)
    @JsonIgnore
    private UserProfile userProfile;
    
    // Enums
    public enum SkillCategory {
        TECHNICAL, SOFT, LANGUAGE, CERTIFICATION
    }
    
    public enum ProficiencyLevel {
        BEGINNER, INTERMEDIATE, ADVANCED, EXPERT
    }
    
    // Utility methods
    public String getDisplayName() {
        return skillName + " (" + proficiencyLevel.toString().toLowerCase() + ")";
    }
    
    public boolean isRecentlyUsed() {
        if (lastUsedDate == null) return false;
        return lastUsedDate.isAfter(LocalDate.now().minusMonths(6));
    }
}
