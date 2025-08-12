package com.placement.expo.service;

import com.placement.expo.entity.UserProfileSimple;
import com.placement.expo.model.AtsScoreResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service to analyze resumes and provide ATS scoring
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AtsAnalysisService {

    private final UserProfileSimpleService userProfileService;

    /**
     * Analyzes a resume file and returns an ATS score result
     * 
     * @param resumeFile The resume file to analyze
     * @return AtsScoreResult with scoring and feedback
     */
    public AtsScoreResult analyzeResume(MultipartFile resumeFile) {
        // In a real implementation, this would integrate with an actual ATS API
        // This is a mock implementation for demonstration purposes
        
        // Mock category scores
        Map<String, Integer> categoryScores = new HashMap<>();
        categoryScores.put("Format", 85);
        categoryScores.put("Keywords", 75);
        categoryScores.put("Experience", 90);
        categoryScores.put("Education", 95);
        categoryScores.put("Skills", 80);
        
        // Calculate overall score (average of categories)
        int overallScore = categoryScores.values().stream()
                .mapToInt(Integer::intValue)
                .sum() / categoryScores.size();
        
        // Mock strengths
        List<String> strengths = new ArrayList<>();
        strengths.add("Strong education section with relevant degrees");
        strengths.add("Well-formatted with clear sections");
        strengths.add("Good use of action verbs in experience descriptions");
        
        // Mock weaknesses
        List<String> weaknesses = new ArrayList<>();
        weaknesses.add("Could use more industry-specific keywords");
        weaknesses.add("Some experience descriptions are too generic");
        
        // Mock improvement suggestions
        List<String> improvements = new ArrayList<>();
        improvements.add("Add more specific technical skills relevant to job targets");
        improvements.add("Quantify achievements with metrics where possible");
        improvements.add("Ensure each job description emphasizes accomplishments, not just responsibilities");
        
        // Build and return the result
        return AtsScoreResult.builder()
                .overallScore(overallScore)
                .categoryScores(categoryScores)
                .strengths(strengths)
                .weaknesses(weaknesses)
                .improvements(improvements)
                .build();
    }
    
    /**
     * Saves the ATS score result to a user's profile
     * 
     * @param appwriteUserId The ID of the user
     * @param atsScoreResult The ATS score result to save
     * @return true if successful, false otherwise
     */
    public boolean saveAtsScoreToProfile(String appwriteUserId, AtsScoreResult atsScoreResult) {
        log.info("Saving ATS score to profile for user: {}", appwriteUserId);
        
        try {
            Optional<UserProfileSimple> profileOpt = userProfileService.getProfileByAppwriteUserId(appwriteUserId);
            
            if (profileOpt.isEmpty()) {
                log.warn("No profile found for user {}, cannot save ATS score", appwriteUserId);
                return false;
            }
            
            UserProfileSimple profile = profileOpt.get();
            
            // Update profile with ATS score
            profile.setAtsScore(atsScoreResult.getOverallScore());
            profile.setAtsFeedback(atsScoreResult.getSummary());
            
            // Save updated profile
            userProfileService.saveProfile(profile);
            
            log.info("Successfully saved ATS score of {} for user {}", 
                    atsScoreResult.getOverallScore(), appwriteUserId);
            
            return true;
            
        } catch (Exception e) {
            log.error("Failed to save ATS score to profile: {}", e.getMessage(), e);
            return false;
        }
    }
}
