package com.mata649.portfolio.project.service;

import com.mata649.portfolio.project.dtos.ProjectResponse;
import com.mata649.portfolio.project.dtos.SaveProjectRequest;
import com.mata649.portfolio.project.exceptions.ProjectNotFoundException;
import com.mata649.portfolio.project.model.Project;
import com.mata649.portfolio.project.repository.ProjectRepository;
import com.mata649.portfolio.skill.model.Skill;
import com.mata649.portfolio.skill.repository.SkillRepository;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final SkillRepository skillRepository;

    public ProjectService(ProjectRepository projectRepository, SkillRepository skillRepository) {
        this.projectRepository = projectRepository;
        this.skillRepository = skillRepository;
    }


    public ProjectResponse create(SaveProjectRequest request) {
        Project project = Project.builder()
                .id(UUID.randomUUID())
                .name(request.getName())
                .description(request.getDescription())
                .githubLink(request.getGithubLink())
                .build();

        List<Skill> skills = skillRepository.findAllById(request.getSkills());
        project.setSkills(skills);
        Project projectSaved = projectRepository.save(project);
        return ProjectResponse.from(projectSaved);
    }

    public ProjectResponse findById(UUID id) {
        Project project = projectRepository.findById(id).orElseThrow(() -> new ProjectNotFoundException(id));
        return ProjectResponse.from(project);
    }

    public Page<ProjectResponse> findAll(Pageable pageable) {
        return projectRepository.findAll(pageable).map(ProjectResponse::from);
    }

    public ProjectResponse delete(UUID id) {
        Project project = projectRepository.findById(id).orElseThrow(() -> new ProjectNotFoundException(id));
        Hibernate.initialize(project.getSkills());
        projectRepository.delete(project);
        return ProjectResponse.from(project);
    }

    public ProjectResponse update(UUID id, SaveProjectRequest request) {
        Project project = projectRepository.findById(id).orElseThrow(() -> new ProjectNotFoundException(id));
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setGithubLink(request.getGithubLink());

        List<Skill> skills = skillRepository.findAllById(request.getSkills());

        project.setSkills(skills);

        Project projectUpdated = projectRepository.save(project);
        return ProjectResponse.from(projectUpdated);
    }
}
