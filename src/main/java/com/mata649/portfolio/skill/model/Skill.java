package com.mata649.portfolio.skill.model;

import com.mata649.portfolio.project.model.Project;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "skills")
public class Skill {
    @Id
    private UUID id;

    @Column(name = "name", length = 30, nullable = false)
    private String name;

    @ManyToMany(mappedBy = "skills")
    private List<Project> projects;

}
