package server

import (
	"fmt"
	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
	"github.com/go-chi/render"
	"github.com/mata649/portfolio/portfolio_api/internal/config"
	"github.com/mata649/portfolio/portfolio_api/internal/skill"
	"gorm.io/gorm"
	"log/slog"
	"net/http"
	"time"
)

type Server struct {
	router *chi.Mux
}

func (s Server) RunServer(cfg *config.Config) error {
	slog.Info("Running server on port %s ...", cfg.WebPort)
	return http.ListenAndServe(fmt.Sprintf(":%s", cfg.WebPort), s.router)
}

func useMiddlewares(r *chi.Mux) {
	r.Use(middleware.RequestID)
	r.Use(middleware.RealIP)
	r.Use(middleware.Logger)
	r.Use(middleware.Recoverer)
	r.Use(middleware.Timeout(10 * time.Second))
	r.Use(render.SetContentType(render.ContentTypeJSON))

}

func NewServer(db *gorm.DB) *Server {
	r := chi.NewRouter()
	useMiddlewares(r)
	skillRouter := skill.SetupRouter(db)
	r.Mount("/api", skillRouter)
	r.Get("/health", healthCheckHandler)
	return &Server{router: r}
}

type healthCheckResponse struct {
	Message string `json:"message"`
}

func healthCheckHandler(w http.ResponseWriter, r *http.Request) {
	render.Status(r, http.StatusOK)
	render.JSON(w, r, healthCheckResponse{
		Message: "OK",
	})
}
