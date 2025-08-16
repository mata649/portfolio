package skill

import (
	"context"
	"fmt"
	"github.com/google/uuid"
	"github.com/mata649/portfolio/portfolio_api/internal/errs"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/mock"
	"testing"
)

type MockSkillRepo struct {
	mock.Mock
}

func (m *MockSkillRepo) Create(ctx context.Context, s *Skill) error {
	args := m.Called(ctx, s)
	return args.Error(0)
}

func (m *MockSkillRepo) FindById(ctx context.Context, id uuid.UUID) (*Skill, error) {
	args := m.Called(ctx, id)
	if s, ok := args.Get(0).(*Skill); ok {
		return s, args.Error(1)
	}
	return nil, args.Error(1)
}

func (m *MockSkillRepo) FindAll(ctx context.Context) ([]Skill, error) {
	args := m.Called(ctx)
	if s, ok := args.Get(0).([]Skill); ok {
		return s, args.Error(1)
	}
	return nil, args.Error(1)
}

func (m *MockSkillRepo) Update(ctx context.Context, s *Skill) error {
	args := m.Called(ctx, s)
	return args.Error(0)
}

func (m *MockSkillRepo) Delete(ctx context.Context, id uuid.UUID) error {
	args := m.Called(ctx, id)
	return args.Error(0)
}

func (m *MockSkillRepo) FindSkillsByIDs(ctx context.Context, ids []uuid.UUID) ([]Skill, error) {
	args := m.Called(ctx, ids)
	if s, ok := args.Get(0).([]Skill); ok {
		return s, args.Error(1)
	}
	return nil, args.Error(1)
}

