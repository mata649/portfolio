package com.mata649.portfolio.skill.service;

import com.mata649.portfolio.skill.dtos.SaveSkillRequest;
import com.mata649.portfolio.skill.dtos.SkillResponse;
import com.mata649.portfolio.skill.exceptions.SkillNotFoundException;
import com.mata649.portfolio.skill.model.Skill;
import com.mata649.portfolio.skill.repository.SkillRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SkillServiceTests {

    @Mock
    private SkillRepository skillRepository;

    @InjectMocks
    private SkillService skillService;

    @Test
    public void create_shouldReturnSkillResponse_whenSaveSkillRequestIsValid() {

        SaveSkillRequest request = new SaveSkillRequest("Java");
        Skill skill = new Skill();
        skill.setId(UUID.randomUUID());
        skill.setName(request.getName());

        when(skillRepository.save(any(Skill.class))).thenReturn(skill);

        SkillResponse response = skillService.create(request);

        assertEquals(skill.getId(), response.getId());
        assertEquals(skill.getName(), response.getName());
    }

    @Test
    public void findById_shouldReturnSkillResponse_whenSkillExists() {

        UUID id = UUID.randomUUID();
        Skill skill = new Skill();
        skill.setId(id);
        skill.setName("Python");

        when(skillRepository.findById(id)).thenReturn(Optional.of(skill));

        SkillResponse response = skillService.findById(id);

        assertEquals(skill.getId(), response.getId());
        assertEquals(skill.getName(), response.getName());
    }

    @Test
    public void findById_shouldThrowSkillNotFoundException_whenSkillDoesNotExist() {

        UUID id = UUID.randomUUID();
        when(skillRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(SkillNotFoundException.class, () -> skillService.findById(id));
    }

    @Test
    public void findAll_shouldReturnListOfSkillResponses_whenSkillsExist() {

        List<Skill> skills = List.of(
                Skill.builder().id(UUID.randomUUID()).name("Java").build(),
                Skill.builder().id(UUID.randomUUID()).name("Python").build()
        );

        when(skillRepository.findAll()).thenReturn(skills);

        // Act
        List<SkillResponse> responses = skillService.findAll();

        // Assert
        assertEquals(skills.size(), responses.size());
        for (int i = 0; i < skills.size(); i++) {
            assertEquals(skills.get(i).getId(), responses.get(i).getId());
            assertEquals(skills.get(i).getName(), responses.get(i).getName());
        }
    }

    @Test
    public void update_shouldReturnUpdatedSkillResponse_whenSkillExists() {

        UUID id = UUID.randomUUID();
        SaveSkillRequest request = new SaveSkillRequest("Updated Skill");
        Skill skill = new Skill();
        skill.setId(id);
        skill.setName("Old Skill");

        when(skillRepository.findById(id)).thenReturn(Optional.of(skill));
        when(skillRepository.save(any(Skill.class))).thenReturn(skill);


        SkillResponse response = skillService.update(request, id);

        assertEquals(skill.getId(), response.getId());
        assertEquals(request.getName(), response.getName());
    }

    @Test
    public void update_shouldThrowSkillNotFoundException_whenSkillDoesNotExist() {

        UUID id = UUID.randomUUID();
        SaveSkillRequest request = new SaveSkillRequest("Nonexistent Skill");

        when(skillRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(SkillNotFoundException.class, () -> skillService.update(request, id));
    }

    @Test
    public void delete_shouldReturnDeletedSkillResponse_whenSkillExists() {

        UUID id = UUID.randomUUID();
        Skill skill = new Skill();
        skill.setId(id);
        skill.setName("To Be Deleted");

        when(skillRepository.findById(id)).thenReturn(Optional.of(skill));

        SkillResponse response = skillService.delete(id);

        assertEquals(skill.getId(), response.getId());
        assertEquals(skill.getName(), response.getName());
        verify(skillRepository).delete(skill);
    }

    @Test
    public void delete_shouldThrowSkillNotFoundException_whenSkillDoesNotExist() {

        UUID id = UUID.randomUUID();

        when(skillRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(SkillNotFoundException.class, () -> skillService.delete(id));
    }
}
