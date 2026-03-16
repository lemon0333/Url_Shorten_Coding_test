package org.example.urlshortener.dto;

import java.time.LocalDateTime;

// domain/url/dto/UrlStatsResponse.java
public record UrlStatsResponse(
        String shortCode,
        long clickCount,
        LocalDateTime createdAt,
        LocalDateTime lastAccessedAt // 필드 추가 권장
) {}