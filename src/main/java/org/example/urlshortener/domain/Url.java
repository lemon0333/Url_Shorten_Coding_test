package org.example.urlshortener.domain;


import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
@Getter
@Entity

@SQLDelete(sql = "UPDATE urls SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
public class Url {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String shortCode;

    private String originalUrl;
    private Long clickCount = 0L;
    private LocalDateTime expiresAt;
    private boolean isDeleted = false;
    // 40번 줄의 updateShortCode가 필요합니다.
    public void updateShortCode(String shortCode) {
        this.shortCode = shortCode;
    }
    // ... audit fields (createdAt, updatedAt)
}