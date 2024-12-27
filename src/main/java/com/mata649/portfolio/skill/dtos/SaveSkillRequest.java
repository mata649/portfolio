package com.mata649.portfolio.skill.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SaveSkillRequest(
        @NotBlank
        @Size(max=30)
        String name
) {
}
