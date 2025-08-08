package com.placement.expo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@EnableConfigurationProperties
@OpenAPIDefinition(
    info = @Info(
        title = "Placement Expo API",
        version = "1.0",
        description = "API for Placement Expo application with Appwrite integration"
    )
)
public class PlacementExpoApplication {
    public static void main(String[] args) {
        SpringApplication.run(PlacementExpoApplication.class, args);
    }
}
