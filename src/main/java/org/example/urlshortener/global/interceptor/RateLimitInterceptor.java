package org.example.urlshortener.global.interceptor;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RateLimitInterceptor implements HandlerInterceptor {
    private final StringRedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String ip = request.getRemoteAddr();
        String key = "limit:ip:" + ip;

        // 1분당 100회 제한 예시
        Long count = redisTemplate.opsForValue().increment(key);
        if (count == 1) {
            redisTemplate.expire(key, Duration.ofMinutes(1));
        }

        if (count > 100) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            return false; // 컨트롤러 진입 차단
        }
        return true;
    }
}