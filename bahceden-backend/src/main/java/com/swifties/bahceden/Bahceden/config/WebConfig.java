package com.swifties.bahceden.Bahceden.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String absolutePath = Paths.get("src/main/resources/media/").toFile().getAbsolutePath();
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + absolutePath + "/");
    }
}
