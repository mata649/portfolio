package com.mata649.portfolio.health.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/health")
public class HealthCheckController {
    public ResponseEntity<Map<String, String>> healthCheck() {
        return ResponseEntity.ok(
                Map.of("message", "service is up")
        );
    }
}
