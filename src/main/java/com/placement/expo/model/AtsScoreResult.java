package com.placement.expo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * Model class for ATS (Applicant Tracking System) scoring results
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AtsScoreResult {
    
    private Integer overallScore;
    private Map<String, Integer> categoryScores;
    private List<String> strengths;
    private List<String> weaknesses;
    private List<String> improvements;
    
    /**
     * Provides a summary of the ATS results
     * 
     * @return A formatted summary of the ATS analysis
     */
    public String getSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("Overall ATS Score: ").append(overallScore != null ? overallScore : 0).append(" out of 100\n\n");
        
        summary.append("Strengths:\n");
        if (strengths != null) {
            strengths.forEach(s -> summary.append("- ").append(s).append("\n"));
        }
        summary.append("\n");
        
        summary.append("Areas for Improvement:\n");
        if (weaknesses != null) {
            weaknesses.forEach(w -> summary.append("- ").append(w).append("\n"));
        }
        
        return summary.toString();
    }
}
