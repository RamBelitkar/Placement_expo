package com.placement.expo.repository;

import com.placement.expo.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    
    // Find by user profile
    List<Project> findByUserProfileIdOrderByDisplayOrderAscCreatedAtDesc(Long userProfileId);
    
    // Find featured projects
    List<Project> findByUserProfileIdAndIsFeaturedTrueOrderByDisplayOrderAsc(Long userProfileId);
    
    // Find by project type
    List<Project> findByUserProfileIdAndProjectType(Long userProfileId, Project.ProjectType projectType);
    
    // Find ongoing projects
    List<Project> findByUserProfileIdAndIsOngoingTrue(Long userProfileId);
    
    // Search projects
    List<Project> findByProjectNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
        String projectName, String description);
    
    // Statistics
    @Query("SELECT p.projectType, COUNT(p) FROM Project p WHERE p.userProfile.id = :userProfileId GROUP BY p.projectType")
    List<Object[]> getProjectTypeCount(@Param("userProfileId") Long userProfileId);
}
