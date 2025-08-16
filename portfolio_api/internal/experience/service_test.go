package experience

import (
	"context"
	"fmt"
	"github.com/google/uuid"
	"github.com/mata649/portfolio/portfolio_api/internal/errs"
	"github.com/mata649/portfolio/portfolio_api/internal/skill"
	"github.com/mata649/portfolio/portfolio_api/internal/sorting"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/mock"
	"testing"
	"time"
)

type MockExperienceRepo struct {
	mock.Mock
}

func (m *MockExperienceRepo) Create(ctx context.Context, e *Experience) error {
	args := m.Called(ctx, e)
	return args.Error(0)
}

func (m *MockExperienceRepo) FindById(ctx context.Context, id uuid.UUID) (*Experience, error) {
	args := m.Called(ctx, id)
	if e, ok := args.Get(0).(*Experience); ok {
		return e, args.Error(1)
	}
	return nil, args.Error(1)
}

func (m *MockExperienceRepo) FindAll(ctx context.Context, s sorting.Sort) ([]Experience, error) {
	args := m.Called(ctx, s)
	if e, ok := args.Get(0).([]Experience); ok {
		return e, args.Error(1)
	}
	return nil, args.Error(1)
}

func (m *MockExperienceRepo) Update(ctx context.Context, e *Experience) error {
	args := m.Called(ctx, e)
	return args.Error(0)
}

func (m *MockExperienceRepo) Delete(ctx context.Context, id uuid.UUID) error {
	args := m.Called(ctx, id)
	return args.Error(0)
}

// MockSkillRepo is a mock implementation of the skill.Repository interface for testing.
type MockSkillRepo struct {
	mock.Mock
}

func (m *MockSkillRepo) Create(ctx context.Context, s *skill.Skill) error {
	args := m.Called(ctx, s)
	return args.Error(0)
}

func (m *MockSkillRepo) FindById(ctx context.Context, id uuid.UUID) (*skill.Skill, error) {
	args := m.Called(ctx, id)
	if s, ok := args.Get(0).(*skill.Skill); ok {
		return s, args.Error(1)
	}
	return nil, args.Error(1)
}

func (m *MockSkillRepo) FindAll(ctx context.Context) ([]skill.Skill, error) {
	args := m.Called(ctx)
	if s, ok := args.Get(0).([]skill.Skill); ok {
		return s, args.Error(1)
	}
	return nil, args.Error(1)
}

func (m *MockSkillRepo) Update(ctx context.Context, s *skill.Skill) error {
	args := m.Called(ctx, s)
	return args.Error(0)
}

func (m *MockSkillRepo) Delete(ctx context.Context, id uuid.UUID) error {
	args := m.Called(ctx, id)
	return args.Error(0)
}

func (m *MockSkillRepo) FindSkillsByIDs(ctx context.Context, ids []uuid.UUID) ([]skill.Skill, error) {
	args := m.Called(ctx, ids)
	if s, ok := args.Get(0).([]skill.Skill); ok {
		return s, args.Error(1)
	}
	return nil, args.Error(1)
}

