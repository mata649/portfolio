package com.mata649.portfolio.project.repository;

import com.mata649.portfolio.project.model.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, UUID> {
    @Query("SELECT DISTINCT p FROM Project p " +
            "LEFT JOIN p.skills s " +
            "WHERE (:skillIds IS NULL OR s.id IN :skillIds)")
    Page<Project> findBySkills(Pageable pageable, @Param("skillIds") List<UUID> skillIds);

}
