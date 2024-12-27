package com.mata649.portfolio.skill.service;

import com.mata649.portfolio.skill.dtos.SaveSkillRequest;
import com.mata649.portfolio.skill.dtos.SkillReponse;
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

    public SkillReponse create(SaveSkillRequest request) {
        Skill skill = new Skill();
        skill.setId(UUID.randomUUID());
        skill.setName(request.name());
        Skill skillSaved = skillRepository.save(skill);
        return SkillReponse.from(skillSaved);
    }


    public SkillReponse findById(UUID id) {
        Skill skill = skillRepository.findById(id).orElseThrow(() -> new SkillNotFoundException(id));
        return SkillReponse.from(skill);
    }

    public List<SkillReponse> findAll() {
        return skillRepository
                .findAll()
                .stream()
                .map(SkillReponse::from)
                .toList();
    }

    public SkillReponse update(SaveSkillRequest request, UUID id) {
        Skill skill = skillRepository.findById(id).orElseThrow(() -> new SkillNotFoundException(id));
        skill.setName(request.name());
        Skill skillUpdated = skillRepository.save(skill);
        return SkillReponse.from(skillUpdated);
    }

    public SkillReponse delete(UUID id) {
        Skill skill = skillRepository.findById(id).orElseThrow(() -> new SkillNotFoundException(id));
        skillRepository.delete(skill);
        return SkillReponse.from(skill);
    }
}
