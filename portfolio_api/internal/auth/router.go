package auth

import (
	"log/slog"
	"net/http"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/render"
	"github.com/mata649/portfolio/portfolio_api/internal/config"
	"github.com/mata649/portfolio/portfolio_api/internal/jwt"
	"github.com/mata649/portfolio/portfolio_api/internal/response"
	"gorm.io/gorm"
)

func SetupRouter(db *gorm.DB, config *config.Config) http.Handler {
	r := chi.NewRouter()
	authRepository := NewRepository(db)
	jwtService := jwt.NewService(config.JwtSigningKey)
	service := NewService(authRepository, jwtService)

	r.Post("/login", loginUserHandler(service))
	r.Route("/", func(prt chi.Router) {
		prt.Use(jwt.ProtectRoutes())
		prt.Post("/validate", validateTokenHandler())
	})
	return r
}

func loginUserHandler(s Service) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		req := &LoginUserRequest{}
		_ = render.Bind(r, req)

		resp, err := s.LoginUser(r.Context(), req)
		if err != nil {
			slog.Error("loginUserHandler: Error %s", err)
			response.HandleServiceError(w, r, err)
			return
		}
		render.Status(r, http.StatusOK)
		render.JSON(w, r, resp)

	}
}

func validateTokenHandler() http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		render.Status(r, http.StatusOK)
		render.JSON(w, r, nil)
	}
}
