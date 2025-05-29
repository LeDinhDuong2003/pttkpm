// database_service/src/main/java/com/example/database_service/config/JacksonConfig.java
package com.example.database_service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class JacksonConfig {

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        // Đăng ký JavaTimeModule để xử lý LocalDateTime
        objectMapper.registerModule(new JavaTimeModule());

        // QUAN TRỌNG: Tắt timestamps để @JsonFormat hoạt động
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // Debug log
        System.out.println("=== Database Service Jackson Configuration ===");
        System.out.println("WRITE_DATES_AS_TIMESTAMPS disabled: " +
                !objectMapper.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS));

        return objectMapper;
    }
}