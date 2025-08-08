package com.placement.expo.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "appwrite")
@Getter
@Setter
public class AppwriteConfig {
    private String endpoint;
    private String projectId;
}
