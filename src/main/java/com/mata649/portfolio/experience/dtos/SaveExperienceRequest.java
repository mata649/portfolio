package com.mata649.portfolio.experience.dtos;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaveExperienceRequest {

    @NotBlank
    @Size(max = 255)
    private String position;

    @NotBlank
    @Size(max = 255)
    private String company;

    @NotBlank
    @Size(max = 255)
    private String location;

    @NotBlank
    @Size(max = 2000)
    private String description;

    @NotNull
    @PastOrPresent
    private LocalDate startTime;

    @NotNull
    @PastOrPresent
    private LocalDate endTime;

    @NotNull
    private Boolean currentJob;

    @NotEmpty
    private Set<UUID> skills;

}
