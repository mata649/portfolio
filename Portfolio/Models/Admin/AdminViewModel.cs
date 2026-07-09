namespace Portfolio.Web.Models.Admin;

public class AdminViewModel
{
    public IEnumerable<SkillViewModel> Skills { get; init; } = [];
    public IEnumerable<ProjectViewModel> Projects { get; init; } = [];
    public IEnumerable<ExperienceViewModel> Experiences { get; init; } = [];
}