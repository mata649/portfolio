package response

import (
	"errors"
	"github.com/go-chi/render"
	"github.com/mata649/portfolio/portfolio_api/internal/errs"
	"net/http"
)

func HandleServiceError(w http.ResponseWriter, r *http.Request, err error) {
	var badRequestError *errs.BadRequestError
	var notFoundError *errs.NotFoundError
	var internalServerError *errs.InternalServerError
	switch {
	case errors.As(err, &badRequestError):
		render.Status(r, http.StatusBadRequest)
	case errors.As(err, &notFoundError):
		render.Status(r, http.StatusNotFound)
	case errors.As(err, &internalServerError):
		render.Status(r, http.StatusInternalServerError)
		return
	default:
		render.Status(r, http.StatusInternalServerError)
	}
	render.JSON(w, r, err)

}
