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
        if (request.endTime() == null && !request.currentJob()) {
            throw new BadRequestException("endTime", "The endTime can't be null if the currentJob is false");
        } else if (request.endTime() != null && request.currentJob()) {

            throw new BadRequestException("currentJob", "The currentJob can't be true if endTime has been set");
        }

        Experience experience = Experience.builder().
                id(UUID.randomUUID())
                .position(request.position())
                .company(request.company())
                .description(request.description())
                .location(request.location())
                .startTime(request.startTime())
                .endTime(request.endTime())
                .currentJob(request.currentJob())
                .build();

        List<Skill> skills = skillRepository.findAllById(request.skills());
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
        if (request.endTime() == null && !request.currentJob()) {
            throw new BadRequestException("endTime", "The endTime can't be null if the currentJob is false");
        } else if (request.endTime() != null && request.currentJob()) {

            throw new BadRequestException("currentJob", "The currentJob can't be true if endTime has been set");
        }
        Experience experience = experienceRepository.findById(id).orElseThrow(() -> new ExperienceNotFoundException(id));
        experience.setPosition(request.position());
        experience.setCompany(request.company());
        experience.setDescription(request.description());
        experience.setLocation(request.location());
        experience.setStartTime(experience.getStartTime());
        experience.setEndTime(experience.getEndTime());
        experience.setCurrentJob(experience.getCurrentJob());

        List<Skill> skills = skillRepository.findAllById(request.skills());
        experience.setSkills(skills);
        Experience experienceSaved = experienceRepository.save(experience);
        return ExperienceResponse.from(experienceSaved);

    }
}
