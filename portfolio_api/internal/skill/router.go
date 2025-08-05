package skill

import (
	"github.com/go-chi/chi/v5"
	"github.com/mata649/portfolio/portfolio_api/internal/jwt"
	"log/slog"
	"net/http"

	"github.com/go-chi/render"
	"github.com/mata649/portfolio/portfolio_api/internal/response"
	"gorm.io/gorm"
)

func SetupRouter(db *gorm.DB) http.Handler {
	r := chi.NewRouter()
	skillRepository := NewRepository(db)
	service := NewService(skillRepository)

	r.Route("/", func(pr chi.Router) {
		pr.Use(jwt.ProtectRoutes())
		pr.Post("/", createSkillHandler(service))
		pr.Put("/{id}", updateSkillHandler(service))
		pr.Delete("/{id}", deleteSkillHandler(service))
	})
	r.Get("/{id}", findSkillByIDHandler(service))
	r.Get("/", findAllSkillsHandler(service))
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

func findSkillByIDHandler(s Service) http.HandlerFunc {
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
