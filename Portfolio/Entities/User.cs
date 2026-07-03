namespace Portfolio.Web.Entities;

public class User
{
    public Guid Id { get; set; }

    public required string Email { get; set; }
    public string? NormalizedEmail { get; set; }
    public string? HashedPassword { get; set; }
    

    public User()
    {
    }

    public User(string email)
    {
        Email = email;
        NormalizedEmail = email.ToUpper();
    }
}