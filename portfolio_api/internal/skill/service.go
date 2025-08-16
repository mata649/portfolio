package skill

import (
	"context"
	"github.com/go-playground/validator/v10"
	"github.com/google/uuid"
	"github.com/mata649/portfolio/portfolio_api/internal/errs"
	"gorm.io/gorm"
	"net/http"
	"time"
)

type CreateSkillRequest struct {
	ID   uuid.UUID `json:"id" validate:"required,uuid"`
	Name string    `json:"name" validate:"required,min=2,max=24"`
}

func (s *CreateSkillRequest) Bind(_ *http.Request) error {
	return nil
}

type UpdateSkillRequest struct {
	Name string `json:"name" validate:"required,min=2,max=24"`
}

func (s *UpdateSkillRequest) Bind(_ *http.Request) error {
	return nil
}

type Response struct {
	ID        uuid.UUID      `json:"id"`
	Name      string         `json:"name"`
	CreatedAt time.Time      `json:"createdAt"`
	UpdatedAt time.Time      `json:"updatedAt"`
	DeletedAt gorm.DeletedAt `json:"deletedAt"`
}

func NewResponse(skill *Skill) *Response {
	return &Response{
		ID:        skill.ID,
		Name:      skill.Name,
		CreatedAt: skill.CreatedAt,
		UpdatedAt: skill.UpdatedAt,
		DeletedAt: skill.DeletedAt,
	}
}

var validate = validator.New()

type Service struct {
	skillRepository Repository
}

func NewService(skillRepository Repository) Service {
	return Service{
		skillRepository: skillRepository,
	}
}

func (s Service) Create(ctx context.Context, request *CreateSkillRequest) error {
	err := validate.Struct(request)
	if err != nil {
		requestErrors := errs.GetRequestErrors(err)
		return NewBadRequestError(requestErrors)
	}

	skill := NewSkill(request.ID, request.Name)
	err = s.skillRepository.Create(ctx, skill)
	if err != nil {
		return NewInternalServerError(err)
	}
	return nil
}

func (s Service) FindById(ctx context.Context, id string) (*Response, error) {
	skillId, err := uuid.Parse(id)
	if err != nil {
		return nil, NewNotFoundError(skillId, "id")
	}

	skill, err := s.skillRepository.FindById(ctx, skillId)
	if err != nil {
		return nil, NewInternalServerError(err)
	}
	if skill == nil {
		return nil, NewNotFoundError(skillId, "id")
	}
	return NewResponse(skill), nil
}

func (s Service) FindAll(ctx context.Context) ([]Response, error) {
	skills, err := s.skillRepository.FindAll(ctx)
	if err != nil {
		return nil, NewInternalServerError(err)
	}

	skillsResponse := make([]Response, len(skills))
	for index, skill := range skills {
		skillsResponse[index] = *NewResponse(&skill)
	}
	return skillsResponse, nil
}

func (s Service) Update(ctx context.Context, id string, request *UpdateSkillRequest) error {

	skillId, err := uuid.Parse(id)
	if err != nil {
		return NewNotFoundError(skillId, "id")
	}

	err = validate.Struct(request)
	if err != nil {
		requestErrors := errs.GetRequestErrors(err)
		return NewBadRequestError(requestErrors)
	}

	skill, err := s.skillRepository.FindById(ctx, skillId)
	if err != nil {
		return NewInternalServerError(err)
	}
	if skill == nil {
		return NewNotFoundError(skillId, "id")
	}

	skill.Name = request.Name
	err = s.skillRepository.Update(ctx, skill)
	if err != nil {
		return NewInternalServerError(err)
	}
	return nil
}

func (s Service) Delete(ctx context.Context, id string) error {
	skillId, err := uuid.Parse(id)
	if err != nil {
		return NewNotFoundError(skillId, "id")
	}

	skill, err := s.skillRepository.FindById(ctx, skillId)
	if err != nil {
		return NewInternalServerError(err)
	}
	if skill == nil {
		return NewNotFoundError(skillId, "id")
	}
	err = s.skillRepository.Delete(ctx, skill.ID)
	if err != nil {
		return NewInternalServerError(err)
	}
	return nil
}
