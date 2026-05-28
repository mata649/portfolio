namespace Portfolio.Web.Data;

using Entities;
using Microsoft.EntityFrameworkCore;

public class ApplicationDbContext(DbContextOptions options) :DbContext(options)
{
    public DbSet<User> User { get; set; }

    public DbSet<Skill> Skills { get; set; }

    public DbSet<Experience> Experiences { get; set; }

    public DbSet<Project> Projects { get; set; }

    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        modelBuilder.ApplyConfigurationsFromAssembly(typeof(ApplicationDbContext).Assembly);
    }
}