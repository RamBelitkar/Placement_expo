package com.placement.expo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "certifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Certification {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Certification Details
    @Column(name = "certification_name", nullable = false, length = 200)
    private String certificationName;
    
    @Column(name = "issuing_organization", nullable = false, length = 200)
    private String issuingOrganization;
    
    @Column(name = "certification_category", length = 100)
    private String certificationCategory;
    
    // Dates
    @Column(name = "issue_date", nullable = false)
    private LocalDate issueDate;
    
    @Column(name = "expiry_date")
    private LocalDate expiryDate;
    
    @Column(name = "is_lifetime_valid")
    @Builder.Default
    private Boolean isLifetimeValid = false;
    
    // Verification
    @Column(name = "credential_id", length = 200)
    private String credentialId;
    
    @Column(name = "credential_url", length = 500)
    private String credentialUrl;
    
    @Column(name = "certificate_file_url", length = 500)
    private String certificateFileUrl;
    
    // Additional Info
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "skills_gained", columnDefinition = "TEXT")
    private String skillsGained; // JSON format
    
    // Timestamp
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_profile_id", nullable = false)
    @JsonIgnore
    private UserProfile userProfile;
    
    // Utility methods
    public boolean isExpired() {
        if (Boolean.TRUE.equals(isLifetimeValid) || expiryDate == null) {
            return false;
        }
        return expiryDate.isBefore(LocalDate.now());
    }
    
    public String getStatusString() {
        if (Boolean.TRUE.equals(isLifetimeValid)) {
            return "Lifetime Valid";
        }
        if (isExpired()) {
            return "Expired";
        }
        if (expiryDate != null) {
            return "Valid until " + expiryDate.toString();
        }
        return "Valid";
    }
}
