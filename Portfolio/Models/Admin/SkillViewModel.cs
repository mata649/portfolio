namespace Portfolio.Web.Models.Admin;

using Entities;

public record SkillViewModel(Guid Id, string Name)
{
    public static SkillViewModel FromSkill(Skill skill) => new SkillViewModel(skill.Id, skill.Name);
}