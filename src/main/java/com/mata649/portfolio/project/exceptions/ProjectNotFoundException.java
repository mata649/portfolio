package com.mata649.portfolio.project.exceptions;

import com.mata649.portfolio.shared.exceptions.EntityNotFoundException;

import java.util.UUID;

public class ProjectNotFoundException extends EntityNotFoundException {
    public ProjectNotFoundException(UUID id) {
        super(String.format("The project with the id %s was not found", id));
    }
}
