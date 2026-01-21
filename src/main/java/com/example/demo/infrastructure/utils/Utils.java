package com.example.demo.infrastructure.utils;


import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class Utils {

    private String cachedPrompt;

    // Метод выполнится один раз при старте приложения
    @PostConstruct
    public void init() throws IOException {
        loadPrompt();
    }

    private void loadPrompt() throws IOException {
        ClassPathResource resource = new ClassPathResource("prompts/default.system.prompt.txt");
        // Читаем поток байтов и превращаем в строку (безопасно для JAR)
        this.cachedPrompt = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
    }

    public String getPrompt() {
        // Возвращаем уже загруженную строку
        return cachedPrompt;
    }
}