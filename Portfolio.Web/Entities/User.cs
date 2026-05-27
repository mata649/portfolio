namespace Portfolio.Web.Users.Domain;

public class User
{
    public Guid Id { get; set; }

    public required string Email { get; set; }

    public required string HashedPassword { get; set; }

    public User()
    {
    }

    public User(string email, string hashedPassword)
    {
        Email = email;
        HashedPassword = hashedPassword;
    }
}