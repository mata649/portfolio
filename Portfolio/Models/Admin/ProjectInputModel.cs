namespace Portfolio.Web.Models.Admin;

using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;

public class ProjectInputModel
{
    public Guid? Id { get; set; }

    [Required(ErrorMessage = "Project name is required")]
    [StringLength(100, ErrorMessage = "Name cannot exceed 100 characters")]
    public string Name { get; set; } = string.Empty;

    [Required(ErrorMessage = "Description is required")]
    [StringLength(1000, ErrorMessage = "Description cannot exceed 1000 characters")]
    public string Description { get; set; } = string.Empty;

    [Url(ErrorMessage = "Please enter a valid URL")]
    [StringLength(255, ErrorMessage = "GitHub Link cannot exceed 255 characters")]
    public string? GitHubLink { get; set; }

    public List<Guid> SelectedSkillIds { get; set; } = [];
}
