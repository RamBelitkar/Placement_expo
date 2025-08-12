package com.placement.expo.config;

import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;

@Configuration
public class FileUploadConfig implements WebMvcConfigurer {
    
    private static final Logger log = LoggerFactory.getLogger(FileUploadConfig.class);
    
    @Value("${app.upload.dir:uploads}")
    private String uploadDir;
    
    @PostConstruct
    public void init() {
        // Create upload directories on startup
        try {
            File uploadsDir = new File(uploadDir);
            if (!uploadsDir.exists()) {
                uploadsDir.mkdirs();
                log.info("Created uploads directory: {}", uploadsDir.getAbsolutePath());
            }
            
            File resumesDir = new File(uploadDir + "/resumes");
            if (!resumesDir.exists()) {
                resumesDir.mkdirs();
                log.info("Created resumes directory: {}", resumesDir.getAbsolutePath());
            }
        } catch (Exception e) {
            log.error("Failed to create upload directories: {}", e.getMessage(), e);
        }
    }
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadDir + "/");
        log.info("Configured resource handler for uploads: {}", uploadDir);
    }
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("*");
        log.info("CORS configuration added for /api/** endpoints");
    }
    
    @Bean
    public MultipartResolver multipartResolver() {
        StandardServletMultipartResolver resolver = new StandardServletMultipartResolver();
        return resolver;
    }
}
