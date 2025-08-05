package server

import (
	"fmt"
	"log/slog"
	"net/http"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
	"github.com/go-chi/render"
	"github.com/mata649/portfolio/portfolio_api/internal/auth"
	"github.com/mata649/portfolio/portfolio_api/internal/config"
	"github.com/mata649/portfolio/portfolio_api/internal/experience"
	"github.com/mata649/portfolio/portfolio_api/internal/project"
	"github.com/mata649/portfolio/portfolio_api/internal/skill"
	"gorm.io/gorm"
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

func NewServer(db *gorm.DB, cfg *config.Config) *Server {
	r := chi.NewRouter()
	useMiddlewares(r)
	skillRouter := skill.SetupRouter(db)
	projectRouter := project.SetupRouter(db)
	experienceRouter := experience.SetupRouter(db)
	authRouter := auth.SetupRouter(db, cfg)
	r.Mount("/api/skills", skillRouter)
	r.Mount("/api/projects", projectRouter)
	r.Mount("/api/experiences", experienceRouter)
	r.Mount("/api/auth", authRouter)
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
