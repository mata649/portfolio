package errs

import (
	"errors"
	"fmt"
	"github.com/go-chi/render"
	"github.com/go-playground/validator/v10"
	"net/http"
)

type RequestError struct {
	Parameter string `json:"parameter"`
	Error     string `json:"error"`
}

func NewRequestError(param string, error string) RequestError {
	return RequestError{
		Parameter: param,
		Error:     error,
	}
}
func GetRequestErrors(err error) []RequestError {
	var valErrs validator.ValidationErrors
	var requestErrors []RequestError
	if errors.As(err, &valErrs) {
		for _, val := range valErrs {

			requestErrors = append(requestErrors, NewRequestError(val.Field(), val.Tag()))
		}
	}
	return requestErrors
}

type ErrorDetail struct {
	Title  string `json:"title"`
	Status int    `json:"status"`
	Detail string `json:"detail"`
}

func (e ErrorDetail) Render(w http.ResponseWriter, r *http.Request) error {
	render.Status(r, e.Status)
	return nil
}
func NewErrorDetail(title string, status int, detail string) *ErrorDetail {
	return &ErrorDetail{
		Title:  title,
		Status: status,
		Detail: detail,
	}
}

type BadRequestError struct {
	Errors []RequestError
	*ErrorDetail
}

func (b BadRequestError) Error() string {

	return fmt.Sprintf("%s: %s - %v", b.ErrorDetail.Title, b.ErrorDetail.Detail, b.Errors)
}

func NewBadRequestError(errors []RequestError, title string, status int, detail string) *BadRequestError {
	return &BadRequestError{
		Errors:      errors,
		ErrorDetail: NewErrorDetail(title, status, detail),
	}
}

type NotFoundError struct {
	*ErrorDetail
}

func (n NotFoundError) Error() string {
	return fmt.Sprintf("%s: %s", n.Title, n.Detail)
}

func NewNotFoundError(title string, status int, detail string) *NotFoundError {
	return &NotFoundError{
		ErrorDetail: NewErrorDetail(title, status, detail),
	}
}

type InternalServerError struct {
	*ErrorDetail
}

func (i InternalServerError) Error() string {
	return fmt.Sprintf("%s: %s", i.Title, i.Detail)
}

func NewInternalServerError(title string, status int, detail string) *InternalServerError {
	return &InternalServerError{
		ErrorDetail: NewErrorDetail(title, status, detail),
	}
}
