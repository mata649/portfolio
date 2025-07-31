package project

import (
	"context"
	"net/http"
	"time"

	"github.com/go-playground/validator/v10"
	"github.com/google/uuid"
	"github.com/mata649/portfolio/portfolio_api/internal/errs"
	"github.com/mata649/portfolio/portfolio_api/internal/skill"
	"gorm.io/gorm"
)

type CreateProjectRequest struct {
	ID          uuid.UUID   `json:"id" validate:"required,uuid"`
	Name        string      `json:"name" validate:"required,min=2,max=24"`
	Description string      `json:"description" validate:"required,min=2,max=1024"`
	GithubLink  string      `json:"githubLink" validate:"required,http_url,max=64"`
	Skills      []uuid.UUID `json:"skills" validate:"required,min=1"`
}

func (s *CreateProjectRequest) Bind(_ *http.Request) error {
	return nil
}

type UpdateProjectRequest struct {
	Name        string      `json:"name" validate:"required,min=2,max=24"`
	Description string      `json:"description" validate:"required,min=2,max=1024"`
	GithubLink  string      `json:"githubLink" validate:"required,http_url,max=64"`
	Skills      []uuid.UUID `json:"skills" validate:"required,min=1"`
}

func (s *UpdateProjectRequest) Bind(_ *http.Request) error {
	return nil
}

type Response struct {
	ID          uuid.UUID         `json:"id"`
	Name        string            `json:"name"`
	Description string            `json:"description"`
	GithubLink  string            `json:"githubLink"`
	Skills      []*skill.Response `json:"skills"`
	CreatedAt   time.Time         `json:"createdAt"`
	UpdatedAt   time.Time         `json:"updatedAt"`
	DeletedAt   gorm.DeletedAt    `json:"deletedAt"`
}

func NewResponse(project *Project) *Response {

	parsedSkills := make([]*skill.Response, len(project.Skills))
	for index, s := range project.Skills {
		parsedSkills[index] = skill.NewResponse(s)
	}
	return &Response{
		ID:          project.ID,
		Name:        project.Name,
		Description: project.Description,
		GithubLink:  project.GithubLink,
		Skills:      parsedSkills,
		CreatedAt:   project.CreatedAt,
		UpdatedAt:   project.UpdatedAt,
		DeletedAt:   project.DeletedAt,
	}
}

var validate = validator.New()

type Service struct {
	projectRepository Repository
	skillRepository   skill.Repository
}

func NewService(projectRepository Repository, skillRepository skill.Repository) Service {
	return Service{
		projectRepository: projectRepository,
		skillRepository:   skillRepository,
	}
}

func (s Service) Create(ctx context.Context, request *CreateProjectRequest) error {
	err := validate.Struct(request)
	if err != nil {
		requestErrors := errs.GetRequestErrors(err)
		return NewBadRequestError(requestErrors)
	}
	skills, err := s.skillRepository.FindSkillsByIDs(ctx, request.Skills)
	project := NewProject(request.ID, request.Name, request.Description, request.GithubLink, skills)
	err = s.projectRepository.Create(ctx, project)
	if err != nil {
		return NewInternalServerError(err)
	}
	return nil
}

func (s Service) FindById(ctx context.Context, id string) (*Response, error) {
	projectId, err := uuid.Parse(id)
	if err != nil {
		return nil, NewNotFoundError(projectId, "id")
	}

	project, err := s.projectRepository.FindById(ctx, projectId)
	if err != nil {
		return nil, NewInternalServerError(err)
	}
	return NewResponse(project), nil
}

func (s Service) FindAll(ctx context.Context) ([]Response, error) {
	projects, err := s.projectRepository.FindAll(ctx)
	if err != nil {
		return nil, NewInternalServerError(err)
	}

	projectsResponse := make([]Response, len(projects))
	for index, project := range projects {
		projectsResponse[index] = *NewResponse(&project)
	}
	return projectsResponse, nil
}

func (s Service) Update(ctx context.Context, id string, request *UpdateProjectRequest) error {

	projectId, err := uuid.Parse(id)
	if err != nil {
		return NewNotFoundError(projectId, "id")
	}

	err = validate.Struct(request)
	if err != nil {
		requestErrors := errs.GetRequestErrors(err)
		return NewBadRequestError(requestErrors)
	}

	project, err := s.projectRepository.FindById(ctx, projectId)
	if err != nil {
		return NewInternalServerError(err)
	}
	if project == nil {
		return NewNotFoundError(projectId, "id")
	}

	skills, err := s.skillRepository.FindSkillsByIDs(ctx, request.Skills)

	project.Name = request.Name
	project.Description = request.Description
	project.GithubLink = request.GithubLink
	project.Skills = skills
	err = s.projectRepository.Update(ctx, project)
	if err != nil {
		return NewInternalServerError(err)
	}
	return nil
}

func (s Service) Delete(ctx context.Context, id string) error {
	projectId, err := uuid.Parse(id)
	if err != nil {
		return NewNotFoundError(projectId, "id")
	}

	project, err := s.projectRepository.FindById(ctx, projectId)
	if err != nil {
		return NewInternalServerError(err)
	}
	if project == nil {
		return NewNotFoundError(projectId, "id")
	}
	err = s.projectRepository.Delete(ctx, project.ID)
	if err != nil {
		return NewInternalServerError(err)
	}
	return nil
}
