package com.mata649.portfolio.project.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

public record SaveProjectRequest(
        @NotBlank
        @Size(max = 255)
        String name,
        @NotBlank
        @Size(max = 1000)
        String description,
        @NotBlank
        @Size(max = 100)
        @Pattern(regexp = "^https:\\/\\/github\\.com(?:\\/[^\\s\\/]+){2}$")
        String githubLink,
        @NotEmpty
        List<UUID> skills
) {
}
