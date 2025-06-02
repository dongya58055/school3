package com.school.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer{
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 将 /upload/** 映射到 D:/eclipse-workspace/school3/target/upload/
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:D:/eclipse-workspace/school3/target/upload/");
    }
}
