package com.placement.expo.repository;

import com.placement.expo.entity.AcademicRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AcademicRecordRepository extends JpaRepository<AcademicRecord, Long> {
    
    // Find by user profile
    List<AcademicRecord> findByUserProfileIdOrderByEducationLevelAsc(Long userProfileId);
    
    // Find by education level
    List<AcademicRecord> findByUserProfileIdAndEducationLevel(
        Long userProfileId, AcademicRecord.EducationLevel educationLevel);
    
    // Find current education
    List<AcademicRecord> findByUserProfileIdAndIsCurrentTrue(Long userProfileId);
    
    // Find by institution
    List<AcademicRecord> findByInstitutionNameContainingIgnoreCase(String institutionName);
    
    // Performance queries
    @Query("SELECT ar FROM AcademicRecord ar WHERE ar.userProfile.id = :userProfileId AND ar.cgpa >= :minCgpa")
    List<AcademicRecord> findByUserProfileAndMinCgpa(
        @Param("userProfileId") Long userProfileId, @Param("minCgpa") java.math.BigDecimal minCgpa);
}
