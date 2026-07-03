namespace Portfolio.Web.Models.Login;

using System.ComponentModel.DataAnnotations;

public class LoginInputModel
{
    [Required] [EmailAddress] public string Email { get; set; }

    [Required]
    [DataType(DataType.Password)]
    public string Password { get; set; }

}