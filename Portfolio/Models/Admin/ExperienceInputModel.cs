namespace Portfolio.Web.Models.Admin;

using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;

public class ExperienceInputModel
{
    public Guid? Id { get; set; }

    [Required(ErrorMessage = "Job title / role is required")]
    [StringLength(100, ErrorMessage = "Role cannot exceed 100 characters")]
    public string Name { get; set; } = string.Empty;

    [Required(ErrorMessage = "Location is required")]
    [StringLength(100, ErrorMessage = "Location cannot exceed 100 characters")]
    public string Location { get; set; } = string.Empty;

    [Required(ErrorMessage = "Company is required")]
    [StringLength(100, ErrorMessage = "Company name cannot exceed 100 characters")]
    public string Company { get; set; } = string.Empty;

    [Required(ErrorMessage = "Description is required")]
    [StringLength(2000, ErrorMessage = "Description cannot exceed 2000 characters")]
    public string Description { get; set; } = string.Empty;

    [Required(ErrorMessage = "Start date is required")]
    public DateOnly StartDate { get; set; } = DateOnly.FromDateTime(DateTime.Today);

    public DateOnly EndDate { get; set; } = DateOnly.FromDateTime(DateTime.Today);

    public bool CurrentJob { get; set; }

    public List<Guid> SelectedSkillIds { get; set; } = [];
}
