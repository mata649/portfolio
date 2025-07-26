package skill

import (
	"log/slog"
	"net/http"
	"github.com/go-chi/chi/v5"
	"github.com/go-chi/render"
	"github.com/mata649/portfolio/portfolio_api/internal/response"
	"gorm.io/gorm"
)

func SetupRouter(db *gorm.DB) http.Handler {
	r := chi.NewRouter()
	skillRepository := NewRepository(db)
	service := NewService(skillRepository)

	r.Route("/skills", func(r chi.Router) {
		r.Post("/", createSkillHandler(service))
		r.Get("/{id}", findSkillByHandler(service))
		r.Get("/", findAllSkillsHandler(service))
		r.Put("/{id}", updateSkillHandler(service))
		r.Delete("/{id}", deleteSkillHandler(service))
	})
	return r
}

func createSkillHandler(s Service) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		req := &CreateSkillRequest{}
		_ = render.Bind(r, req)
		err := s.Create(r.Context(), req)
		if err != nil {
			slog.Error("createSkillHandler: Error %s", err)
			response.HandleServiceError(w, r, err)
			return
		}
		render.Status(r, http.StatusCreated)
	}
}

func findSkillByHandler(s Service) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		id := chi.URLParam(r, "id")
		resp, err := s.FindById(r.Context(), id)
		if err != nil {
			slog.Error("findSkillByHandler: Error %s", err)
			response.HandleServiceError(w, r, err)
			return
		}
		render.Status(r, http.StatusOK)
		render.JSON(w, r, resp)

	}
}

func findAllSkillsHandler(s Service) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		resp, err := s.FindAll(r.Context())
		if err != nil {
			slog.Error("findAllSkillsHandler: Error %s", err)
			response.HandleServiceError(w, r, err)
			return
		}
		render.Status(r, http.StatusOK)
		render.JSON(w, r, resp)
	}
}

func updateSkillHandler(s Service) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		id := chi.URLParam(r, "id")

		req := &UpdateSkillRequest{}
		_ = render.Bind(r, req)
		err := s.Update(r.Context(), id, req)
		if err != nil {
			slog.Error("updateSkillHandler: Error %s", err)
			response.HandleServiceError(w, r, err)
			return
		}
		render.Status(r, http.StatusOK)
	}
}

func deleteSkillHandler(s Service) http.HandlerFunc {
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
