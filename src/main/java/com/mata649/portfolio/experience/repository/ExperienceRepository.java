package com.mata649.portfolio.experience.repository;

import com.mata649.portfolio.experience.model.Experience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ExperienceRepository extends JpaRepository<Experience, UUID> {
}