func TestService_Create(t *testing.T) {
	id, err := uuid.NewRandom()
	if err != nil {
		t.Fatal(err)
	}
	skillID, err := uuid.NewRandom()
	if err != nil {
		t.Fatal(err)
	}
	startDate := time.Now().AddDate(0, -6, 0)
	endDate := time.Now()

	validRequest := &CreateExperienceRequest{
		ID:           id,
		Position:     "Software Engineer",
		Location:     "New York",
		Company:      "Tech Corp",
		Description:  "Developed cool stuff.",
		StartDate:    startDate,
		EndDate:      endDate,
		IsCurrentJob: false,
		Skills:       []uuid.UUID{skillID},
	}

	experience := NewExperience(validRequest.ID,
		validRequest.Position,
		validRequest.Location,
		validRequest.Company,
		validRequest.Description,
		validRequest.StartDate,
		validRequest.EndDate,
		validRequest.IsCurrentJob,
		[]skill.Skill{})

	tests := []struct {
		name        string
		request     *CreateExperienceRequest
		mockSetup   func(*MockExperienceRepo, *MockSkillRepo)
		expectedErr errs.ServiceError
	}{
		{
			name:        "validation error - missing fields",
			request:     &CreateExperienceRequest{},
			mockSetup:   func(e *MockExperienceRepo, s *MockSkillRepo) {},
			expectedErr: &errs.BadRequestError{},
		},
		{
			name:    "internal server error - FindSkillsByIDs repository error",
			request: validRequest,
			mockSetup: func(e *MockExperienceRepo, s *MockSkillRepo) {
				s.On("FindSkillsByIDs", context.Background(), validRequest.Skills).Return(nil, fmt.Errorf("error finding skills"))
			},
			expectedErr: &errs.InternalServerError{},
		},
		{
			name:    "not found error - skill not found",
			request: validRequest,
			mockSetup: func(e *MockExperienceRepo, s *MockSkillRepo) {
				s.On("FindSkillsByIDs", context.Background(), validRequest.Skills).Return([]skill.Skill{}, nil)
			},
			expectedErr: &errs.NotFoundError{},
		},
		{
			name:    "internal server error - Create repository error",
			request: validRequest,
			mockSetup: func(e *MockExperienceRepo, s *MockSkillRepo) {
				foundSkills := []skill.Skill{{ID: skillID}}
				experience.Skills = foundSkills
				s.On("FindSkillsByIDs", context.Background(), validRequest.Skills).Return(foundSkills, nil)
				e.On("Create", context.Background(), experience).Return(fmt.Errorf("error creating experience"))
			},
			expectedErr: &errs.InternalServerError{},
		},
		{
			name:    "experience created successfully",
			request: validRequest,
			mockSetup: func(e *MockExperienceRepo, s *MockSkillRepo) {
				foundSkills := []skill.Skill{{ID: skillID}}
				experience.Skills = foundSkills
				s.On("FindSkillsByIDs", context.Background(), validRequest.Skills).Return(foundSkills, nil)
				e.On("Create", context.Background(), experience).Return(nil)
			},
			expectedErr: nil,
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			experienceRepo := new(MockExperienceRepo)
			skillRepo := new(MockSkillRepo)
			service := NewService(experienceRepo, skillRepo)
			if tt.mockSetup != nil {
				tt.mockSetup(experienceRepo, skillRepo)
			}
			err := service.Create(context.Background(), tt.request)
			if tt.expectedErr == nil {
				assert.NoError(t, err)
			} else {
				assert.ErrorAs(t, err, &tt.expectedErr)
			}
		})
	}
}

func TestService_FindById(t *testing.T) {
	id, err := uuid.NewRandom()
	if err != nil {
		t.Fatal(err)
	}
	skillID, err := uuid.NewRandom()
	if err != nil {
		t.Fatal(err)
	}
	foundSkills := []skill.Skill{{ID: skillID}}
	experienceFound := NewExperience(id, "Software Engineer", "New York", "Tech Corp", "Developed cool stuff.", time.Now().AddDate(0, -6, 0), time.Now(), false, foundSkills)
	expectedResponse := NewResponse(experienceFound)

	tests := []struct {
		name        string
		id          string
		mockSetup   func(*MockExperienceRepo)
		expectedRes *Response
		expectedErr errs.ServiceError
	}{
		{
			name:        "not found error - invalid id format",
			id:          "invalid-uuid",
			mockSetup:   func(e *MockExperienceRepo) {},
			expectedRes: nil,
			expectedErr: &errs.NotFoundError{},
		},
		{
			name: "internal server error - FindById repository error",
			id:   id.String(),
			mockSetup: func(e *MockExperienceRepo) {
				e.On("FindById", context.Background(), id).Return(nil, fmt.Errorf("error finding experience"))
			},
			expectedRes: nil,
			expectedErr: &errs.InternalServerError{},
		},
		{
			name: "not found error - experience not found",
			id:   id.String(),
			mockSetup: func(e *MockExperienceRepo) {
				e.On("FindById", context.Background(), id).Return(nil, nil)
			},
			expectedRes: nil,
			expectedErr: &errs.NotFoundError{},
		},
		{
			name: "experience was found successfully",
			id:   id.String(),
			mockSetup: func(e *MockExperienceRepo) {
				e.On("FindById", context.Background(), id).Return(experienceFound, nil)
			},
			expectedRes: expectedResponse,
			expectedErr: nil,
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			experienceRepo := new(MockExperienceRepo)
			skillRepo := new(MockSkillRepo)
			service := NewService(experienceRepo, skillRepo)
			if tt.mockSetup != nil {
				tt.mockSetup(experienceRepo)
			}
			response, err := service.FindById(context.Background(), tt.id)
			if tt.expectedErr == nil {
				assert.NoError(t, err)
				assert.Equal(t, tt.expectedRes.ID, response.ID)
				assert.Equal(t, tt.expectedRes.Position, response.Position)
				assert.Equal(t, tt.expectedRes.Location, response.Location)
				assert.Equal(t, tt.expectedRes.Company, response.Company)
				assert.Equal(t, tt.expectedRes.Description, response.Description)
				assert.Equal(t, tt.expectedRes.IsCurrentJob, response.IsCurrentJob)
				assert.Equal(t, len(tt.expectedRes.Skills), len(response.Skills))
			} else {
				assert.ErrorAs(t, err, &tt.expectedErr)
			}
		})
	}
}

