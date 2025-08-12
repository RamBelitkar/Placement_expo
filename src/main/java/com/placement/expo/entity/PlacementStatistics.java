package com.placement.expo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "placement_statistics")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlacementStatistics {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Overall Stats
    @Column(name = "total_applications")
    @Builder.Default
    private Integer totalApplications = 0;
    
    @Column(name = "applications_pending")
    @Builder.Default
    private Integer applicationsPending = 0;
    
    @Column(name = "applications_rejected")
    @Builder.Default
    private Integer applicationsRejected = 0;
    
    @Column(name = "interviews_attended")
    @Builder.Default
    private Integer interviewsAttended = 0;
    
    @Column(name = "offers_received")
    @Builder.Default
    private Integer offersReceived = 0;
    
    // Best Metrics
    @Column(name = "highest_package_offered", precision = 12, scale = 2)
    private BigDecimal highestPackageOffered;
    
    @Column(name = "best_company_selected", length = 200)
    private String bestCompanySelected;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "placement_status")
    @Builder.Default
    private PlacementStatus placementStatus = PlacementStatus.SEEKING;
    
    // Profile Metrics
    @Column(name = "profile_views")
    @Builder.Default
    private Integer profileViews = 0;
    
    @Column(name = "profile_score")
    @Builder.Default
    private Integer profileScore = 0;
    
    @Column(name = "last_activity_date")
    private LocalDateTime lastActivityDate;
    
    // Calculated Fields
    @Column(name = "success_rate", precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal successRate = BigDecimal.ZERO;
    
    @Column(name = "average_interview_score", precision = 5, scale = 2)
    private BigDecimal averageInterviewScore;
    
    // Updates
    @Column(name = "last_calculated")
    private LocalDateTime lastCalculated;
    
    @PrePersist
    @PreUpdate
    protected void updateTimestamp() {
        lastCalculated = LocalDateTime.now();
    }
    
    // Relationships
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_profile_id", nullable = false, unique = true)
    @JsonIgnore
    private UserProfile userProfile;
    
    // Enums
    public enum PlacementStatus {
        SEEKING, PLACED, HIGHER_STUDIES, ENTREPRENEUR
    }
    
    // Utility methods
    public void incrementTotalApplications() {
        this.totalApplications = (this.totalApplications == null ? 0 : this.totalApplications) + 1;
        recalculateSuccessRate();
    }
    
    public void incrementInterviewsAttended() {
        this.interviewsAttended = (this.interviewsAttended == null ? 0 : this.interviewsAttended) + 1;
    }
    
    public void incrementOffersReceived() {
        this.offersReceived = (this.offersReceived == null ? 0 : this.offersReceived) + 1;
        recalculateSuccessRate();
    }
    
    public void incrementRejections() {
        this.applicationsRejected = (this.applicationsRejected == null ? 0 : this.applicationsRejected) + 1;
        recalculateSuccessRate();
    }
    
    public void updateHighestPackage(BigDecimal packageAmount) {
        if (packageAmount != null && (this.highestPackageOffered == null || 
            packageAmount.compareTo(this.highestPackageOffered) > 0)) {
            this.highestPackageOffered = packageAmount;
        }
    }
    
    public void incrementProfileViews() {
        this.profileViews = (this.profileViews == null ? 0 : this.profileViews) + 1;
    }
    
    private void recalculateSuccessRate() {
        if (totalApplications != null && totalApplications > 0 && offersReceived != null) {
            this.successRate = BigDecimal.valueOf(offersReceived)
                .divide(BigDecimal.valueOf(totalApplications), 2, BigDecimal.ROUND_HALF_UP)
                .multiply(BigDecimal.valueOf(100));
        } else {
            this.successRate = BigDecimal.ZERO;
        }
    }
    
    public String getPlacementStatusDisplay() {
        return switch (placementStatus) {
            case SEEKING -> "Actively Seeking";
            case PLACED -> "Successfully Placed";
            case HIGHER_STUDIES -> "Pursuing Higher Studies";
            case ENTREPRENEUR -> "Entrepreneur";
        };
    }
    
    public String getSuccessRateDisplay() {
        if (successRate == null) return "0%";
        return successRate.stripTrailingZeros().toPlainString() + "%";
    }
}
