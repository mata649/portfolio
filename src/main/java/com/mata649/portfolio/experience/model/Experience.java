package com.mata649.portfolio.experience.model;

import com.mata649.portfolio.skill.model.Skill;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "experiences")
public class Experience {
    @Id
    private UUID id;

    @Column(name = "position", length = 255, nullable = false)
    private String position;

    @Column(name = "company", length = 255, nullable = false)
    private String company;

    @Column(name = "location", length = 255, nullable = false)
    private String location;

    @Column(name = "description", length = 2000, nullable = false)
    private String description;

    @Column(name = "start_time", nullable = false)
    private Date startTime;

    @Column(name = "end_time", nullable = false)
    private Date endTime;

    @Column(name = "current_job", nullable = false)
    private Boolean currentJob;

    @ManyToMany
    @JoinTable(
            name = "experiences_skills",
            joinColumns = @JoinColumn(name = "experience_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private List<Skill> skills;

}
