package com.placement.expo.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is an example of how to use AtsScoreResult without actually using the real class.
 * This is for documentation purposes only.
 */
public class AtsScoreExampleUsage {

    public static void main(String[] args) {
        // Sample usage of AtsScoreResult
        
        // 1. Create category scores
        Map<String, Integer> categoryScores = new HashMap<>();
        categoryScores.put("Format", 85);
        categoryScores.put("Keywords", 75);
        categoryScores.put("Experience", 90);
        categoryScores.put("Education", 95);
        categoryScores.put("Skills", 80);
        
        // 2. Calculate overall score (average of categories)
        int overallScore = categoryScores.values().stream()
                .mapToInt(Integer::intValue)
                .sum() / categoryScores.size();
        
        // 3. Create strengths list
        List<String> strengths = new ArrayList<>();
        strengths.add("Strong education section with relevant degrees");
        strengths.add("Well-formatted with clear sections");
        strengths.add("Good use of action verbs in experience descriptions");
        
        // 4. Create weaknesses list
        List<String> weaknesses = new ArrayList<>();
        weaknesses.add("Could use more industry-specific keywords");
        weaknesses.add("Some experience descriptions are too generic");
        
        // 5. Create improvement suggestions list
        List<String> improvements = new ArrayList<>();
        improvements.add("Add more specific technical skills relevant to job targets");
        improvements.add("Quantify achievements with metrics where possible");
        improvements.add("Ensure each job description emphasizes accomplishments, not just responsibilities");
        
        // 6. In a real application, you would build the AtsScoreResult like this:
        // AtsScoreResult result = AtsScoreResult.builder()
        //         .overallScore(overallScore)
        //         .categoryScores(categoryScores)
        //         .strengths(strengths)
        //         .weaknesses(weaknesses)
        //         .improvements(improvements)
        //         .build();
        
        // 7. Print out results (simulating actual AtsScoreResult usage)
        System.out.println("==============================================");
        System.out.println("          ATS SCORING DEMONSTRATION          ");
        System.out.println("==============================================");
        
        System.out.println("ATS Score: " + overallScore);
        
        System.out.println("\nCategory Scores:");
        categoryScores.forEach((category, score) -> 
            System.out.println("  - " + category + ": " + score));
        
        System.out.println("\nStrengths:");
        strengths.forEach(s -> System.out.println("  - " + s));
        
        System.out.println("\nWeaknesses:");
        weaknesses.forEach(w -> System.out.println("  - " + w));
        
        System.out.println("\nImprovement Suggestions:");
        improvements.forEach(i -> System.out.println("  - " + i));
        
        System.out.println("==============================================");
    }
}
