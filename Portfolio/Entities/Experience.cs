namespace Portfolio.Web.Entities;

public class Experience
{

    public Guid Id { get; set; }

    public required string Name { get; set; }

    public required string Location { get; set; }

    public required string Company { get; set; }

    public required string Description { get; set; }

    public DateOnly StartDate { get; set; }

    public DateOnly EndDate { get; set; }

    public bool CurrentJob { get; set; }

    public List<Skill> Skills { get; set; } = [];

    public Experience(Guid id, string name, string location, string company, string description, DateOnly startDate, DateOnly endDate, bool currentJob)
    {
        Id = id;
        Name = name;
        Location = location;
        Company = company;
        Description = description;
        StartDate = startDate;
        EndDate = endDate;
        CurrentJob = currentJob;
    }

    public Experience()
    {
    }


}