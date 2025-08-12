package com.placement.expo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors().and()
            .csrf().disable()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers(
                "/",
                "/index.html",
                "/login.html",
                "/register.html",
                "/registration.html",  // Add registration page
                "/dashboard.html",     // Allow access to dashboard
                "/jobs.html",          // Add jobs page
                "/applications.html",  // Add applications page
                "/test.html",          // Allow access to test page
                "/css/**",
                "/js/**",
                "/images/**",
                "/favicon.ico",     // Allow access to favicon
                "/actuator/**",
                "/api/v1/auth/**",
                "/api/v1/profile-simple/**",  // Allow access to simplified profile API
                "/api/v1/jobs/**",            // Allow access to jobs API
                "/api/v1/applications/**",    // Allow access to applications API
                "/api/v1/upload/**",          // Allow access to file upload API
                "/api/v1/ats/**",             // Allow access to ATS analysis API
                "/uploads/**",                // Allow access to uploaded files
                "/h2-console/**",             // Allow access to H2 console
                "/error"  // Allow access to error page
            ).permitAll()
            .anyRequest().authenticated()
            .and()
            .headers()
            .frameOptions().sameOrigin()  // Allow iframe for same origin
            .contentSecurityPolicy("default-src 'self'; script-src 'self' 'unsafe-inline' https://fonts.googleapis.com https://cdn.jsdelivr.net; style-src 'self' 'unsafe-inline' https://fonts.googleapis.com; font-src 'self' https://fonts.gstatic.com; img-src 'self' data:; connect-src 'self' https://nyc.cloud.appwrite.io https://*.appwrite.io")
            .and()
            .httpStrictTransportSecurity(hstsConfig -> 
                hstsConfig
                    .maxAgeInSeconds(31536000)
                    .includeSubDomains(true));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Get allowed origins from environment variable or use default for development
        String allowedOrigins = System.getenv("CORS_ALLOWED_ORIGINS");
        if (allowedOrigins != null && !allowedOrigins.isEmpty()) {
            configuration.setAllowedOrigins(Arrays.asList(allowedOrigins.split(",")));
        } else {
            // Development defaults
            configuration.setAllowedOrigins(Arrays.asList(
                "http://192.168.1.11:8080",
                "http://192.168.1.11:8081",
                "http://localhost:8080",
                "http://localhost:8081",
                "http://127.0.0.1:8080",
                "http://127.0.0.1:8081"
            ));
        }
        
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList(
            "Authorization",
            "Cache-Control",
            "Content-Type",
            "Origin",
            "X-Requested-With",
            "Accept",
            "X-Appwrite-User-Id",      // Add Appwrite headers
            "X-Appwrite-User-Email"    // Add Appwrite headers
        ));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L); // Cache preflight response for 1 hour
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
