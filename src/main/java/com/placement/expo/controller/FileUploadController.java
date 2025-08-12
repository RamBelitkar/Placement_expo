package com.placement.expo.controller;

import com.placement.expo.model.AtsScoreResult;
import com.placement.expo.service.AtsScoreService;
import com.placement.expo.service.UserProfileSimpleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/upload")
@Slf4j
@CrossOrigin(origins = "*")
public class FileUploadController {
    
    @Value("${app.upload.dir:uploads}")
    private String uploadDir;
    
    @Autowired
    private UserProfileSimpleService userProfileService;
    
    @Autowired
    private AtsScoreService atsScoreService;
    
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final String[] ALLOWED_EXTENSIONS = {".pdf", ".doc", ".docx"};
    private static final String[] ALLOWED_MIME_TYPES = {
        "application/pdf", 
        "application/msword", 
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    };
    
    // Sanitize filename to prevent path traversal attacks
    private String sanitizeFilename(String filename) {
        if (filename == null) return null;
        
        // Remove path separators and special characters
        String sanitized = filename.replaceAll("[/\\\\:*?\"<>|]", "");
        
        // Limit filename length
        if (sanitized.length() > 100) {
            String extension = "";
            int lastDot = sanitized.lastIndexOf('.');
            if (lastDot > 0) {
                extension = sanitized.substring(lastDot);
                sanitized = sanitized.substring(0, Math.min(96, lastDot)) + extension;
            } else {
                sanitized = sanitized.substring(0, 100);
            }
        }
        
        return sanitized;
    }
    
    @PostMapping("/resume")
    public ResponseEntity<Map<String, Object>> uploadResume(
            @RequestParam("file") MultipartFile file,
            @RequestHeader("X-Appwrite-User-Id") String appwriteUserId) {
        
        try {
            // Validate file
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Please select a file to upload"
                ));
            }
            
            // Check file size
            if (file.getSize() > MAX_FILE_SIZE) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "File size must be less than 5MB"
                ));
            }
            
            // Check file extension and MIME type
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Invalid file"
                ));
            }
            
            // Sanitize filename
            String sanitizedFilename = sanitizeFilename(originalFilename);
            if (sanitizedFilename == null || sanitizedFilename.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Invalid filename"
                ));
            }
            
            // Check file extension
            boolean validExtension = false;
            for (String ext : ALLOWED_EXTENSIONS) {
                if (sanitizedFilename.toLowerCase().endsWith(ext)) {
                    validExtension = true;
                    break;
                }
            }
            
            if (!validExtension) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Only PDF, DOC, and DOCX files are allowed"
                ));
            }
            
            // Check MIME type for additional security
            String contentType = file.getContentType();
            boolean validMimeType = false;
            if (contentType != null) {
                for (String mimeType : ALLOWED_MIME_TYPES) {
                    if (contentType.equals(mimeType)) {
                        validMimeType = true;
                        break;
                    }
                }
            }
            
            if (!validMimeType) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Invalid file type. Only PDF, DOC, and DOCX files are allowed"
                ));
            }
            
            // Create upload directory if it doesn't exist
            Path uploadPath = Paths.get(uploadDir, "resumes");
            Files.createDirectories(uploadPath);
            
            // Generate unique filename using sanitized original name
            String fileExtension = sanitizedFilename.substring(sanitizedFilename.lastIndexOf("."));
            String baseFileName = sanitizedFilename.substring(0, sanitizedFilename.lastIndexOf("."));
            String fileName = appwriteUserId + "_" + baseFileName + "_" + UUID.randomUUID().toString() + fileExtension;
            Path filePath = uploadPath.resolve(fileName);
            
            // Save file
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            
            // Generate accessible URL
            String fileUrl = "/uploads/resumes/" + fileName;
            
            log.info("Resume uploaded successfully for user {}: {}", appwriteUserId, fileName);
            
            // Calculate ATS score for the resume
            String fullFilePath = uploadPath.resolve(fileName).toString();
            log.info("Starting ATS analysis for user {} with file: {}", appwriteUserId, fullFilePath);
            AtsScoreResult atsScore;
            try {
                atsScore = atsScoreService.analyzeResume(appwriteUserId, fullFilePath);
                log.info("ATS score calculated successfully for user {}: {}", appwriteUserId, atsScore.getOverallScore());
            } catch (Exception e) {
                log.error("Error during ATS analysis for user {}: {}", appwriteUserId, e.getMessage(), e);
                // If analysis fails, create a default score result
                atsScore = AtsScoreResult.builder()
                    .overallScore(0)
                    .categoryScores(java.util.Collections.singletonMap("Error", 0))
                    .strengths(java.util.Collections.emptyList())
                    .weaknesses(java.util.Collections.singletonList("Resume analysis failed"))
                    .improvements(java.util.Collections.singletonList("Please try uploading your resume again"))
                    .build();
            }
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Resume uploaded and analyzed successfully",
                "data", Map.of(
                    "fileName", fileName,
                    "originalName", originalFilename,
                    "fileUrl", fileUrl,
                    "size", file.getSize(),
                    "atsScore", atsScore.getOverallScore(),
                    "atsFeedback", atsScore.getSummary()
                )
            ));
            
        } catch (IOException e) {
            log.error("Failed to upload resume for user {}: {}", appwriteUserId, e.getMessage());
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "message", "Failed to upload file: " + e.getMessage()
            ));
        }
    }
    
    @DeleteMapping("/resume/{fileName}")
    public ResponseEntity<Map<String, Object>> deleteResume(
            @PathVariable String fileName,
            @RequestHeader("X-Appwrite-User-Id") String appwriteUserId) {
        
        try {
            // Ensure user can only delete their own files
            if (!fileName.startsWith(appwriteUserId + "_")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(
                    "success", false,
                    "message", "Unauthorized to delete this file"
                ));
            }
            
            Path filePath = Paths.get(uploadDir, "resumes", fileName);
            
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                log.info("Resume deleted successfully for user {}: {}", appwriteUserId, fileName);
                
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Resume deleted successfully"
                ));
            } else {
                return ResponseEntity.notFound().build();
            }
            
        } catch (IOException e) {
            log.error("Failed to delete resume for user {}: {}", appwriteUserId, e.getMessage());
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "message", "Failed to delete file: " + e.getMessage()
            ));
        }
    }
}
