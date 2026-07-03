namespace Portfolio.Web.Models.Admin;

using System.ComponentModel.DataAnnotations;

public class SkillInputModel
{
    [Required]
    public string Name { get; set; }
}