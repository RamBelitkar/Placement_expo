package com.placement.expo.service;

import com.placement.expo.entity.UserProfileSimple;
import com.placement.expo.model.AtsScoreResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AtsScoreService {

    private final UserProfileSimpleService userProfileService;
    
    /**
     * Analyzes the resume for a user and calculates the ATS score
     * 
     * @param appwriteUserId The user ID to analyze
     * @param resumeFilePath The file path to the resume
     * @return The ATS score result
     */
    public AtsScoreResult analyzeResume(String appwriteUserId, String resumeFilePath) {
        log.info("=== ATS ANALYSIS STARTED ===");
        log.info("Analyzing resume for user {} at path {}", appwriteUserId, resumeFilePath);
        
        try {
            // Check if file exists
            File resumeFile = new File(resumeFilePath);
            if (!resumeFile.exists()) {
                log.error("Resume file not found: {}", resumeFilePath);
                return createDefaultScore("Resume file not found");
            }
            
            log.info("File exists, size: {} bytes", resumeFile.length());
            
            // In a real implementation, this would use an actual ATS scoring system
            // For now, we'll use a mock implementation that generates a reasonable score
            
            // Mock implementation - replace with actual ATS scoring logic
            log.info("Generating ATS score for user: {}", appwriteUserId);
            AtsScoreResult result = generateMockAtsScore(appwriteUserId);
            log.info("ATS score generated: {}", result.getOverallScore());
            
            // Update the user profile with the score
            log.info("Looking up profile for user: {}", appwriteUserId);
            Optional<UserProfileSimple> profileOpt = userProfileService.getProfileByAppwriteUserId(appwriteUserId);
            
            if (profileOpt.isPresent()) {
                UserProfileSimple profile = profileOpt.get();
                log.info("Found profile for user {}: ID={}", appwriteUserId, profile.getId());
                
                profile.setAtsScore(result.getOverallScore());
                profile.setAtsFeedback(result.getSummary());
                
                log.info("Saving profile with ATS score: {}", result.getOverallScore());
                userProfileService.saveProfile(profile);
                log.info("✅ Successfully updated ATS score for user {}: {}", appwriteUserId, result.getOverallScore());
            } else {
                log.warn("❌ No profile found for user: {}, ATS score not saved", appwriteUserId);
            }
            
            log.info("=== ATS ANALYSIS COMPLETED SUCCESSFULLY ===");
            return result;
            
        } catch (Exception e) {
            log.error("❌ Failed to analyze resume: {}", e.getMessage(), e);
            return createDefaultScore("Error analyzing resume: " + e.getMessage());
        }
    }
    
    /**
     * Creates a default score when analysis fails
     */
    private AtsScoreResult createDefaultScore(String error) {
        return AtsScoreResult.builder()
                .overallScore(0)
                .categoryScores(Map.of("error", 0))
                .strengths(List.of())
                .weaknesses(List.of(error))
                .improvements(List.of("Upload a valid resume file"))
                .build();
    }
    
    /**
     * Generates a mock ATS score for testing purposes
     * In a real implementation, this would be replaced by actual ATS scoring logic
     */
    private AtsScoreResult generateMockAtsScore(String userId) {
        // Use the user ID to make the "random" score consistent for the same user
        Random random = new Random(userId.hashCode());
        int overallScore = 60 + random.nextInt(40); // Score between 60-99
        
        Map<String, Integer> categoryScores = new HashMap<>();
        categoryScores.put("format", 70 + random.nextInt(30));
        categoryScores.put("content", 65 + random.nextInt(35));
        categoryScores.put("keywords", 60 + random.nextInt(40));
        categoryScores.put("skills", 75 + random.nextInt(25));
        
        List<String> strengths = new ArrayList<>();
        if (categoryScores.get("format") > 80) {
            strengths.add("Well-formatted resume with clear sections");
        }
        if (categoryScores.get("content") > 80) {
            strengths.add("Strong content with quantifiable achievements");
        }
        if (categoryScores.get("keywords") > 80) {
            strengths.add("Good use of industry-specific keywords");
        }
        if (categoryScores.get("skills") > 80) {
            strengths.add("Relevant skills highlighted effectively");
        }
        
        List<String> weaknesses = new ArrayList<>();
        if (categoryScores.get("format") < 80) {
            weaknesses.add("Resume formatting could be improved");
        }
        if (categoryScores.get("content") < 80) {
            weaknesses.add("Content lacks specific achievements");
        }
        if (categoryScores.get("keywords") < 80) {
            weaknesses.add("Missing important industry keywords");
        }
        if (categoryScores.get("skills") < 80) {
            weaknesses.add("Technical skills section needs enhancement");
        }
        
        List<String> improvements = Arrays.asList(
            "Add more quantifiable achievements",
            "Include relevant industry keywords",
            "Highlight technical skills more prominently",
            "Use a clearer section structure"
        );
        
        return AtsScoreResult.builder()
                .overallScore(overallScore)
                .categoryScores(categoryScores)
                .strengths(strengths)
                .weaknesses(weaknesses)
                .improvements(improvements)
                .build();
    }
}
