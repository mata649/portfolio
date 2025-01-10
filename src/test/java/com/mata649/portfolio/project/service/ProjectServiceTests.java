package com.mata649.portfolio.project.service;

import com.mata649.portfolio.project.dtos.ProjectResponse;
import com.mata649.portfolio.project.dtos.SaveProjectRequest;
import com.mata649.portfolio.project.exceptions.ProjectNotFoundException;
import com.mata649.portfolio.project.model.Project;
import com.mata649.portfolio.project.repository.ProjectRepository;
import com.mata649.portfolio.skill.model.Skill;
import com.mata649.portfolio.skill.repository.SkillRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTests {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private SkillRepository skillRepository;

    @InjectMocks
    private ProjectService projectService;

    @Test
    public void create_shouldReturnProjectResponse_whenSaveProjectRequestIsValid() {
        SaveProjectRequest request = new SaveProjectRequest(
                "Project Name",
                "Project Description",
                "https://github.com/example",
                List.of(UUID.randomUUID())
        );

        List<Skill> skills = List.of(Skill.builder().id(UUID.randomUUID()).name("Java").build());
        Project project = Project.builder()
                .id(UUID.randomUUID())
                .name(request.getName())
                .description(request.getDescription())
                .githubLink(request.getGithubLink())
                .skills(skills)
                .build();

        when(skillRepository.findAllById(request.getSkills())).thenReturn(skills);
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        ProjectResponse response = projectService.create(request);

        assertEquals(project.getId(), response.getId());
        assertEquals(project.getName(), response.getName());
        assertEquals(project.getDescription(), response.getDescription());
        assertEquals(project.getGithubLink(), response.getGithubLink());
    }

    @Test
    public void findById_shouldReturnProjectResponse_whenProjectExists() {
        UUID id = UUID.randomUUID();
        Project project = Project.builder()
                .id(id)
                .name("Existing Project")
                .description("Description")
                .githubLink("https://github.com/example")
                .skills(List.of())
                .build();

        when(projectRepository.findById(id)).thenReturn(Optional.of(project));

        ProjectResponse response = projectService.findById(id);

        assertEquals(project.getId(), response.getId());
        assertEquals(project.getName(), response.getName());
        assertEquals(project.getDescription(), response.getDescription());
    }

    @Test
    public void findById_shouldThrowProjectNotFoundException_whenProjectDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(projectRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ProjectNotFoundException.class, () -> projectService.findById(id));
    }

    @Test
    public void findAll_shouldReturnPageOfProjectResponses_whenProjectsExist() {

        Pageable pageable = Pageable.ofSize(10);
        Project project = Project.builder()
                .id(UUID.randomUUID())
                .name("Project")
                .description("Description")
                .githubLink("https://github.com/example")
                .skills(List.of())
                .build();

        Page<Project> projectsPage = new PageImpl<>(List.of(project));
        when(projectRepository.findAll(pageable)).thenReturn(projectsPage);

        // Act
        Page<ProjectResponse> responses = projectService.findAll(pageable);

        // Assert
        assertEquals(1, responses.getTotalElements());
        ProjectResponse response = responses.getContent().get(0);
        assertEquals(project.getId(), response.getId());
        assertEquals(project.getName(), response.getName());
        assertEquals(project.getDescription(), response.getDescription());
    }

    @Test
    public void delete_shouldReturnDeletedProjectResponse_whenProjectExists() {
        UUID id = UUID.randomUUID();
        Project project = Project.builder()
                .id(id)
                .name("To Be Deleted")
                .description("Description")
                .githubLink("https://github.com/example")
                .skills(List.of())
                .build();

        when(projectRepository.findById(id)).thenReturn(Optional.of(project));

        ProjectResponse response = projectService.delete(id);

        assertEquals(project.getId(), response.getId());
        assertEquals(project.getName(), response.getName());
        verify(projectRepository).delete(project);
    }

    @Test
    public void delete_shouldThrowProjectNotFoundException_whenProjectDoesNotExist() {

        UUID id = UUID.randomUUID();
        when(projectRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ProjectNotFoundException.class, () -> projectService.delete(id));
    }

    @Test
    public void update_shouldReturnUpdatedProjectResponse_whenProjectExists() {
        UUID id = UUID.randomUUID();
        SaveProjectRequest request = new SaveProjectRequest(
                "Updated Project",
                "Updated Description",
                "https://github.com/updated",
                List.of(UUID.randomUUID())
        );

        Project project = Project.builder()
                .id(id)
                .name("Old Project")
                .description("Old Description")
                .githubLink("https://github.com/old")
                .skills(List.of())
                .build();

        List<Skill> skills = List.of(Skill.builder().id(UUID.randomUUID()).name("Python").build());

        when(projectRepository.findById(id)).thenReturn(Optional.of(project));
        when(skillRepository.findAllById(request.getSkills())).thenReturn(skills);
        when(projectRepository.save(any(Project.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProjectResponse response = projectService.update(id, request);

        assertEquals(project.getId(), response.getId());
        assertEquals(request.getName(), response.getName());
        assertEquals(request.getDescription(), response.getDescription());
        assertEquals(request.getGithubLink(), response.getGithubLink());
    }

    @Test
    public void update_shouldThrowProjectNotFoundException_whenProjectDoesNotExist() {
        UUID id = UUID.randomUUID();
        SaveProjectRequest request = new SaveProjectRequest("Name", "Description", "https://github.com/example", List.of());

        when(projectRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ProjectNotFoundException.class, () -> projectService.update(id, request));
    }
}

