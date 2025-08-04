package skill

import (
	"fmt"
	"github.com/mata649/portfolio/portfolio_api/internal/errs"
)

func NewBadRequestError(errors []errs.RequestError) *errs.BadRequestError {
	return errs.NewBadRequestError(errors,
		"Skill.BadRequest",
		"There is problem with the skill request")

}

func NewInternalServerError(err error) *errs.InternalServerError {
	return errs.NewInternalServerError("Skill.InternalError",
		fmt.Sprintf(" An internal server error has occurred: %s",
			err.Error()))
}

func NewNotFoundError[T fmt.Stringer](identifier T, identifierName string) *errs.NotFoundError {
	return errs.NewNotFoundError(
		"Skill.NotFound",
		fmt.Sprintf("The skill with the %s %s was not found", identifierName, identifier),
	)
}