func TestService_FindAll(t *testing.T) {
	id1, err := uuid.NewRandom()
	if err != nil {
		t.Fatal(err)
	}
	id2, err := uuid.NewRandom()
	if err != nil {
		t.Fatal(err)
	}
	skillID1, err := uuid.NewRandom()
	if err != nil {
		t.Fatal(err)
	}
	skillID2, err := uuid.NewRandom()
	if err != nil {
		t.Fatal(err)
	}
	experience1 := NewExperience(id1, "Pos 1", "Loc 1", "Comp 1", "Desc 1", time.Now().AddDate(0, -12, 0), time.Now().AddDate(0, -6, 0), false, []skill.Skill{{ID: skillID1}})
	experience2 := NewExperience(id2, "Pos 2", "Loc 2", "Comp 2", "Desc 2", time.Now().AddDate(0, -5, 0), time.Now().AddDate(0, -1, 0), false, []skill.Skill{{ID: skillID2}})
	expectedResponse := []Response{*NewResponse(experience1), *NewResponse(experience2)}

	tests := []struct {
		name        string
		sort        sorting.Sort
		mockSetup   func(*MockExperienceRepo)
		expectedRes []Response
		expectedErr errs.ServiceError
	}{
		{
			name:        "bad request error - invalid sort field",
			sort:        sorting.Sort{By: "invalid_field"},
			mockSetup:   func(e *MockExperienceRepo) {},
			expectedRes: nil,
			expectedErr: &errs.BadRequestError{},
		},
		{
			name: "internal server error - FindAll repository error",
			sort: sorting.Sort{By: "start_date"},
			mockSetup: func(e *MockExperienceRepo) {
				e.On("FindAll", context.Background(), sorting.Sort{By: "start_date"}).Return(nil, fmt.Errorf("error finding experiences"))
			},
			expectedRes: nil,
			expectedErr: &errs.InternalServerError{},
		},
		{
			name: "experiences found successfully",
			sort: sorting.Sort{By: "start_date"},
			mockSetup: func(e *MockExperienceRepo) {
				e.On("FindAll", context.Background(), sorting.Sort{By: "start_date"}).Return([]Experience{*experience1, *experience2}, nil)
			},
			expectedRes: expectedResponse,
			expectedErr: nil,
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			experienceRepo := new(MockExperienceRepo)
			skillRepo := new(MockSkillRepo)
			service := NewService(experienceRepo, skillRepo)
			if tt.mockSetup != nil {
				tt.mockSetup(experienceRepo)
			}
			response, err := service.FindAll(context.Background(), tt.sort)
			if tt.expectedErr == nil {
				assert.NoError(t, err)
				assert.Equal(t, len(tt.expectedRes), len(response))
				assert.Equal(t, tt.expectedRes[0].Position, response[0].Position)
				assert.Equal(t, tt.expectedRes[1].Position, response[1].Position)
			} else {
				assert.ErrorAs(t, err, &tt.expectedErr)
			}
		})
	}
}

