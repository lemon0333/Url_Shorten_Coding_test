package org.example.urlshortener.repository;

import org.example.urlshortener.domain.Url;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UrlRepository extends JpaRepository<Url, Long> {
    // shortCode로 조회 (is_deleted가 false인 것만 조회는 엔티티의 @Where로 처리됨)
    Optional<Url> findByShortCode(String shortCode);

    // 중복 체크용
    boolean existsByShortCode(String shortCode);
}