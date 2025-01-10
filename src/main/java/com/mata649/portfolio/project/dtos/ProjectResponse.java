package com.mata649.portfolio.project.dtos;

import com.mata649.portfolio.project.model.Project;
import com.mata649.portfolio.skill.dtos.SkillResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectResponse {

    private UUID id;
    private String name;
    private String description;
    private String githubLink;
    private List<SkillResponse> skills;

    public static ProjectResponse from(Project project) {
        return new ProjectResponse(project.getId(),
                project.getName(),
                project.getDescription(),
                project.getGithubLink(), project
                .getSkills().stream().map(SkillResponse::from).toList());
    }
}
