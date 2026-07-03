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
        var skills = await (from s in dbContext.Skills select SkillViewModel.FromSkill(s)).ToListAsync();
        var model = new AdminViewModel()
        {
            Skills = skills
        };
        return View(model);
    }
}