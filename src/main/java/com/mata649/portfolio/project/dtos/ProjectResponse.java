package com.mata649.portfolio.project.dtos;

import com.mata649.portfolio.project.model.Project;
import com.mata649.portfolio.skill.dtos.SkillReponse;

import java.util.List;
import java.util.UUID;

public record ProjectResponse(
        UUID id,
        String name,
        String description,
        String githubLink,
        List<SkillReponse> skills
) {


    public static ProjectResponse from(Project project) {
        return new ProjectResponse(project.getId(),
                project.getName(),
                project.getDescription(),
                project.getGithubLink(), project
                .getSkills().stream().map(SkillReponse::from).toList());
    }
}
