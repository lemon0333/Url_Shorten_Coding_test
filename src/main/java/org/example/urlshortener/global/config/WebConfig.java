package org.example.urlshortener.global.config;

import lombok.RequiredArgsConstructor;
import org.example.urlshortener.global.interceptor.RateLimitInterceptor;
import org.example.urlshortener.global.interceptor.UrlAccessInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final UrlAccessInterceptor urlAccessInterceptor;
    private final RateLimitInterceptor rateLimitInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(urlAccessInterceptor)
                .addPathPatterns("/*")            // /{shortCode} 형태의 리다이렉트 경로
                .excludePathPatterns("/api/**"); // 실제 API 생성 경로는 제외 (필요시)

        registry.addInterceptor(rateLimitInterceptor)
                .addPathPatterns("/api/**");      // 생성 API에만 과도한 호출 방지 적용
    }
}