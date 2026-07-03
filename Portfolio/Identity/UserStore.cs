namespace Portfolio.Web.Identity;

using Data;
using Entities;
using Microsoft.AspNetCore.Identity;
using Microsoft.EntityFrameworkCore;

public class UserStore(ApplicationDbContext context) : IUserStore<User>, IUserEmailStore<User>, IUserPasswordStore<User>, IAsyncDisposable
{


    public Task<string> GetUserIdAsync(User user, CancellationToken cancellationToken) =>
        Task.FromResult(user.Id.ToString());
    public Task<string?> GetUserNameAsync(User user, CancellationToken cancellationToken) =>
        Task.FromResult(user.NormalizedEmail)!;

    public Task SetUserNameAsync(User user, string? userName, CancellationToken cancellationToken) =>
        throw new NotImplementedException();

    public Task<string?> GetNormalizedUserNameAsync(User user, CancellationToken cancellationToken) =>
        throw new NotImplementedException();

    public Task SetNormalizedUserNameAsync(User user, string? normalizedName, CancellationToken cancellationToken) =>
        Task.CompletedTask;

    public async Task<IdentityResult> CreateAsync(User user, CancellationToken cancellationToken)
    {
        await context.Users.AddAsync(user, cancellationToken);
        await context.SaveChangesAsync(cancellationToken);
        return IdentityResult.Success;
    }

    public Task<IdentityResult> UpdateAsync(User user, CancellationToken cancellationToken) =>
        throw new NotImplementedException();

    public Task<IdentityResult> DeleteAsync(User user, CancellationToken cancellationToken) =>
        throw new NotImplementedException();

    public async Task<User?> FindByIdAsync(string userId, CancellationToken cancellationToken) =>
        await context.Users.FindAsync([Guid.Parse(userId)], cancellationToken);

    public async Task<User?> FindByNameAsync(string normalizedUserName, CancellationToken cancellationToken) =>
        await context.Users.FirstOrDefaultAsync(user => user.NormalizedEmail == normalizedUserName, cancellationToken);

    public Task SetEmailAsync(User user, string? email, CancellationToken cancellationToken) =>
        throw new NotImplementedException();

    public Task<string?> GetEmailAsync(User user, CancellationToken cancellationToken) =>
        Task.FromResult(user.Email)!;

    public Task<bool> GetEmailConfirmedAsync(User user, CancellationToken cancellationToken) =>
        throw new NotImplementedException();

    public Task SetEmailConfirmedAsync(User user, bool confirmed, CancellationToken cancellationToken) =>
        throw new NotImplementedException();

    public async Task<User?> FindByEmailAsync(string normalizedEmail, CancellationToken cancellationToken) =>
        await context.Users.FirstOrDefaultAsync(user => user.NormalizedEmail == normalizedEmail,
            cancellationToken: cancellationToken);

    public Task<string?> GetNormalizedEmailAsync(User user, CancellationToken cancellationToken) =>
        Task.FromResult(user.NormalizedEmail)!;

    public Task SetNormalizedEmailAsync(User user, string? normalizedEmail, CancellationToken cancellationToken)
    {
        if (string.IsNullOrEmpty(normalizedEmail)) throw new ArgumentNullException(nameof(normalizedEmail));
        user.NormalizedEmail = normalizedEmail;
        return Task.CompletedTask;
    }

    public Task SetPasswordHashAsync(User user, string? passwordHash, CancellationToken cancellationToken)
    {
        if (string.IsNullOrEmpty(passwordHash)) throw new ArgumentNullException(nameof(passwordHash));
        
        user.HashedPassword = passwordHash;
        return Task.CompletedTask;
    }

    public Task<string?> GetPasswordHashAsync(User user, CancellationToken cancellationToken) =>
        Task.FromResult(user.HashedPassword)!;

    public Task<bool> HasPasswordAsync(User user, CancellationToken cancellationToken) =>
        throw new NotImplementedException();

    public void Dispose()
    {
        context.Dispose();
    }

    public async ValueTask DisposeAsync()
    {
        await context.DisposeAsync();
    }
}