package experience

import (
	"github.com/mata649/portfolio/portfolio_api/internal/jwt"
	"github.com/mata649/portfolio/portfolio_api/internal/sorting"
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
			slog.Error("error creating experience", "error", err, "request", req)
			response.HandleServiceError(w, r, err)
			return
		}
		render.Status(r, http.StatusCreated)
		render.JSON(w, r, nil)
	}
}

func findExperienceByIDHandler(s Service) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		id := chi.URLParam(r, "id")
		resp, err := s.FindById(r.Context(), id)
		if err != nil {
			slog.Error("error finding experience by id", "error", err, "id", id)
			response.HandleServiceError(w, r, err)
			return
		}
		render.Status(r, http.StatusOK)
		render.JSON(w, r, resp)

	}
}

func findAllExperiencesHandler(s Service) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		by := r.URL.Query().Get("sortBy")
		order := r.URL.Query().Get("sortOrder")
		sort := sorting.NewSort(by, order)
		resp, err := s.FindAll(r.Context(), sort)
		if err != nil {
			slog.Error("error finding all experiences", "error", err, "sort", sort)
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
			slog.Error("error updating experience", "error", err, "id", id, "request", req)
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
			slog.Error("error deleting experience", "error", err, "id", id)
			response.HandleServiceError(w, r, err)
			return
		}
		render.Status(r, http.StatusOK)

	}
}
