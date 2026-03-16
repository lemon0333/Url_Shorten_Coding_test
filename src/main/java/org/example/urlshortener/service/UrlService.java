package org.example.urlshortener.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.urlshortener.domain.Url;
import org.example.urlshortener.dto.UrlResponse;
import org.example.urlshortener.repository.UrlRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UrlService {
    private final UrlRepository urlRepository;
    private final StringRedisTemplate redisTemplate;

    // 1. Base62 변환 로직
    private static final String BASE62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public String encode(Long id) {
        StringBuilder sb = new StringBuilder();
        while (id > 0) {
            sb.append(BASE62.charAt((int) (id % 62)));
            id /= 62;
        }
        return sb.reverse().toString();
    }

    // 2. URL 단축 및 저장
    @Transactional
    public UrlResponse shortenUrl(UrlRequest request) {
        // 커스텀 코드 처리 로직 (생략 가능)
        Url url = urlRepository.save(Url.builder()
                .originalUrl(request.getOriginalUrl())
                .expiresAt(request.getExpiresAt())
                .build());

        String shortCode = (request.getCustomCode() != null) ?
                request.getCustomCode() : encode(url.getId());

        url.updateShortCode(shortCode);

        // Redis에 캐싱 (Cache-aside) 및 TTL 설정
        redisTemplate.opsForValue().set("url:" + shortCode, url.getOriginalUrl());
        return UrlResponse.from(url);
    }

    // 3. 리다이렉트 시 카운팅 (Redis INCR)
    public String getOriginalUrl(String shortCode) {
        // 1. Redis 조회
        String cachedUrl = redisTemplate.opsForValue().get("url:" + shortCode);
        if (cachedUrl != null) {
            // 2. 삭제 여부 확인용 키가 따로 없다면 DB 체크 필요 (또는 캐시 삭제 전략)
            redisTemplate.opsForValue().increment("url_clicks:" + shortCode);
            return cachedUrl;
        }

        // 3. DB Fallback (Optional로 체크)
        Url url = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new EntityNotFoundException("404 Not Found"));

        // 4. 요구사항: 소프트 딜리트/만료 여부 확인 -> 410 응답
        if (url.isDeleted() || (url.getExpiresAt() != null && url.getExpiresAt().isBefore(LocalDateTime.now()))) {
            throw new ResponseStatusException(HttpStatus.GONE, "URL has expired or been deleted");
        }

        return url.getOriginalUrl();
    }

        // DB fallback
        Url url = urlRepository.findByShortCodeAndIsDeletedFalse(shortCode)
                .orElseThrow(() -> new EntityNotFoundException("404 Not Found"));

        // 만료 체크 후 Redis 동기화 및 리턴
        return url.getOriginalUrl();
    }
}