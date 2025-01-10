package com.mata649.portfolio.experience.dtos;

import com.mata649.portfolio.experience.model.Experience;
import com.mata649.portfolio.skill.dtos.SkillResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExperienceResponse {
    private UUID id;
    private String position;
    private String company;
    private String location;
    private String description;
    private LocalDate startTime;
    private LocalDate endTime;
    private Boolean currentJob;
    private List<SkillResponse> skills;

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
                experience.getSkills().stream().map(SkillResponse::from).toList());
    }
}
