package org.example.urlshortener.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.urlshortener.dto.ShortenRequest;
import org.example.urlshortener.dto.UrlResponse;
import org.example.urlshortener.dto.UrlStatsResponse;
import org.example.urlshortener.service.UrlService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/urls")
@RequiredArgsConstructor
public class UrlController {
    private final UrlService urlService;

    @PostMapping
    public ResponseEntity<UrlResponse> shorten(@Valid @RequestBody ShortenRequest request) {
        return ResponseEntity.ok(urlService.shortenUrl(request));
    }

    @GetMapping("/{shortCode}/stats")
    public ResponseEntity<UrlStatsResponse> getStats(@PathVariable String shortCode) {
        return ResponseEntity.ok(urlService.getStats(shortCode));
    }

    @DeleteMapping("/{shortCode}")
    public ResponseEntity<Void> delete(@PathVariable String shortCode) {
        urlService.deleteUrl(shortCode);
        return ResponseEntity.noContent().build();
    }
}