package com.placement.expo.repository;

import com.placement.expo.entity.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    
    // Find by user profile
    List<JobApplication> findByUserProfileIdOrderByApplicationDateDesc(Long userProfileId);
    
    // Find by status
    List<JobApplication> findByUserProfileIdAndStatus(Long userProfileId, JobApplication.ApplicationStatus status);
    
    // Find active applications
    @Query("SELECT ja FROM JobApplication ja WHERE ja.userProfile.id = :userProfileId AND ja.status IN ('APPLIED', 'SCREENING', 'INTERVIEW')")
    List<JobApplication> findActiveApplicationsByUserProfileId(@Param("userProfileId") Long userProfileId);
    
    // Find by company
    List<JobApplication> findByCompanyIdOrderByApplicationDateDesc(Long companyId);
    
    // Find recent applications
    List<JobApplication> findByUserProfileIdAndApplicationDateAfterOrderByApplicationDateDesc(
        Long userProfileId, LocalDateTime since);
    
    // Statistics queries
    @Query("SELECT ja.status, COUNT(ja) FROM JobApplication ja WHERE ja.userProfile.id = :userProfileId GROUP BY ja.status")
    List<Object[]> getApplicationStatusCount(@Param("userProfileId") Long userProfileId);
    
    @Query("SELECT COUNT(ja) FROM JobApplication ja WHERE ja.userProfile.id = :userProfileId AND ja.status = 'SELECTED'")
    Long countSuccessfulApplications(@Param("userProfileId") Long userProfileId);
    
    @Query("SELECT COUNT(ja) FROM JobApplication ja WHERE ja.userProfile.id = :userProfileId AND ja.interviewDate IS NOT NULL")
    Long countInterviewsAttended(@Param("userProfileId") Long userProfileId);
    
    @Query("SELECT MAX(ja.offeredPackage) FROM JobApplication ja WHERE ja.userProfile.id = :userProfileId AND ja.offeredPackage IS NOT NULL")
    java.math.BigDecimal findHighestPackageOffered(@Param("userProfileId") Long userProfileId);
}
