package com.placement.expo.controller;

import com.placement.expo.model.AtsScoreResult;
import com.placement.expo.service.AtsAnalysisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ats")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class AtsAnalysisController {
    
    private final AtsAnalysisService atsAnalysisService;
    
    /**
     * Endpoint for analyzing a resume file
     * 
     * @param resumeFile The resume file to analyze
     * @return ATS score result with feedback
     */
    @PostMapping(value = "/analyze", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> analyzeResume(
            @RequestParam("file") MultipartFile resumeFile,
            @RequestHeader(value = "X-Appwrite-User-Id", required = false) String headerUserId,
            @RequestParam(value = "userId", required = false) String paramUserId) {
        
        // Use the user ID from header if available, otherwise use the one from parameters
        String userId = headerUserId != null ? headerUserId : paramUserId;
        
        try {
            log.info("Analyzing resume for user: {}", userId != null ? userId : "anonymous");
            
            if (resumeFile.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Please upload a resume file"
                ));
            }
            
            // Analyze the resume
            AtsScoreResult result = atsAnalysisService.analyzeResume(resumeFile);
            
            // If userId is provided, save the score to the user's profile
            if (userId != null && !userId.isEmpty()) {
                log.info("Updating profile with ATS score for user ID: {}", userId);
                try {
                    atsAnalysisService.saveAtsScoreToProfile(userId, result);
                    log.info("Successfully saved ATS score to user profile");
                } catch (Exception e) {
                    log.warn("Failed to save ATS score to profile: {}", e.getMessage());
                    // Continue to return the analysis result even if saving to profile fails
                }
            }
            
            // Return the result
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", result);
            response.put("summary", result.getSummary());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Failed to analyze resume: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Error analyzing resume: " + e.getMessage()
            ));
        }
    }
    
    /**
     * Endpoint for getting sample ATS results (for demo purposes)
     * 
     * @return Sample ATS score result
     */
    @GetMapping("/sample")
    public ResponseEntity<Map<String, Object>> getSampleResult() {
        try {
            // Create a sample result using builder pattern
            Map<String, Integer> categoryScores = new HashMap<>();
            categoryScores.put("Format", 85);
            categoryScores.put("Keywords", 75);
            categoryScores.put("Experience", 80);
            categoryScores.put("Education", 90);
            categoryScores.put("Skills", 60);
            
            List<String> strengths = new ArrayList<>();
            strengths.add("Clear professional summary");
            strengths.add("Good education credentials");
            strengths.add("Well-structured format");
            
            List<String> weaknesses = new ArrayList<>();
            weaknesses.add("Limited technical skills section");
            weaknesses.add("Experience lacks quantifiable achievements");
            
            List<String> improvements = new ArrayList<>();
            improvements.add("Add more industry-specific keywords");
            improvements.add("Quantify achievements in work experience");
            improvements.add("Expand technical skills section");
            
            AtsScoreResult sampleResult = AtsScoreResult.builder()
                    .overallScore(78)
                    .categoryScores(categoryScores)
                    .strengths(strengths)
                    .weaknesses(weaknesses)
                    .improvements(improvements)
                    .build();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", sampleResult);
            response.put("summary", sampleResult.getSummary());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Failed to generate sample result: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Error generating sample: " + e.getMessage()
            ));
        }
    }
}
