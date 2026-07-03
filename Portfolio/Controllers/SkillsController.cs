namespace Portfolio.Web.Controllers;

using Data;
using Entities;
using Microsoft.AspNetCore.Mvc;
using Models.Admin;

public class SkillsController(ApplicationDbContext dbContext) : Controller
{
    public IActionResult Create()
    {
        return PartialView("~/Views/Admin/Skills/_SkillForm.cshtml", new SkillInputModel());
    }
    
    [HttpPost]
    public async Task<IActionResult> Create(SkillInputModel model)
    {
        if (!ModelState.IsValid)
            return PartialView("Skills/_SkillForm");
        var skill = new Skill()
        {
            Id = Guid.NewGuid(),
            Name = model.Name,
        };
        await dbContext.AddAsync(skill);
        await dbContext.SaveChangesAsync();
        Response.Headers.Append("HX-Trigger", "closeModal");
        return PartialView("~/Views/Admin/Skills/_SkillRow.cshtml", SkillViewModel.FromSkill(skill));
    }
}