func TestService_Update(t *testing.T) {
	id, err := uuid.NewRandom()
	if err != nil {
		t.Fatal(err)
	}
	skillID, err := uuid.NewRandom()
	if err != nil {
		t.Fatal(err)
	}
	startDate := time.Now().AddDate(0, -6, 0)
	endDate := time.Now()

	validRequest := &UpdateExperienceRequest{
		Position:     "Senior Software Engineer",
		Location:     "San Francisco",
		Company:      "Updated Corp",
		Description:  "Updated description.",
		StartDate:    startDate,
		EndDate:      endDate,
		IsCurrentJob: true,
		Skills:       []uuid.UUID{skillID},
	}

	experienceFound := NewExperience(id, "Old Pos", "Old Loc", "Old Comp", "Old Desc", startDate.AddDate(0, -6, 0), endDate.AddDate(0, -6, 0), false, []skill.Skill{})

	tests := []struct {
		name        string
		id          string
		request     *UpdateExperienceRequest
		mockSetup   func(*MockExperienceRepo, *MockSkillRepo)
		expectedErr errs.ServiceError
	}{
		{
			name:        "not found error - invalid id format",
			id:          "invalid-uuid",
			request:     validRequest,
			mockSetup:   func(e *MockExperienceRepo, s *MockSkillRepo) {},
			expectedErr: &errs.NotFoundError{},
		},
		{
			name:        "validation error - missing fields",
			id:          id.String(),
			request:     &UpdateExperienceRequest{},
			mockSetup:   func(e *MockExperienceRepo, s *MockSkillRepo) {},
			expectedErr: &errs.BadRequestError{},
		},
		{
			name:    "internal server error - FindById repository error",
			id:      id.String(),
			request: validRequest,
			mockSetup: func(e *MockExperienceRepo, s *MockSkillRepo) {
				e.On("FindById", context.Background(), id).Return(nil, fmt.Errorf("error finding experience"))
			},
			expectedErr: &errs.InternalServerError{},
		},
		{
			name:    "not found error - experience not found",
			id:      id.String(),
			request: validRequest,
			mockSetup: func(e *MockExperienceRepo, s *MockSkillRepo) {
				e.On("FindById", context.Background(), id).Return(nil, nil)
			},
			expectedErr: &errs.NotFoundError{},
		},
		{
			name:    "internal server error - FindSkillsByIDs repository error",
			id:      id.String(),
			request: validRequest,
			mockSetup: func(e *MockExperienceRepo, s *MockSkillRepo) {
				e.On("FindById", context.Background(), id).Return(experienceFound, nil)
				s.On("FindSkillsByIDs", context.Background(), validRequest.Skills).Return(nil, fmt.Errorf("error finding skills"))
			},
			expectedErr: &errs.InternalServerError{},
		},
		{
			name:    "not found error - skill not found",
			id:      id.String(),
			request: validRequest,
			mockSetup: func(e *MockExperienceRepo, s *MockSkillRepo) {
				e.On("FindById", context.Background(), id).Return(experienceFound, nil)
				s.On("FindSkillsByIDs", context.Background(), validRequest.Skills).Return([]skill.Skill{}, nil)
			},
			expectedErr: &errs.NotFoundError{},
		},
		{
			name:    "internal server error - Update repository error",
			id:      id.String(),
			request: validRequest,
			mockSetup: func(e *MockExperienceRepo, s *MockSkillRepo) {
				foundSkills := []skill.Skill{{ID: skillID}}
				updatedExperience := experienceFound
				updatedExperience.Position = validRequest.Position
				updatedExperience.Location = validRequest.Location
				updatedExperience.Company = validRequest.Company
				updatedExperience.Description = validRequest.Description
				updatedExperience.StartDate = validRequest.StartDate
				updatedExperience.EndDate = validRequest.EndDate
				updatedExperience.IsCurrentJob = validRequest.IsCurrentJob
				updatedExperience.Skills = foundSkills

				e.On("FindById", context.Background(), id).Return(experienceFound, nil)
				s.On("FindSkillsByIDs", context.Background(), validRequest.Skills).Return(foundSkills, nil)
				e.On("Update", context.Background(), updatedExperience).Return(fmt.Errorf("error updating experience"))
			},
			expectedErr: &errs.InternalServerError{},
		},
		{
			name:    "experience was updated successfully",
			id:      id.String(),
			request: validRequest,
			mockSetup: func(e *MockExperienceRepo, s *MockSkillRepo) {
				foundSkills := []skill.Skill{{ID: skillID}}
				updatedExperience := experienceFound
				updatedExperience.Position = validRequest.Position
				updatedExperience.Location = validRequest.Location
				updatedExperience.Company = validRequest.Company
				updatedExperience.Description = validRequest.Description
				updatedExperience.StartDate = validRequest.StartDate
				updatedExperience.EndDate = validRequest.EndDate
				updatedExperience.IsCurrentJob = validRequest.IsCurrentJob
				updatedExperience.Skills = foundSkills

				e.On("FindById", context.Background(), id).Return(experienceFound, nil)
				s.On("FindSkillsByIDs", context.Background(), validRequest.Skills).Return(foundSkills, nil)
				e.On("Update", context.Background(), updatedExperience).Return(nil)
			},
			expectedErr: nil,
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			experienceRepo := new(MockExperienceRepo)
			skillRepo := new(MockSkillRepo)
			service := NewService(experienceRepo, skillRepo)
			if tt.mockSetup != nil {
				tt.mockSetup(experienceRepo, skillRepo)
			}
			err := service.Update(context.Background(), tt.id, tt.request)
			if tt.expectedErr == nil {
				assert.NoError(t, err)
			} else {
				assert.ErrorAs(t, err, &tt.expectedErr)
			}
		})
	}
}

