package com.hari134.coderun.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class GlobalCorsConfiguration implements WebMvcConfigurer {

    private final Logger logger = LoggerFactory.getLogger(GlobalCorsConfiguration.class);

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        logger.info("Applying CORS configuration for all paths");

        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(false)
                .maxAge(3600);

        logger.info("CORS configuration applied successfully");
    }
}
