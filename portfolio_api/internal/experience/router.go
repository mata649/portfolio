package experience

import (
	"github.com/mata649/portfolio/portfolio_api/internal/jwt"
	"log/slog"
	"net/http"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/render"
	"github.com/mata649/portfolio/portfolio_api/internal/response"
	"github.com/mata649/portfolio/portfolio_api/internal/skill"
	"gorm.io/gorm"
)

func SetupRouter(db *gorm.DB) http.Handler {
	r := chi.NewRouter()
	skillRepository := skill.NewRepository(db)
	experienceRepository := NewRepository(db)
	service := NewService(experienceRepository, skillRepository)
	r.Route("/", func(pr chi.Router) {
		pr.Use(jwt.ProtectRoutes())
		pr.Post("/", createExperienceHandler(service))
		pr.Put("/{id}", updateExperienceHandler(service))
		pr.Delete("/{id}", deleteExperienceHandler(service))
	})
	r.Get("/{id}", findExperienceByIDHandler(service))
	r.Get("/", findAllExperiencesHandler(service))
	return r
}

func createExperienceHandler(s Service) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		req := &CreateExperienceRequest{}
		_ = render.Bind(r, req)
		err := s.Create(r.Context(), req)
		if err != nil {
			slog.Error("createExperienceHandler: Error %s", err)
			response.HandleServiceError(w, r, err)
			return
		}
		render.Status(r, http.StatusCreated)
	}
}

func findExperienceByIDHandler(s Service) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		id := chi.URLParam(r, "id")
		resp, err := s.FindById(r.Context(), id)
		if err != nil {
			slog.Error("findExperienceByHandler: Error %s", err)
			response.HandleServiceError(w, r, err)
			return
		}
		render.Status(r, http.StatusOK)
		render.JSON(w, r, resp)

	}
}

func findAllExperiencesHandler(s Service) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		resp, err := s.FindAll(r.Context())
		if err != nil {
			slog.Error("findAllExperiencesHandler: Error %s", err)
			response.HandleServiceError(w, r, err)
			return
		}
		render.Status(r, http.StatusOK)
		render.JSON(w, r, resp)
	}
}

func updateExperienceHandler(s Service) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		id := chi.URLParam(r, "id")

		req := &UpdateExperienceRequest{}
		_ = render.Bind(r, req)
		err := s.Update(r.Context(), id, req)
		if err != nil {
			slog.Error("updateExperienceHandler: Error %s", err)
			response.HandleServiceError(w, r, err)
			return
		}
		render.Status(r, http.StatusOK)
	}
}

func deleteExperienceHandler(s Service) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		id := chi.URLParam(r, "id")
		err := s.Delete(r.Context(), id)
		if err != nil {
			response.HandleServiceError(w, r, err)
			return
		}
		render.Status(r, http.StatusOK)

	}
}
