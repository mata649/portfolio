package com.mata649.portfolio.experience.exceptions;

import com.mata649.portfolio.shared.exceptions.EntityNotFoundException;

import java.util.UUID;

public class ExperienceNotFoundException extends EntityNotFoundException {

    public ExperienceNotFoundException(UUID id) {
        super(String.format("The experience with the id %s was not found", id));
    }
}
