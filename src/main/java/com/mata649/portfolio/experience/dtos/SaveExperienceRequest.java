package com.mata649.portfolio.experience.dtos;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public record SaveExperienceRequest(
        @NotBlank
        @Size(max = 255)
        String position,
        @NotBlank
        @Size(max = 255)
        String company,
        @NotBlank
        @Size(max = 255)
        String location,
        @NotBlank
        @Size(max = 2000)
        String description,
        @NotNull
        @PastOrPresent
        Date startTime,
        @PastOrPresent
        Date endTime,
        @NotNull
        Boolean currentJob,
        @NotEmpty
        List<UUID> skills
) {
}
