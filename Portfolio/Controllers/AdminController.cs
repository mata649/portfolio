namespace Portfolio.Web.Controllers;

using Data;
using Entities;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Models;
using Models.Admin;

[Authorize]
public class AdminController(ApplicationDbContext dbContext) : Controller
{
    public async Task<IActionResult> Index()
    {
        var skills = await dbContext.Skills
            .Select(s => SkillViewModel.FromSkill(s))
            .ToListAsync();

        var projects = await dbContext.Projects
            .Include(p => p.Skills)
            .Select(p => ProjectViewModel.FromProject(p))
            .ToListAsync();

        var experiences = await dbContext.Experiences
            .Include(e => e.Skills)
            .Select(e => ExperienceViewModel.FromExperience(e))
            .ToListAsync();

        var model = new AdminViewModel()
        {
            Skills = skills,
            Projects = projects,
            Experiences = experiences
        };
        return View(model);
    }
}