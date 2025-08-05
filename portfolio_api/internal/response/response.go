package response

import (
	"net/http"

	"github.com/go-chi/render"
	"github.com/mata649/portfolio/portfolio_api/internal/errs"
)

type InternalServerErrResp struct {
	Message string `json:"message"`
}

func HandleServiceError(w http.ResponseWriter, r *http.Request, err error) {

	sErr, ok := err.(errs.ServiceError)
	errDetail := sErr.GetErrorDetail()
	if !ok || errDetail.Status == http.StatusInternalServerError {
		render.Status(r, http.StatusInternalServerError)
		render.JSON(w, r, &InternalServerErrResp{Message: "Internal Error"})
		return
	}
	render.Status(r, errDetail.Status)
	render.JSON(w, r, err)

}
