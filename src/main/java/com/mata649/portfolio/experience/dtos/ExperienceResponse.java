package com.mata649.portfolio.experience.dtos;

import com.mata649.portfolio.experience.model.Experience;
import com.mata649.portfolio.skill.dtos.SkillReponse;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record ExperienceResponse(
        UUID id,
        String position,
        String company,
        String location,
        String description,
        LocalDate startTime,
        LocalDate endTime,
        Boolean currentJob,
        List<SkillReponse> skills
) {

    public static ExperienceResponse from(Experience experience) {
        return new ExperienceResponse(
                experience.getId(),
                experience.getPosition(),
                experience.getCompany(),
                experience.getLocation(),
                experience.getDescription(),
                experience.getStartTime(),
                experience.getEndTime(),
                experience.getCurrentJob(),
                experience.getSkills().stream().map(SkillReponse::from).toList());
    }
}
