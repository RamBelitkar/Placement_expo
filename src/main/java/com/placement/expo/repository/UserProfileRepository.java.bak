package com.placement.expo.repository;

import com.placement.expo.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    
    // Find by Appwrite User ID (primary lookup method)
    Optional<UserProfile> findByAppwriteUserId(String appwriteUserId);
    
    // Check if profile exists
    boolean existsByAppwriteUserId(String appwriteUserId);
    
    // Find by unique identifiers
    Optional<UserProfile> findByStudentId(String studentId);
    Optional<UserProfile> findByRollNumber(String rollNumber);
    
    // Search by name
    List<UserProfile> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
        String firstName, String lastName);
    
    // Filter by department
    List<UserProfile> findByDepartmentIgnoreCase(String department);
    
    // Filter by graduation year
    List<UserProfile> findByCurrentYear(Integer currentYear);
    
    // Filter by verification status
    List<UserProfile> findByIsProfileVerified(Boolean isVerified);
    
    // Advanced queries
    @Query("SELECT up FROM UserProfile up WHERE up.profileCompletionPercentage >= :minCompletion")
    List<UserProfile> findProfilesWithMinCompletion(@Param("minCompletion") Integer minCompletion);
    
    @Query("SELECT up FROM UserProfile up WHERE up.currentCgpa >= :minCgpa")
    List<UserProfile> findProfilesWithMinCgpa(@Param("minCgpa") java.math.BigDecimal minCgpa);
    
    @Query("SELECT up FROM UserProfile up WHERE up.backlogs <= :maxBacklogs")
    List<UserProfile> findProfilesWithMaxBacklogs(@Param("maxBacklogs") Integer maxBacklogs);
    
    // Statistics queries
    @Query("SELECT COUNT(up) FROM UserProfile up WHERE up.department = :department")
    Long countByDepartment(@Param("department") String department);
    
    @Query("SELECT up.department, COUNT(up) FROM UserProfile up GROUP BY up.department")
    List<Object[]> getDepartmentWiseCount();
    
    @Query("SELECT AVG(up.currentCgpa) FROM UserProfile up WHERE up.currentCgpa IS NOT NULL")
    java.math.BigDecimal getAverageCgpa();
}
