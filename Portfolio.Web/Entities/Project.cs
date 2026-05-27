namespace Portfolio.Web.Entities;

public class Project
{
    public Guid Id { get; set; }
    public required string Name { get; set; }
    public required string Description { get; set; }
    
    public string? GitHubLink { get; set; }

    public List<Skill> Skills { get; set; } = [];

    public Project(Guid id, string name, string description, string? gitHubLink)
    {
        Id = id;
        Name = name;
        Description = description;
        GitHubLink = gitHubLink;
    }

    public Project()
    {
        
    }
}