package com.example.sas.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
                String userDir = System.getProperty("user.dir");

                // Serve uploaded receipt images from filesystem
                registry.addResourceHandler("/uploads/receipts/**")
                                .addResourceLocations("file:" + userDir + "/uploads/receipts/");

                // Serve uploaded ticket evidence
                registry.addResourceHandler("/uploads/tickets/**")
                                .addResourceLocations("file:" + userDir + "/uploads/tickets/");

                // Serve uploaded staff ID photos
                registry.addResourceHandler("/uploads/id-photos/**")
                                .addResourceLocations("file:" + userDir + "/uploads/id-photos/");

                // NEW: Serve uploaded customer profile pictures
                registry.addResourceHandler("/uploads/profile-pictures/**")
                                .addResourceLocations("file:" + userDir + "/uploads/profile-pictures/");

                // Serve uploaded service images
                registry.addResourceHandler("/uploads/service-images/**")
                                .addResourceLocations("file:" + userDir + "/uploads/service-images/");
        }
}
