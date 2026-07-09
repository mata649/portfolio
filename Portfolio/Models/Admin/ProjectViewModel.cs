namespace Portfolio.Web.Models.Admin;

using Entities;

public record ProjectViewModel(Guid Id, string Name, string Description, string? GitHubLink, IEnumerable<SkillViewModel> Skills)
{
    public static ProjectViewModel FromProject(Project project) => 
        new ProjectViewModel(
            project.Id, 
            project.Name, 
            project.Description, 
            project.GitHubLink, 
            project.Skills.Select(SkillViewModel.FromSkill)
        );
}
