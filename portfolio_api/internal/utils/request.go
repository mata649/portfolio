package utils

import (
	"encoding/json"
	"github.com/mata649/portfolio/portfolio_api/internal/errs"
	"io"
	"log/slog"
	"net/http"
)

func Binding(req interface{}, r *http.Request) error {
	body, err := io.ReadAll(r.Body)
	if err != nil {
		slog.Error("BindingError: Error reading body: %s", err)
		return err
	}
	err = json.Unmarshal(body, req)
	if err != nil {
		slog.Error("BindingError: Error parsing body: %s", err)
		return err
	}
	return nil
}
func HandleBindingError(w http.ResponseWriter, reqError *errs.BadRequestError) {
	w.WriteHeader(http.StatusBadRequest)
	jsonData, err := json.Marshal(reqError)
	if err != nil {
		slog.Error("HandleBindingError: error marshalling json - %s", err)
		return
	}
	_, err = w.Write(jsonData)
	if err != nil {
		slog.Error("HandleBindingError: error writing json -  %s", err)
		return
	}

}