func TestService_Create(t *testing.T) {
	id, err := uuid.NewRandom()
	if err != nil {
		t.Fatal(err)
	}
	validRequest := &CreateSkillRequest{
		ID:   id,
		Name: "AWS",
	}
	skill := &Skill{
		ID:   validRequest.ID,
		Name: validRequest.Name,
	}
	tests := []struct {
		name        string
		request     *CreateSkillRequest
		mockSetup   func(*MockSkillRepo)
		expectedErr errs.ServiceError
	}{
		{
			name:        "validation error - missing fields",
			request:     &CreateSkillRequest{},
			mockSetup:   func(m *MockSkillRepo) {},
			expectedErr: &errs.BadRequestError{},
		},
		{
			name:    "internal server error - Create repository error",
			request: validRequest,
			mockSetup: func(m *MockSkillRepo) {
				m.On("Create", context.Background(), skill).Return(fmt.Errorf("error creating skill"))
			},
			expectedErr: &errs.InternalServerError{},
		},
		{
			name:    "skill created successfully",
			request: validRequest,
			mockSetup: func(m *MockSkillRepo) {
				m.On("Create", context.Background(), skill).Return(nil)
			},
			expectedErr: nil,
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			repo := new(MockSkillRepo)
			service := NewService(repo)
			if tt.mockSetup != nil {
				tt.mockSetup(repo)
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
	skill := &Skill{
		ID:   id,
		Name: "AWS",
	}
	expectedResponse := &Response{
		ID:   skill.ID,
		Name: skill.Name,
	}
	tests := []struct {
		name        string
		id          string
		mockSetup   func(*MockSkillRepo)
		expectedErr errs.ServiceError
	}{
		{
			name:        "not found error - invalid id format",
			id:          "0as0da0sd01203n12en20e2",
			mockSetup:   func(m *MockSkillRepo) {},
			expectedErr: &errs.NotFoundError{},
		},
		{
			name: "internal server error - FindById repository error",
			id:   id.String(),
			mockSetup: func(m *MockSkillRepo) {
				m.On("FindById", context.Background(), id).Return(nil, fmt.Errorf("error finding skill"))
			},
			expectedErr: &errs.InternalServerError{},
		},
		{
			name: "not found error - skill not found",
			id:   id.String(),
			mockSetup: func(m *MockSkillRepo) {
				m.On("FindById", context.Background(), id).Return(nil, nil)
			},
			expectedErr: &errs.NotFoundError{},
		},
		{
			name: "skill was found successfully",
			id:   id.String(),
			mockSetup: func(m *MockSkillRepo) {
				m.On("FindById", context.Background(), id).Return(skill, nil)
			},
			expectedErr: nil,
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			repo := new(MockSkillRepo)
			service := NewService(repo)
			if tt.mockSetup != nil {
				tt.mockSetup(repo)
			}
			response, err := service.FindById(context.Background(), tt.id)

			if tt.expectedErr == nil {
				assert.NoError(t, err)
				assert.Equal(t, expectedResponse, response)
			} else {
				assert.ErrorAs(t, err, &tt.expectedErr)
			}
		})
	}
}

func TestService_FindAll(t *testing.T) {
	id, err := uuid.NewRandom()
	if err != nil {
		t.Fatal(err)
	}
	id2, err := uuid.NewRandom()
	if err != nil {
		t.Fatal(err)
	}
	skill := NewSkill(id, "AWS")
	skill2 := NewSkill(id2, "Go")
	expectedResponse := []Response{
		{ID: skill.ID, Name: skill.Name},
		{ID: skill2.ID, Name: skill2.Name},
	}
	tests := []struct {
		name        string
		mockSetup   func(*MockSkillRepo)
		expectedErr errs.ServiceError
	}{
		{
			name: "internal server error - FindAll repository error",
			mockSetup: func(m *MockSkillRepo) {
				m.On("FindAll", context.Background()).Return(nil, fmt.Errorf("error finding skills"))
			},
			expectedErr: &errs.InternalServerError{},
		},
		{
			name: "skills found successfully",
			mockSetup: func(m *MockSkillRepo) {
				m.On("FindAll", context.Background()).Return([]Skill{*skill, *skill2}, nil)
			},
			expectedErr: nil,
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			repo := new(MockSkillRepo)
			service := NewService(repo)
			if tt.mockSetup != nil {
				tt.mockSetup(repo)
			}
			response, err := service.FindAll(context.Background())
			if tt.expectedErr == nil {
				assert.NoError(t, err)
				assert.Equal(t, expectedResponse, response)
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
	validRequest := &UpdateSkillRequest{
		Name: "AWS",
	}
	skillFound := NewSkill(id, "AWS")
	tests := []struct {
		name        string
		request     *UpdateSkillRequest
		id          string
		mockSetup   func(*MockSkillRepo)
		expectedErr errs.ServiceError
	}{
		{
			name:        "not found error - invalid id format",
			id:          "asiodn2312oads",
			mockSetup:   func(m *MockSkillRepo) {},
			expectedErr: &errs.NotFoundError{},
		},
		{
			name:        "validation error - missing fields",
			id:          id.String(),
			request:     &UpdateSkillRequest{},
			mockSetup:   func(m *MockSkillRepo) {},
			expectedErr: &errs.BadRequestError{},
		},
		{
			name:    "internal server error - FindById repository error",
			id:      id.String(),
			request: validRequest,
			mockSetup: func(m *MockSkillRepo) {
				m.On("FindById", context.Background(), id).Return(nil, fmt.Errorf("error finding skill"))
			},
			expectedErr: &errs.InternalServerError{},
		},
		{
			name:    "not found error - skill not found",
			id:      id.String(),
			request: validRequest,
			mockSetup: func(m *MockSkillRepo) {
				m.On("FindById", context.Background(), id).Return(nil, nil)
			},
			expectedErr: &errs.NotFoundError{},
		},
		{
			name:    "internal server error - Update repository error",
			id:      id.String(),
			request: validRequest,
			mockSetup: func(m *MockSkillRepo) {
				m.On("FindById", context.Background(), id).Return(skillFound, nil)
				m.On("Update", context.Background(), skillFound).Return(fmt.Errorf("error updating skill"))
			},
			expectedErr: &errs.InternalServerError{},
		},
		{
			name:    "skill was updated successfully",
			id:      id.String(),
			request: validRequest,
			mockSetup: func(m *MockSkillRepo) {
				m.On("FindById", context.Background(), id).Return(skillFound, nil)
				m.On("Update", context.Background(), skillFound).Return(nil)
			},
			expectedErr: nil,
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			repo := new(MockSkillRepo)
			service := NewService(repo)
			if tt.mockSetup != nil {
				tt.mockSetup(repo)
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
	skillFound := NewSkill(id, "AWS")
	tests := []struct {
		name        string
		id          string
		mockSetup   func(*MockSkillRepo)
		expectedErr errs.ServiceError
	}{
		{
			name:        "not found error - invalid id format",
			id:          "asiodn2312oads",
			mockSetup:   func(m *MockSkillRepo) {},
			expectedErr: &errs.NotFoundError{},
		},
		{
			name: "internal server error - FindById repository error",
			id:   id.String(),
			mockSetup: func(m *MockSkillRepo) {
				m.On("FindById", context.Background(), id).Return(nil, fmt.Errorf("error finding skill"))
			},
			expectedErr: &errs.InternalServerError{},
		},
		{
			name: "not found error - skill not found",
			id:   id.String(),
			mockSetup: func(m *MockSkillRepo) {
				m.On("FindById", context.Background(), id).Return(nil, nil)
			},
			expectedErr: &errs.NotFoundError{},
		},
		{
			name: "internal server error - Delete repository error",
			id:   id.String(),
			mockSetup: func(m *MockSkillRepo) {
				m.On("FindById", context.Background(), id).Return(skillFound, nil)
				m.On("Delete", context.Background(), skillFound.ID).Return(fmt.Errorf("error deleting skill"))
			},
			expectedErr: &errs.InternalServerError{},
		},
		{
			name: "skill was deleted successfully",
			id:   id.String(),
			mockSetup: func(m *MockSkillRepo) {
				m.On("FindById", context.Background(), id).Return(skillFound, nil)
				m.On("Delete", context.Background(), skillFound.ID).Return(nil)
			},
			expectedErr: nil,
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			repo := new(MockSkillRepo)
			service := NewService(repo)
			if tt.mockSetup != nil {
				tt.mockSetup(repo)
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
