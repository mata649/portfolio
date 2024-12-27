package com.mata649.portfolio.skill.controller;

import com.mata649.portfolio.skill.dtos.SaveSkillRequest;
import com.mata649.portfolio.skill.dtos.SkillReponse;
import com.mata649.portfolio.skill.service.SkillService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/skills")
public class SkillController {
    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @PostMapping
    public ResponseEntity<SkillReponse> create(@Valid @RequestBody SaveSkillRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(skillService.create(request));
    }


    @GetMapping("")
    public ResponseEntity<List<SkillReponse>> findAll() {
        return ResponseEntity.ok(skillService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SkillReponse> findById(@PathVariable UUID id) {
        return ResponseEntity
                .ok(skillService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SkillReponse> update(@PathVariable UUID id, @Valid @RequestBody SaveSkillRequest request) {
        return ResponseEntity.ok(skillService.update(request, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SkillReponse> delete(@PathVariable UUID id){
        return ResponseEntity.ok(skillService.delete(id));
    }
}
