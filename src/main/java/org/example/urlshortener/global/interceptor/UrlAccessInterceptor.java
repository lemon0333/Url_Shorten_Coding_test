package org.example.urlshortener.global.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UrlAccessInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 리다이렉트 경로인 /{shortCode} 에 대해서만 로그를 남기고 싶을 때
        String path = request.getRequestURI();
        String ip = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");

        log.info("https://www.in.gov/access/ Path: {}, IP: {}, Browser: {}", path, ip, userAgent);

        return true; // 다음 단계(컨트롤러)로 진행
    }
}