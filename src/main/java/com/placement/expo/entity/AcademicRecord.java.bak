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
@Table(name = "academic_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AcademicRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Education Level
    @Enumerated(EnumType.STRING)
    @Column(name = "education_level", nullable = false)
    private EducationLevel educationLevel;
    
    @Column(name = "institution_name", nullable = false, length = 200)
    private String institutionName;
    
    @Column(name = "board_university", length = 200)
    private String boardUniversity;
    
    @Column(name = "course_name", length = 100)
    private String courseName;
    
    @Column(name = "specialization", length = 100)
    private String specialization;
    
    // Academic Details
    @Column(name = "start_date")
    private LocalDate startDate;
    
    @Column(name = "end_date")
    private LocalDate endDate;
    
    @Column(name = "is_current")
    @Builder.Default
    private Boolean isCurrent = false;
    
    // Performance
    @Column(name = "marks_obtained", precision = 8, scale = 2)
    private BigDecimal marksObtained;
    
    @Column(name = "total_marks", precision = 8, scale = 2)
    private BigDecimal totalMarks;
    
    @Column(name = "percentage", precision = 5, scale = 2)
    private BigDecimal percentage;
    
    @Column(name = "cgpa", precision = 4, scale = 2)
    private BigDecimal cgpa;
    
    @Column(name = "grade", length = 10)
    private String grade;
    
    // Additional Info
    @Column(name = "achievements", columnDefinition = "TEXT")
    private String achievements;
    
    @Column(name = "projects", columnDefinition = "TEXT")
    private String projects; // JSON format
    
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
    public enum EducationLevel {
        TENTH("10TH"),
        TWELFTH("12TH"), 
        DIPLOMA("DIPLOMA"),
        GRADUATION("GRADUATION"),
        POST_GRADUATION("POST_GRADUATION");
        
        private final String value;
        
        EducationLevel(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
    }
    
    // Utility methods
    public boolean isCurrentEducation() {
        return Boolean.TRUE.equals(isCurrent);
    }
    
    public String getDisplayName() {
        return educationLevel.getValue() + " - " + institutionName;
    }
}
