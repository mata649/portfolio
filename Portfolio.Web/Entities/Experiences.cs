namespace Portfolio.Web.Entities;

public class Experiences
{

    public Guid Id { get; set; }

    public required string PositionName { get; set; }

    public required string Company { get; set; }

    public required string Description { get; set; }

    public DateOnly StartDate { get; set; }

    public DateOnly EndDate { get; set; }

    public bool CurrentJob { get; set; }

    public List<Skill> Skills { get; set; } = [];

    public Experiences(
        Guid id,
        string positionName,
        string company,
        string description,
        DateOnly startDate,
        DateOnly endDate,
        bool currentJob
    )
    {
        Id = id;
        PositionName = positionName;
        Company = company;
        Description = description;
        StartDate = startDate;
        EndDate = endDate;
        CurrentJob = currentJob;
    }

    public Experiences()
    {
    }


}