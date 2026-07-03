namespace Portfolio.Web.Models.Admin;

public class AdminViewModel()
{
    public IEnumerable<SkillViewModel> Skills { get; init; } = [];
}