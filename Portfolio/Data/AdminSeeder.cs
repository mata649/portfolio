namespace Portfolio.Web.Data;

using Entities;
using Microsoft.AspNetCore.Identity;

public static class AdminSeeder
{
    public static async Task SeedAdminUser(this IApplicationBuilder app, IConfiguration config)
    {
        var email = config["AdminSettings:Email"];
        var  password = config["AdminSettings:Password"];
        if (string.IsNullOrWhiteSpace(email) || string.IsNullOrWhiteSpace(password))
            throw new ArgumentException("Admin user password and admin user email configuration are required.");

        var scope = app.ApplicationServices.CreateScope();
        using var userManager = scope.ServiceProvider.GetRequiredService<UserManager<User>>();
        
        var userFound = await userManager.FindByEmailAsync(email);
        if (userFound is not null)
            return;

        var user = new User
        {
            Email = email,
            NormalizedEmail = email.ToUpper(),
        };
        var result = await userManager.CreateAsync(user, password);
        if (!result.Succeeded)
        {
            result.Errors.ToList().ForEach(error =>  Console.WriteLine(error.Description));
        }
    }
}