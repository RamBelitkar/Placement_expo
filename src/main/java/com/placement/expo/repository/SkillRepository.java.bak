package com.placement.expo.repository;

import com.placement.expo.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {
    
    // Find by user profile
    List<Skill> findByUserProfileIdOrderBySkillCategoryAscProficiencyLevelDesc(Long userProfileId);
    
    // Find by skill category
    List<Skill> findByUserProfileIdAndSkillCategory(Long userProfileId, Skill.SkillCategory skillCategory);
    
    // Find verified skills
    List<Skill> findByUserProfileIdAndIsVerifiedTrue(Long userProfileId);
    
    // Find by proficiency level
    List<Skill> findByUserProfileIdAndProficiencyLevel(
        Long userProfileId, Skill.ProficiencyLevel proficiencyLevel);
    
    // Search skills
    List<Skill> findBySkillNameContainingIgnoreCase(String skillName);
    
    // Statistics
    @Query("SELECT s.skillCategory, COUNT(s) FROM Skill s WHERE s.userProfile.id = :userProfileId GROUP BY s.skillCategory")
    List<Object[]> getSkillCategoryCount(@Param("userProfileId") Long userProfileId);
    
    @Query("SELECT s.skillName, COUNT(s) FROM Skill s GROUP BY s.skillName ORDER BY COUNT(s) DESC")
    List<Object[]> getMostPopularSkills();
}
