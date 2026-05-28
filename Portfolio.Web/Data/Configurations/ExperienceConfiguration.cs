namespace Portfolio.Web.Data.Configurations;

using Entities;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;

public class ExperienceConfiguration : IEntityTypeConfiguration<Experience>
{

    public void Configure(EntityTypeBuilder<Experience> builder)
    {
        builder.ToTable("Experiences");
        builder.HasKey(x => x.Id);
        builder.Property(x => x.Id).ValueGeneratedOnAdd();
        builder.Property(x => x.Name).IsRequired().HasMaxLength(100);
        builder.Property(x => x.Location).IsRequired().HasMaxLength(100);
        builder.Property(x => x.Company).IsRequired().HasMaxLength(100);
        builder.Property(x => x.Description).IsRequired().HasMaxLength(2000);
        builder.Property(x => x.CurrentJob).IsRequired();
        builder.Property(x => x.EndDate).IsRequired();
        builder.Property(x => x.StartDate).IsRequired();
        builder.HasMany(x => x.Skills).WithMany(x => x.Experiences);
    }
}