func TestService_Delete(t *testing.T) {
	id, err := uuid.NewRandom()
	if err != nil {
		t.Fatal(err)
	}
	experienceFound := NewExperience(id, "Old Pos", "Old Loc", "Old Comp", "Old Desc", time.Now().AddDate(0, -6, 0), time.Now(), false, []skill.Skill{})

	tests := []struct {
		name        string
		id          string
		mockSetup   func(*MockExperienceRepo)
		expectedErr errs.ServiceError
	}{
		{
			name:        "not found error - invalid id format",
			id:          "invalid-uuid",
			mockSetup:   func(e *MockExperienceRepo) {},
			expectedErr: &errs.NotFoundError{},
		},
		{
			name: "internal server error - FindById repository error",
			id:   id.String(),
			mockSetup: func(e *MockExperienceRepo) {
				e.On("FindById", context.Background(), id).Return(nil, fmt.Errorf("error finding experience"))
			},
			expectedErr: &errs.InternalServerError{},
		},
		{
			name: "not found error - experience not found",
			id:   id.String(),
			mockSetup: func(e *MockExperienceRepo) {
				e.On("FindById", context.Background(), id).Return(nil, nil)
			},
			expectedErr: &errs.NotFoundError{},
		},
		{
			name: "internal server error - Delete repository error",
			id:   id.String(),
			mockSetup: func(e *MockExperienceRepo) {
				e.On("FindById", context.Background(), id).Return(experienceFound, nil)
				e.On("Delete", context.Background(), id).Return(fmt.Errorf("error deleting experience"))
			},
			expectedErr: &errs.InternalServerError{},
		},
		{
			name: "experience was deleted successfully",
			id:   id.String(),
			mockSetup: func(e *MockExperienceRepo) {
				e.On("FindById", context.Background(), id).Return(experienceFound, nil)
				e.On("Delete", context.Background(), id).Return(nil)
			},
			expectedErr: nil,
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			experienceRepo := new(MockExperienceRepo)
			skillRepo := new(MockSkillRepo)
			service := NewService(experienceRepo, skillRepo)
			if tt.mockSetup != nil {
				tt.mockSetup(experienceRepo)
			}
			err := service.Delete(context.Background(), tt.id)
			if tt.expectedErr == nil {
				assert.NoError(t, err)
			} else {
				assert.ErrorAs(t, err, &tt.expectedErr)
			}
		})
	}
}
