package auth

import (
	"fmt"

	"github.com/mata649/portfolio/portfolio_api/internal/errs"
)

func NewBadRequestError(errors []errs.RequestError) *errs.BadRequestError {
	return errs.NewBadRequestError(errors,
		"Authentication.BadRequest",
		"There is problem with the authentication request")

}

func NewInternalServerError(err error) *errs.InternalServerError {
	return errs.NewInternalServerError("Authentication.InternalError",
		fmt.Sprintf(" An internal server error has occurred: %s",
			err.Error()))
}

func NewUserNotFoundError[T fmt.Stringer](identifier, identifierName string) *errs.NotFoundError {
	return errs.NewNotFoundError(
		"Authentication.UserNotFound",
		fmt.Sprintf("The user with the %s %s was not found", identifierName, identifier),
	)
}

func NewEmailAlreadyTakenError(email string) *errs.ConflictError {
	return errs.NewConflictError(
		"Authentication.EmailAlreadyTaken",
		fmt.Sprintf("The email %s has been already taken", email),
	)
}

func NewInvalidCredentialsError() *errs.UnauthorizedError {
	return errs.NewUnauthorizedError(
		"Authentication.InvalidCredentials",
		"The email or password is wrong")
}
