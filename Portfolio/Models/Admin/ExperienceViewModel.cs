namespace Portfolio.Web.Models.Admin;

using System;
using Entities;

public record ExperienceViewModel(
    Guid Id, 
    string Name, 
    string Location, 
    string Company, 
    string Description, 
    DateOnly StartDate, 
    DateOnly EndDate, 
    bool CurrentJob, 
    IEnumerable<SkillViewModel> Skills
)
{
    public static ExperienceViewModel FromExperience(Experience experience) => 
        new ExperienceViewModel(
            experience.Id, 
            experience.Name, 
            experience.Location, 
            experience.Company, 
            experience.Description, 
            experience.StartDate, 
            experience.EndDate, 
            experience.CurrentJob, 
            experience.Skills.Select(SkillViewModel.FromSkill)
        );
}
