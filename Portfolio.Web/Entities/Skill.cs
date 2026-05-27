namespace Portfolio.Web.Entities;

public class Skill
{
    public Guid Id { get; set; }
    public required string Name { get; set; }

    public Skill(Guid id, string name)
    {
        Id = id;
        Name = name;
    }

    public Skill()
    {
        
    }
}