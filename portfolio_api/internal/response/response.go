package response

import (
	"errors"
	"fmt"
	"net/http"

	"github.com/go-chi/render"
	"github.com/mata649/portfolio/portfolio_api/internal/errs"
)

type InternalServerErrResp struct {
	Message string `json:"message"`
}

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
	default:
		render.Status(r, http.StatusInternalServerError)
	}

	if errors.As(err, &internalServerError) {
		fmt.Println("Int error branch")
		render.JSON(w, r, &InternalServerErrResp{Message: "Internal Error"})
	} else {
		fmt.Println("Other error branch")
		render.JSON(w, r, err)
	}

}
