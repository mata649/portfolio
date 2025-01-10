package com.mata649.portfolio.experience.service;

import com.mata649.portfolio.experience.dtos.ExperienceResponse;
import com.mata649.portfolio.experience.dtos.SaveExperienceRequest;
import com.mata649.portfolio.experience.exceptions.ExperienceNotFoundException;
import com.mata649.portfolio.experience.model.Experience;
import com.mata649.portfolio.experience.repository.ExperienceRepository;
import com.mata649.portfolio.shared.exceptions.BadRequestException;
import com.mata649.portfolio.skill.model.Skill;
import com.mata649.portfolio.skill.repository.SkillRepository;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ExperienceService {
    private final ExperienceRepository experienceRepository;
    private final SkillRepository skillRepository;

    public ExperienceService(ExperienceRepository experienceRepository, SkillRepository skillRepository) {
        this.experienceRepository = experienceRepository;
        this.skillRepository = skillRepository;
    }

    public ExperienceResponse create(SaveExperienceRequest request) {
        Experience experience = Experience.builder().
                id(UUID.randomUUID())
                .position(request.getPosition())
                .company(request.getCompany())
                .description(request.getDescription())
                .location(request.getLocation())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .currentJob(request.getCurrentJob())
                .build();

        List<Skill> skills = skillRepository.findAllById(request.getSkills());
        experience.setSkills(skills);
        Experience experienceSaved = experienceRepository.save(experience);
        return ExperienceResponse.from(experienceSaved);
    }

    public ExperienceResponse findById(UUID id) {
        Experience experience = experienceRepository.findById(id).orElseThrow(() -> new ExperienceNotFoundException(id));
        return ExperienceResponse.from(experience);
    }

    public List<ExperienceResponse> findAll() {
        List<Experience> experiences = experienceRepository.findAll();
        return experiences.stream().map(ExperienceResponse::from).toList();
    }

    public ExperienceResponse delete(UUID id) {
        Experience experience = experienceRepository.findById(id).orElseThrow(() -> new ExperienceNotFoundException(id));
        Hibernate.initialize(experience.getSkills());
        experienceRepository.delete(experience);
        return ExperienceResponse.from(experience);
    }

    public ExperienceResponse update(UUID id, SaveExperienceRequest request) {
        Experience experience = experienceRepository.findById(id).orElseThrow(() -> new ExperienceNotFoundException(id));
        experience.setPosition(request.getPosition());
        experience.setCompany(request.getCompany());
        experience.setDescription(request.getDescription());
        experience.setLocation(request.getLocation());
        experience.setStartTime(experience.getStartTime());
        experience.setEndTime(experience.getEndTime());
        experience.setCurrentJob(experience.getCurrentJob());

        List<Skill> skills = skillRepository.findAllById(request.getSkills());
        experience.setSkills(skills);
        Experience experienceSaved = experienceRepository.save(experience);
        return ExperienceResponse.from(experienceSaved);

    }
}
