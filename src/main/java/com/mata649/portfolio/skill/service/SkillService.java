package com.mata649.portfolio.skill.service;

import com.mata649.portfolio.skill.dtos.SaveSkillRequest;
import com.mata649.portfolio.skill.dtos.SkillResponse;
import com.mata649.portfolio.skill.exceptions.SkillNotFoundException;
import com.mata649.portfolio.skill.model.Skill;
import com.mata649.portfolio.skill.repository.SkillRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SkillService {
    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public SkillResponse create(SaveSkillRequest request) {
        Skill skill = new Skill();
        skill.setId(UUID.randomUUID());
        skill.setName(request.getName());
        Skill skillSaved = skillRepository.save(skill);
        return SkillResponse.from(skillSaved);
    }


    public SkillResponse findById(UUID id) {
        Skill skill = skillRepository.findById(id).orElseThrow(() -> new SkillNotFoundException(id));
        return SkillResponse.from(skill);
    }

    public List<SkillResponse> findAll() {
        return skillRepository
                .findAll()
                .stream()
                .map(SkillResponse::from)
                .toList();
    }

    public SkillResponse update(SaveSkillRequest request, UUID id) {
        Skill skill = skillRepository.findById(id).orElseThrow(() -> new SkillNotFoundException(id));
        skill.setName(request.getName());
        Skill skillUpdated = skillRepository.save(skill);
        return SkillResponse.from(skillUpdated);
    }

    public SkillResponse delete(UUID id) {
        Skill skill = skillRepository.findById(id).orElseThrow(() -> new SkillNotFoundException(id));
        skillRepository.delete(skill);
        return SkillResponse.from(skill);
    }
}
