# ATS Score Result Model Documentation

The `AtsScoreResult` model is designed to represent the results of an Applicant Tracking System (ATS) analysis of a resume. This document provides guidance on how to use this model in your application.

## Model Structure

The `AtsScoreResult` class has the following properties:

- `overallScore` (Integer): The overall ATS score for the resume (typically on a scale of 0-100).
- `categoryScores` (Map<String, Integer>): Scores for specific categories of the resume, such as Format, Keywords, Experience, etc.
- `strengths` (List<String>): A list of strengths identified in the resume.
- `weaknesses` (List<String>): A list of weaknesses or areas for improvement in the resume.
- `improvements` (List<String>): Specific suggestions for improving the resume's ATS score.

## Usage Examples

### Creating an AtsScoreResult

Using the builder pattern provided by Lombok:

```java
AtsScoreResult result = AtsScoreResult.builder()
        .overallScore(85)
        .categoryScores(categoryScores)  // Map<String, Integer>
        .strengths(strengths)            // List<String>
        .weaknesses(weaknesses)          // List<String>
        .improvements(improvements)      // List<String>
        .build();
```

### Getting a Summary

The `getSummary()` method generates a formatted text summary of the ATS analysis:

```java
String summary = result.getSummary();
System.out.println(summary);
```

Output:
```
Overall ATS Score: 85 out of 100

Strengths:
- Strong education section with relevant degrees
- Well-formatted with clear sections
- Good use of action verbs in experience descriptions

Areas for Improvement:
- Could use more industry-specific keywords
- Some experience descriptions are too generic
```

## Integration with Services

The `AtsScoreResult` model is typically used in conjunction with services that analyze resumes, such as:

1. `AtsAnalysisService`: For analyzing uploaded resumes
2. `ResumeService`: For managing resume uploads and analysis results
3. `UserProfileService`: For associating ATS scores with user profiles

## Tips for Best Practices

1. Always check for null values when accessing lists or maps in the model
2. Use meaningful category names in the `categoryScores` map
3. Keep strength and weakness descriptions concise and actionable
4. Consider providing a UI component to display the ATS score in a visual way (e.g., gauge, chart)

## Sample Implementation

See the `com.placement.expo.example.AtsScoreExampleUsage` class for a complete example of how to use this model.
