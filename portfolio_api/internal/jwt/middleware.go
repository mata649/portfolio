package jwt

import (
	"net/http"
	"strings"

	"github.com/go-chi/render"
	"github.com/mata649/portfolio/portfolio_api/internal/config"
	"github.com/mata649/portfolio/portfolio_api/internal/errs"
)

func ProtectRoutes() func(next http.Handler) http.Handler {
	cfg := config.LoadConfig()

	jwtService := NewService(cfg.JwtSigningKey)
	return func(next http.Handler) http.Handler {
		fn := func(w http.ResponseWriter, r *http.Request) {
			h, ok := r.Header["Authorization"]
			if !ok {
				writeForbiddenError(w, r)
				return
			}
			if !strings.HasPrefix(h[0], "Bearer ") {
				writeForbiddenError(w, r)
				return
			}
			secs := strings.Split(h[0], " ")
			if len(secs) != 2 {
				writeForbiddenError(w, r)
				return
			}
			token := secs[1]
			err := jwtService.VerifyToken(token)
			if err != nil {
				writeForbiddenError(w, r)
				return
			}
			next.ServeHTTP(w, r)
		}
		return http.HandlerFunc(fn)
	}
}

func writeForbiddenError(w http.ResponseWriter, r *http.Request) {
	err := NewForbiddenError()
	render.Status(r, err.Status)
	render.JSON(w, r, err)
}

func NewForbiddenError() *errs.ForbiddenError {
	return errs.NewForbiddenError(
		"Authentication.Forbidden",
		"You're not allowed to perform this action")
}
