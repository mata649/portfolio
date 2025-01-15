package com.mata649.portfolio.project.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaveProjectRequest {

    @NotBlank
    @Size(max = 255)
    private String name;
    @NotBlank
    @Size(max = 1000)
    private String description;
    @NotBlank
    @Size(max = 100)
    @Pattern(regexp = "^https:\\/\\/github\\.com(?:\\/[^\\s\\/]+){2}$")
    private String githubLink;
    @NotEmpty
    private Set<UUID> skills;
}
