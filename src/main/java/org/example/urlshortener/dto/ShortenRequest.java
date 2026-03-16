package org.example.urlshortener.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;

public record ShortenRequest(
        @NotBlank @URL String originalUrl, // 필수값 및 URL 형식 검증
        String customCode,
        LocalDateTime expiresAt
) {}
