package com.placement.expo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/");
        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/static/js/");
        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/static/images/");
        registry.addResourceHandler("/*.html")
                .addResourceLocations("classpath:/static/");
    }
}

@Configuration
@Profile("dev")
class DevWebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // In development, serve static files directly from src folder for live reloading
        registry.addResourceHandler("/static/**")
                .addResourceLocations("file:src/main/resources/static/", "classpath:/static/")
                .setCachePeriod(0);
        registry.addResourceHandler("/css/**")
                .addResourceLocations("file:src/main/resources/static/css/", "classpath:/static/css/")
                .setCachePeriod(0);
        registry.addResourceHandler("/js/**")
                .addResourceLocations("file:src/main/resources/static/js/", "classpath:/static/js/")
                .setCachePeriod(0);
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:src/main/resources/static/images/", "classpath:/static/images/")
                .setCachePeriod(0);
        registry.addResourceHandler("/*.html")
                .addResourceLocations("file:src/main/resources/static/", "classpath:/static/")
                .setCachePeriod(0);
    }
}
