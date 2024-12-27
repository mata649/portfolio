package com.mata649.portfolio.skill.exceptions;

import com.mata649.portfolio.shared.exceptions.EntityNotFoundException;

import java.util.UUID;

public class SkillNotFoundException extends EntityNotFoundException {
    public SkillNotFoundException(UUID id) {
        super(String.format("The skill with the id %s was not found", id));
    }
}
