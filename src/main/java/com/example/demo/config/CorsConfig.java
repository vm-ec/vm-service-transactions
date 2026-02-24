package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Global CORS configuration for the demo REST API.
 *
 * - Enables CORS for all endpoints under /api/** so local frontends can call the API.
 * - For production you should restrict allowedOrigins to the exact domains you expect.
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Allow cross-origin requests to the API from any origin for development/testing.
        // Change allowedOrigins to a specific origin (e.g. "http://localhost:3000") for production.
        registry.addMapping("/api/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(false)
                .maxAge(3600);
    }
}

