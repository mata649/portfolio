namespace Portfolio.Web.Data.Configurations;

using Entities;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;

public class ProjectConfiguration : IEntityTypeConfiguration<Project>
{

    public void Configure(EntityTypeBuilder<Project> builder)
    {   builder.ToTable("Projects");
        builder.HasKey(x => x.Id);
        builder.Property(x => x.Id).ValueGeneratedOnAdd();
        builder.Property(x => x.Name).HasMaxLength(200).IsRequired();
        builder.Property(x => x.Description).HasMaxLength(2000).IsRequired();
        builder.Property(x=> x.GitHubLink).IsRequired(false).HasMaxLength(255);
        builder.HasMany(x => x.Skills).WithMany(x => x.Projects);
    }
}