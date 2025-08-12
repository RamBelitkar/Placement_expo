package com.placement.expo.sample;

import com.placement.expo.model.AtsScoreResult;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Sample demonstration of the AtsScoreResult model
 * This class runs automatically when the application starts
 */
@Component
public class AtsScoringDemo implements CommandLineRunner {

    @Override
    public void run(String... args) {
        System.out.println("==============================================");
        System.out.println("          ATS SCORING DEMONSTRATION          ");
        System.out.println("==============================================");
        
        // Create a sample ATS score result
        AtsScoreResult result = createSampleAtsResult();
        
        // Print the result
        System.out.println("ATS Score: " + result.getOverallScore());
        System.out.println("\nCategory Scores:");
        result.getCategoryScores().forEach((category, score) -> 
            System.out.println("  - " + category + ": " + score));
            
        System.out.println("\nSummary:");
        System.out.println(result.getSummary());
        
        System.out.println("==============================================");
    }
    
    /**
     * Creates a sample ATS score result for demonstration
     * 
     * @return Sample ATS score result
     */
    private AtsScoreResult createSampleAtsResult() {
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
}
