package com.mata649.portfolio.skill.dtos;

import com.mata649.portfolio.skill.model.Skill;

import java.util.UUID;

public record SkillReponse(
        UUID id,
        String name
) {

    public static SkillReponse from(Skill skill) {
        return new SkillReponse(skill.getId(), skill.getName());
    }
}
