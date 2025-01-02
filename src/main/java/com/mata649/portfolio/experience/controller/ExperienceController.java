package com.mata649.portfolio.experience.controller;

import com.mata649.portfolio.experience.dtos.ExperienceResponse;
import com.mata649.portfolio.experience.dtos.SaveExperienceRequest;
import com.mata649.portfolio.experience.service.ExperienceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/experiences")
public class ExperienceController {
    private final ExperienceService experienceService;

    public ExperienceController(ExperienceService experienceService) {
        this.experienceService = experienceService;
    }

    @PostMapping()
    public ResponseEntity<ExperienceResponse> create(@Valid @RequestBody SaveExperienceRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(experienceService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExperienceResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(experienceService.findById(id));
    }

    @GetMapping("/")
    public ResponseEntity<List<ExperienceResponse>> findAll() {
        return ResponseEntity.ok(experienceService.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ExperienceResponse> delete(@PathVariable UUID id) {
        return ResponseEntity.ok(experienceService.delete(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExperienceResponse> update(@PathVariable UUID id, @Valid @RequestBody SaveExperienceRequest request) {
        return ResponseEntity.ok(experienceService.update(id, request));
    }

}
