package com.darshan.url_shorten_v5.Configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(
                        "http://localhost:8083"
//                        "https:"myDomain.com"
                )
                .allowedMethods("GET", "POST")
                .allowedHeaders("Content-Type")
                .exposedHeaders("*")
                .maxAge(3600);
    }
}
