package com.mata649.portfolio.skill.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "skills")
public class Skill {
    @Id
    private UUID id;

    @Column(name = "name", length = 30, nullable = false)
    private String name;
}
