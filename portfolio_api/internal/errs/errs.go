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

func (e ErrorDetail) Render(_ http.ResponseWriter, r *http.Request) error {
	render.Status(r, e.Status)
	return nil
}

func (i ErrorDetail) Error() string {
	return fmt.Sprintf("%s: %s", i.Title, i.Detail)
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

func NewBadRequestError(errors []RequestError, title string, detail string) *BadRequestError {
	return &BadRequestError{
		Errors:      errors,
		ErrorDetail: NewErrorDetail(title, http.StatusBadRequest, detail),
	}
}

type NotFoundError struct {
	*ErrorDetail
}

func NewNotFoundError(title string, detail string) *NotFoundError {
	return &NotFoundError{
		ErrorDetail: NewErrorDetail(title, http.StatusNotFound, detail),
	}
}

type InternalServerError struct {
	*ErrorDetail
}

func NewInternalServerError(title string, detail string) *InternalServerError {
	return &InternalServerError{
		ErrorDetail: NewErrorDetail(title, http.StatusInternalServerError, detail),
	}
}

type ConflictError struct {
	*ErrorDetail
}

func NewConflictError(title string, detail string) *ConflictError {
	return &ConflictError{
		ErrorDetail: NewErrorDetail(title, http.StatusConflict, detail),
	}
}

type UnauthorizedError struct {
	*ErrorDetail
}

func NewUnauthorizedError(title string, detail string) *UnauthorizedError {
	return &UnauthorizedError{ErrorDetail: NewErrorDetail(title, http.StatusUnauthorized, detail)}
}
