package com.mata649.portfolio.experience.service;

import com.mata649.portfolio.experience.dtos.ExperienceResponse;
import com.mata649.portfolio.experience.dtos.SaveExperienceRequest;
import com.mata649.portfolio.experience.exceptions.ExperienceNotFoundException;
import com.mata649.portfolio.experience.model.Experience;
import com.mata649.portfolio.experience.repository.ExperienceRepository;
import com.mata649.portfolio.shared.exceptions.BadRequestException;
import com.mata649.portfolio.skill.model.Skill;
import com.mata649.portfolio.skill.repository.SkillRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExperienceServiceTests {

    @Mock
    private ExperienceRepository experienceRepository;

    @Mock
    private SkillRepository skillRepository;

    @InjectMocks
    private ExperienceService experienceService;

    @Test
    public void create_shouldReturnExperienceResponse_whenRequestIsValid() {
        SaveExperienceRequest request = new SaveExperienceRequest(
                "Software Engineer",
                "Tech Company",
                "Developing cool stuff",
                "Remote",
                LocalDate.of(2020, 1, 1),
                LocalDate.of(2022, 1, 1),
                false,
                List.of(UUID.randomUUID())
        );

        List<Skill> skills = List.of(Skill.builder().id(UUID.randomUUID()).name("Java").build());
        Experience experience = Experience.builder()
                .id(UUID.randomUUID())
                .position(request.position())
                .company(request.company())
                .description(request.description())
                .location(request.location())
                .startTime(request.startTime())
                .endTime(request.endTime())
                .currentJob(request.currentJob())
                .skills(skills)
                .build();

        when(skillRepository.findAllById(request.skills())).thenReturn(skills);
        when(experienceRepository.save(any(Experience.class))).thenReturn(experience);

        ExperienceResponse response = experienceService.create(request);

        assertNotNull(response);
        assertEquals(experience.getId(), response.id());
        assertEquals(experience.getPosition(), response.position());
        verify(experienceRepository).save(any(Experience.class));
    }

    @Test
    public void create_shouldThrowBadRequestException_whenEndTimeIsNullAndCurrentJobIsFalse() {
        SaveExperienceRequest request = new SaveExperienceRequest(
                "Software Engineer",
                "Tech Company",
                "Developing cool stuff",
                "Remote",
                LocalDate.of(2020, 1, 1),
                null,
                false,
                List.of(UUID.randomUUID())
        );

        assertThrows(BadRequestException.class, () -> experienceService.create(request));
    }

    @Test
    public void findById_shouldReturnExperienceResponse_whenExperienceExists() {
        UUID id = UUID.randomUUID();
        Experience experience = Experience.builder()
                .id(id)
                .position("Software Engineer")
                .company("Tech Company")
                .skills(List.of())
                .build();

        when(experienceRepository.findById(id)).thenReturn(Optional.of(experience));

        ExperienceResponse response = experienceService.findById(id);

        assertNotNull(response);
        assertEquals(experience.getId(), response.id());
        verify(experienceRepository).findById(id);
    }

    @Test
    public void findById_shouldThrowExperienceNotFoundException_whenExperienceDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(experienceRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ExperienceNotFoundException.class, () -> experienceService.findById(id));
    }

    @Test
    public void delete_shouldReturnExperienceResponse_whenExperienceExists() {
        UUID id = UUID.randomUUID();
        Experience experience = Experience.builder()
                .id(id)
                .position("Software Engineer")
                .skills(List.of())
                .build();

        when(experienceRepository.findById(id)).thenReturn(Optional.of(experience));

        ExperienceResponse response = experienceService.delete(id);

        assertNotNull(response);
        assertEquals(experience.getId(), response.id());
        verify(experienceRepository).delete(experience);
    }

    @Test
    public void delete_shouldThrowExperienceNotFoundException_whenExperienceDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(experienceRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ExperienceNotFoundException.class, () -> experienceService.delete(id));
    }

    @Test
    public void update_shouldReturnUpdatedExperienceResponse_whenRequestIsValid() {
        UUID id = UUID.randomUUID();
        SaveExperienceRequest request = new SaveExperienceRequest(
                "Updated Position",
                "Updated Company",
                "Updated Description",
                "Updated Location",
                LocalDate.of(2021, 1, 1),
                LocalDate.of(2023, 1, 1),
                false,
                Arrays.asList(UUID.randomUUID())
        );

        Experience existingExperience = Experience.builder()
                .id(id)
                .position("Old Position")
                .company("Old Company")
                .build();

        List<Skill> skills = List.of(Skill.builder().id(UUID.randomUUID()).name("Java").build());

        when(experienceRepository.findById(id)).thenReturn(Optional.of(existingExperience));
        when(skillRepository.findAllById(request.skills())).thenReturn(skills);
        when(experienceRepository.save(any(Experience.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ExperienceResponse response = experienceService.update(id, request);

        assertNotNull(response);
        assertEquals(request.position(), response.position());
        assertEquals(request.company(), response.company());
        verify(experienceRepository).save(any(Experience.class));
    }

    @Test
    public void update_shouldThrowExperienceNotFoundException_whenExperienceDoesNotExist() {
        UUID id = UUID.randomUUID();
        SaveExperienceRequest request = new SaveExperienceRequest(
                "Position",
                "Company",
                "Description",
                "Location",
                LocalDate.of(2020, 1, 1),
                LocalDate.of(2022, 1, 1),
                false,
                List.of(UUID.randomUUID())
        );

        when(experienceRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ExperienceNotFoundException.class, () -> experienceService.update(id, request));
    }
}
