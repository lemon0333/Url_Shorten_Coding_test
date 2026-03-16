package org.example.urlshortener.dto;

import org.example.urlshortener.domain.Url;

import java.time.LocalDateTime;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.example.urlshortener.domain.Url;
import java.time.LocalDateTime;

@Builder
public record UrlResponse(
        @JsonProperty("short_code")
        String shortCode,

        @JsonProperty("short_url")
        String shortUrl,

        @JsonProperty("original_url")
        String originalUrl,

        @JsonProperty("expires_at")
        LocalDateTime expiresAt,

        @JsonProperty("created_at")
        LocalDateTime createdAt
) {
    public static UrlResponse from(Url url) {
        // 실제 운영 환경이라면 도메인 주소를 환경변수에서 가져오는 것이 좋지만,
        // 과제용으로는 일단 하드코딩하거나 상수로 처리합니다.
        String domain = "https://short.ly";

        return UrlResponse.builder()
                .shortCode(url.getShortCode())
                .shortUrl(domain + "/" + url.getShortCode())
                .originalUrl(url.getOriginalUrl())
                .expiresAt(url.getExpiresAt())
                 .build();
    }
}