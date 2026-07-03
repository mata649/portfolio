using Microsoft.AspNetCore.Mvc;

namespace Portfolio.Web.Controllers;

using Entities;
using Microsoft.AspNetCore.Identity;
using Models.Login;

public class UsersController(SignInManager<User> signInManager) : Controller
{   
    [Route("/login")]
    public IActionResult Login()
    {
        if (User.Identity?.IsAuthenticated == true)
        {
            return RedirectToAction("Index", "Admin");
        }
        return View();
    }
    
    [HttpPost]
    [Route("/login")]
    public async Task<IActionResult> Login(LoginInputModel model)
    {
        if (!ModelState.IsValid) return View("Login", model);

        var result = await signInManager.PasswordSignInAsync(model.Email, model.Password, true, lockoutOnFailure:false);
        if (result.Succeeded)
        {
            return RedirectToAction("Index", "Admin");
        }

        ModelState.AddModelError(string.Empty, "Email  or password is invalid");   
        return View(model);
    }

    [HttpPost]
    public async Task<IActionResult> Logout()
    {
        await signInManager.SignOutAsync();
        return RedirectToAction("Index", "Home");
    }
    
}