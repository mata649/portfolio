package project

import (
	"fmt"
	"github.com/mata649/portfolio/portfolio_api/internal/errs"
)

func NewBadRequestError(errors []errs.RequestError) *errs.BadRequestError {
	return errs.NewBadRequestError(errors,
		"Project.BadRequest",
		"There is problem with the project request")

}

func NewInternalServerError(err error) *errs.InternalServerError {
	return errs.NewInternalServerError("Project.InternalError",
		fmt.Sprintf(" An internal server error has occurred: %s",
			err.Error()))
}

func NewNotFoundError[T fmt.Stringer](identifier T, identifierName string) *errs.NotFoundError {
	return errs.NewNotFoundError(
		"Project.NotFound",
		fmt.Sprintf("The project with the %s %s was not found", identifierName, identifier),
	)
}
