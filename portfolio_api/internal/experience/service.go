package experience

import (
	"context"
	"net/http"
	"slices"
	"time"

	"github.com/go-playground/validator/v10"
	"github.com/google/uuid"
	"github.com/mata649/portfolio/portfolio_api/internal/errs"
	"github.com/mata649/portfolio/portfolio_api/internal/skill"
	"gorm.io/gorm"
)

type CreateExperienceRequest struct {
	ID           uuid.UUID   `json:"id" validate:"required,uuid"`
	Position     string      `json:"position" validate:"required,min=2,max=48"`
	Location     string      `json:"location" validate:"required,min=2,max=24"`
	Description  string      `json:"description" validate:"required,min=2,max=2048"`
	StartDate    time.Time   `json:"startDate" validate:"required,ltfield=EndDate"`
	EndDate      time.Time   `json:"endDate" validate:"gtfield=StartDate"`
	IsCurrentJob bool        `json:"isCurrentJob" validate:"required"`
	Skills       []uuid.UUID `json:"skills" validate:"required,min=1"`
}

func (s *CreateExperienceRequest) Bind(_ *http.Request) error {
	return nil
}

type UpdateExperienceRequest struct {
	Position     string      `json:"position" validate:"required,min=2,max=48"`
	Location     string      `json:"location" validate:"required,min=2,max=24"`
	Description  string      `json:"description" validate:"required,min=2,max=2048"`
	StartDate    time.Time   `json:"startDate" validate:"required,ltfield=EndDate"`
	EndDate      time.Time   `json:"endDate" validate:"gtfield=StartDate"`
	IsCurrentJob bool        `json:"isCurrentJob" validate:"required"`
	Skills       []uuid.UUID `json:"skills" validate:"required,min=1"`
}

func (s *UpdateExperienceRequest) Bind(_ *http.Request) error {
	return nil
}

type Response struct {
	ID           uuid.UUID        `json:"id"`
	Position     string           `json:"position"`
	Location     string           `json:"location"`
	Description  string           `json:"description"`
	StartDate    time.Time        `json:"startDate"`
	EndDate      time.Time        `json:"endDate"`
	IsCurrentJob bool             `json:"isCurrentJob"`
	Skills       []skill.Response `json:"skills"`
	CreatedAt    time.Time        `json:"createdAt"`
	UpdatedAt    time.Time        `json:"updatedAt"`
	DeletedAt    gorm.DeletedAt   `json:"deletedAt"`
}

func NewResponse(experience *Experience) *Response {

	parsedSkills := make([]skill.Response, len(experience.Skills))
	for index, s := range experience.Skills {
		parsedSkills[index] = *skill.NewResponse(&s)
	}
	return &Response{
		ID:           experience.ID,
		Position:     experience.Position,
		Location:     experience.Location,
		Description:  experience.Description,
		StartDate:    experience.StartDate,
		EndDate:      experience.EndDate,
		IsCurrentJob: experience.IsCurrentJob,
		Skills:       parsedSkills,
		CreatedAt:    experience.CreatedAt,
		UpdatedAt:    experience.UpdatedAt,
		DeletedAt:    experience.DeletedAt,
	}
}

var validate = validator.New()

type Service struct {
	experienceRepository Repository
	skillRepository      skill.Repository
}

func NewService(experienceRepository Repository, skillRepository skill.Repository) Service {
	return Service{
		experienceRepository: experienceRepository,
		skillRepository:      skillRepository,
	}
}
func checkIfSkillsWereFound(skills []skill.Skill, ids []uuid.UUID) error {
	if len(skills) == len(ids) {
		return nil
	}
	skillIds := make([]uuid.UUID, len(skills))
	for i, s := range skills {
		skillIds[i] = s.ID
	}
	for _, id := range ids {
		if !slices.Contains(skillIds, id) {
			return skill.NewNotFoundError(id, "id")
		}
	}
	return nil
}
func (s Service) Create(ctx context.Context, request *CreateExperienceRequest) error {
	err := validate.Struct(request)
	if err != nil {
		requestErrors := errs.GetRequestErrors(err)
		return NewBadRequestError(requestErrors)
	}
	skills, err := s.skillRepository.FindSkillsByIDs(ctx, request.Skills)
	if err != nil {
		return NewInternalServerError(err)
	}
	err = checkIfSkillsWereFound(skills, request.Skills)
	if err != nil {
		return err
	}
	experience := NewExperience(request.ID,
		request.Position,
		request.Location,
		request.Description,
		request.StartDate,
		request.EndDate,
		request.IsCurrentJob,
		skills)

	err = s.experienceRepository.Create(ctx, experience)
	if err != nil {
		return NewInternalServerError(err)
	}
	return nil
}

func (s Service) FindById(ctx context.Context, id string) (*Response, error) {
	experienceId, err := uuid.Parse(id)
	if err != nil {
		return nil, NewNotFoundError(experienceId, "id")
	}

	experience, err := s.experienceRepository.FindById(ctx, experienceId)
	if err != nil {
		return nil, NewInternalServerError(err)
	}
	return NewResponse(experience), nil
}

func (s Service) FindAll(ctx context.Context) ([]Response, error) {
	experiences, err := s.experienceRepository.FindAll(ctx)
	if err != nil {
		return nil, NewInternalServerError(err)
	}

	experiencesResponse := make([]Response, len(experiences))
	for index, experience := range experiences {
		experiencesResponse[index] = *NewResponse(&experience)
	}
	return experiencesResponse, nil
}

func (s Service) Update(ctx context.Context, id string, request *UpdateExperienceRequest) error {

	experienceId, err := uuid.Parse(id)
	if err != nil {
		return NewNotFoundError(experienceId, "id")
	}

	err = validate.Struct(request)
	if err != nil {
		requestErrors := errs.GetRequestErrors(err)
		return NewBadRequestError(requestErrors)
	}

	experience, err := s.experienceRepository.FindById(ctx, experienceId)
	if err != nil {
		return NewInternalServerError(err)
	}
	if experience == nil {
		return NewNotFoundError(experienceId, "id")
	}

	skills, err := s.skillRepository.FindSkillsByIDs(ctx, request.Skills)
	err = checkIfSkillsWereFound(skills, request.Skills)
	if err != nil {
		return err
	}
	experience.Position = request.Position
	experience.Location = request.Location
	experience.Description = request.Description
	experience.StartDate = request.StartDate
	experience.EndDate = request.EndDate
	experience.IsCurrentJob = request.IsCurrentJob
	experience.Skills = skills
	err = s.experienceRepository.Update(ctx, experience)
	if err != nil {
		return NewInternalServerError(err)
	}
	return nil
}

func (s Service) Delete(ctx context.Context, id string) error {
	experienceId, err := uuid.Parse(id)
	if err != nil {
		return NewNotFoundError(experienceId, "id")
	}

	experience, err := s.experienceRepository.FindById(ctx, experienceId)
	if err != nil {
		return NewInternalServerError(err)
	}
	if experience == nil {
		return NewNotFoundError(experienceId, "id")
	}
	err = s.experienceRepository.Delete(ctx, experience.ID)
	if err != nil {
		return NewInternalServerError(err)
	}
	return nil
}
