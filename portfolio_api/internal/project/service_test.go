package project

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
)

// MockProjectRepo is a mock implementation of the Repository interface for testing.
type MockProjectRepo struct {
	mock.Mock
}

func (m *MockProjectRepo) Create(ctx context.Context, p *Project) error {
	args := m.Called(ctx, p)
	return args.Error(0)
}

func (m *MockProjectRepo) FindById(ctx context.Context, id uuid.UUID) (*Project, error) {
	args := m.Called(ctx, id)
	if p, ok := args.Get(0).(*Project); ok {
		return p, args.Error(1)
	}
	return nil, args.Error(1)
}

func (m *MockProjectRepo) FindAll(ctx context.Context, s sorting.Sort) ([]Project, error) {
	args := m.Called(ctx, s)
	if p, ok := args.Get(0).([]Project); ok {
		return p, args.Error(1)
	}
	return nil, args.Error(1)
}

func (m *MockProjectRepo) Update(ctx context.Context, p *Project) error {
	args := m.Called(ctx, p)
	return args.Error(0)
}

func (m *MockProjectRepo) Delete(ctx context.Context, id uuid.UUID) error {
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
	validRequest := &CreateProjectRequest{
		ID:          id,
		Name:        "Test Project",
		Description: "This is a test project.",
		GithubLink:  "https://github.com/test",
		Skills:      []uuid.UUID{skillID},
	}
	validProject := NewProject(validRequest.ID, validRequest.Name, validRequest.Description, validRequest.GithubLink, []skill.Skill{})

	tests := []struct {
		name        string
		request     *CreateProjectRequest
		mockSetup   func(*MockProjectRepo, *MockSkillRepo)
		expectedErr errs.ServiceError
	}{
		{
			name:        "validation error - missing fields",
			request:     &CreateProjectRequest{},
			mockSetup:   func(p *MockProjectRepo, s *MockSkillRepo) {},
			expectedErr: &errs.BadRequestError{},
		},
		{
			name:    "internal server error - FindSkillsByIDs repository error",
			request: validRequest,
			mockSetup: func(p *MockProjectRepo, s *MockSkillRepo) {
				s.On("FindSkillsByIDs", context.Background(), validRequest.Skills).Return(nil, fmt.Errorf("error finding skills"))
			},
			expectedErr: &errs.InternalServerError{},
		},
		{
			name:    "not found error - skill not found",
			request: validRequest,
			mockSetup: func(p *MockProjectRepo, s *MockSkillRepo) {
				s.On("FindSkillsByIDs", context.Background(), validRequest.Skills).Return([]skill.Skill{}, nil)
			},
			expectedErr: &errs.NotFoundError{},
		},
		{
			name:    "internal server error - Create repository error",
			request: validRequest,
			mockSetup: func(p *MockProjectRepo, s *MockSkillRepo) {
				foundSkills := []skill.Skill{{ID: skillID}}
				validProject.Skills = foundSkills // Assign skills to the project to match the call
				s.On("FindSkillsByIDs", context.Background(), validRequest.Skills).Return(foundSkills, nil)
				p.On("Create", context.Background(), validProject).Return(fmt.Errorf("error creating project"))
			},
			expectedErr: &errs.InternalServerError{},
		},
		{
			name:    "project created successfully",
			request: validRequest,
			mockSetup: func(p *MockProjectRepo, s *MockSkillRepo) {
				foundSkills := []skill.Skill{{ID: skillID}}
				validProject.Skills = foundSkills
				s.On("FindSkillsByIDs", context.Background(), validRequest.Skills).Return(foundSkills, nil)
				p.On("Create", context.Background(), validProject).Return(nil)
			},
			expectedErr: nil,
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			projectRepo := new(MockProjectRepo)
			skillRepo := new(MockSkillRepo)
			service := NewService(projectRepo, skillRepo)
			if tt.mockSetup != nil {
				tt.mockSetup(projectRepo, skillRepo)
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
	projectFound := NewProject(id, "Test Project", "This is a test project.", "https://github.com/test", foundSkills)
	expectedResponse := NewResponse(projectFound)

	tests := []struct {
		name        string
		id          string
		mockSetup   func(*MockProjectRepo)
		expectedRes *Response
		expectedErr errs.ServiceError
	}{
		{
			name:        "not found error - invalid id format",
			id:          "invalid-uuid",
			mockSetup:   func(p *MockProjectRepo) {},
			expectedRes: nil,
			expectedErr: &errs.NotFoundError{},
		},
		{
			name: "internal server error - FindById repository error",
			id:   id.String(),
			mockSetup: func(p *MockProjectRepo) {
				p.On("FindById", context.Background(), id).Return(nil, fmt.Errorf("error finding project"))
			},
			expectedRes: nil,
			expectedErr: &errs.InternalServerError{},
		},
		{
			name: "not found error - project not found",
			id:   id.String(),
			mockSetup: func(p *MockProjectRepo) {
				p.On("FindById", context.Background(), id).Return(nil, nil)
			},
			expectedRes: nil,
			expectedErr: &errs.NotFoundError{},
		},
		{
			name: "project was found successfully",
			id:   id.String(),
			mockSetup: func(p *MockProjectRepo) {
				p.On("FindById", context.Background(), id).Return(projectFound, nil)
			},
			expectedRes: expectedResponse,
			expectedErr: nil,
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			projectRepo := new(MockProjectRepo)
			skillRepo := new(MockSkillRepo) // Need skill repo even if not used in this function to create the service
			service := NewService(projectRepo, skillRepo)
			if tt.mockSetup != nil {
				tt.mockSetup(projectRepo)
			}
			response, err := service.FindById(context.Background(), tt.id)
			if tt.expectedErr == nil {
				assert.NoError(t, err)
				assert.Equal(t, tt.expectedRes.ID, response.ID)
				assert.Equal(t, tt.expectedRes.Name, response.Name)
				assert.Equal(t, tt.expectedRes.Description, response.Description)
				assert.Equal(t, tt.expectedRes.GithubLink, response.GithubLink)
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
	project1 := NewProject(id1, "Project 1", "Desc 1", "https://github.com/proj1", []skill.Skill{{ID: skillID1}})
	project2 := NewProject(id2, "Project 2", "Desc 2", "https://github.com/proj2", []skill.Skill{{ID: skillID2}})
	expectedResponse := []Response{*NewResponse(project1), *NewResponse(project2)}

	tests := []struct {
		name        string
		sort        sorting.Sort
		mockSetup   func(*MockProjectRepo)
		expectedRes []Response
		expectedErr errs.ServiceError
	}{
		{
			name:        "bad request error - invalid sort field",
			sort:        sorting.Sort{By: "invalid_field"},
			mockSetup:   func(p *MockProjectRepo) {},
			expectedRes: nil,
			expectedErr: &errs.BadRequestError{},
		},
		{
			name: "internal server error - FindAll repository error",
			sort: sorting.Sort{By: "name"},
			mockSetup: func(p *MockProjectRepo) {
				p.On("FindAll", context.Background(), sorting.Sort{By: "name"}).Return(nil, fmt.Errorf("error finding projects"))
			},
			expectedRes: nil,
			expectedErr: &errs.InternalServerError{},
		},
		{
			name: "projects found successfully",
			sort: sorting.Sort{By: "name"},
			mockSetup: func(p *MockProjectRepo) {
				p.On("FindAll", context.Background(), sorting.Sort{By: "name"}).Return([]Project{*project1, *project2}, nil)
			},
			expectedRes: expectedResponse,
			expectedErr: nil,
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			projectRepo := new(MockProjectRepo)
			skillRepo := new(MockSkillRepo)
			service := NewService(projectRepo, skillRepo)
			if tt.mockSetup != nil {
				tt.mockSetup(projectRepo)
			}
			response, err := service.FindAll(context.Background(), tt.sort)
			if tt.expectedErr == nil {
				assert.NoError(t, err)
				assert.Equal(t, len(tt.expectedRes), len(response))
				assert.Equal(t, tt.expectedRes[0].Name, response[0].Name)
				assert.Equal(t, tt.expectedRes[1].Name, response[1].Name)
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
	validRequest := &UpdateProjectRequest{
		Name:        "Updated Project",
		Description: "Updated description.",
		GithubLink:  "https://github.com/updated",
		Skills:      []uuid.UUID{skillID},
	}
	projectFound := NewProject(id, "Old Name", "Old Desc", "https://github.com/old", []skill.Skill{})

	tests := []struct {
		name        string
		id          string
		request     *UpdateProjectRequest
		mockSetup   func(*MockProjectRepo, *MockSkillRepo)
		expectedErr errs.ServiceError
	}{
		{
			name:        "not found error - invalid id format",
			id:          "invalid-uuid",
			request:     validRequest,
			mockSetup:   func(p *MockProjectRepo, s *MockSkillRepo) {},
			expectedErr: &errs.NotFoundError{},
		},
		{
			name:        "validation error - missing fields",
			id:          id.String(),
			request:     &UpdateProjectRequest{},
			mockSetup:   func(p *MockProjectRepo, s *MockSkillRepo) {},
			expectedErr: &errs.BadRequestError{},
		},
		{
			name:    "internal server error - FindById repository error",
			id:      id.String(),
			request: validRequest,
			mockSetup: func(p *MockProjectRepo, s *MockSkillRepo) {
				p.On("FindById", context.Background(), id).Return(nil, fmt.Errorf("error finding project"))
			},
			expectedErr: &errs.InternalServerError{},
		},
		{
			name:    "not found error - project not found",
			id:      id.String(),
			request: validRequest,
			mockSetup: func(p *MockProjectRepo, s *MockSkillRepo) {
				p.On("FindById", context.Background(), id).Return(nil, nil)
			},
			expectedErr: &errs.NotFoundError{},
		},
		{
			name:    "internal server error - FindSkillsByIDs repository error",
			id:      id.String(),
			request: validRequest,
			mockSetup: func(p *MockProjectRepo, s *MockSkillRepo) {
				p.On("FindById", context.Background(), id).Return(projectFound, nil)
				s.On("FindSkillsByIDs", context.Background(), validRequest.Skills).Return(nil, fmt.Errorf("error finding skills"))
			},
			expectedErr: &errs.InternalServerError{},
		},
		{
			name:    "not found error - skill not found",
			id:      id.String(),
			request: validRequest,
			mockSetup: func(p *MockProjectRepo, s *MockSkillRepo) {
				p.On("FindById", context.Background(), id).Return(projectFound, nil)
				s.On("FindSkillsByIDs", context.Background(), validRequest.Skills).Return([]skill.Skill{}, nil)
			},
			expectedErr: &errs.NotFoundError{},
		},
		{
			name:    "internal server error - Update repository error",
			id:      id.String(),
			request: validRequest,
			mockSetup: func(p *MockProjectRepo, s *MockSkillRepo) {
				foundSkills := []skill.Skill{{ID: skillID}}
				updatedProject := projectFound // a copy of the found project
				updatedProject.Name = validRequest.Name
				updatedProject.Description = validRequest.Description
				updatedProject.GithubLink = validRequest.GithubLink
				updatedProject.Skills = foundSkills
				p.On("FindById", context.Background(), id).Return(projectFound, nil)
				s.On("FindSkillsByIDs", context.Background(), validRequest.Skills).Return(foundSkills, nil)
				p.On("Update", context.Background(), updatedProject).Return(fmt.Errorf("error updating project"))
			},
			expectedErr: &errs.InternalServerError{},
		},
		{
			name:    "project was updated successfully",
			id:      id.String(),
			request: validRequest,
			mockSetup: func(p *MockProjectRepo, s *MockSkillRepo) {
				foundSkills := []skill.Skill{{ID: skillID}}
				updatedProject := projectFound
				updatedProject.Name = validRequest.Name
				updatedProject.Description = validRequest.Description
				updatedProject.GithubLink = validRequest.GithubLink
				updatedProject.Skills = foundSkills
				p.On("FindById", context.Background(), id).Return(projectFound, nil)
				s.On("FindSkillsByIDs", context.Background(), validRequest.Skills).Return(foundSkills, nil)
				p.On("Update", context.Background(), updatedProject).Return(nil)
			},
			expectedErr: nil,
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			projectRepo := new(MockProjectRepo)
			skillRepo := new(MockSkillRepo)
			service := NewService(projectRepo, skillRepo)
			if tt.mockSetup != nil {
				tt.mockSetup(projectRepo, skillRepo)
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
	projectFound := NewProject(id, "Test Project", "This is a test project.", "https://github.com/test", []skill.Skill{})

	tests := []struct {
		name        string
		id          string
		mockSetup   func(*MockProjectRepo)
		expectedErr errs.ServiceError
	}{
		{
			name:        "not found error - invalid id format",
			id:          "invalid-uuid",
			mockSetup:   func(p *MockProjectRepo) {},
			expectedErr: &errs.NotFoundError{},
		},
		{
			name: "internal server error - FindById repository error",
			id:   id.String(),
			mockSetup: func(p *MockProjectRepo) {
				p.On("FindById", context.Background(), id).Return(nil, fmt.Errorf("error finding project"))
			},
			expectedErr: &errs.InternalServerError{},
		},
		{
			name: "not found error - project not found",
			id:   id.String(),
			mockSetup: func(p *MockProjectRepo) {
				p.On("FindById", context.Background(), id).Return(nil, nil)
			},
			expectedErr: &errs.NotFoundError{},
		},
		{
			name: "internal server error - Delete repository error",
			id:   id.String(),
			mockSetup: func(p *MockProjectRepo) {
				p.On("FindById", context.Background(), id).Return(projectFound, nil)
				p.On("Delete", context.Background(), id).Return(fmt.Errorf("error deleting project"))
			},
			expectedErr: &errs.InternalServerError{},
		},
		{
			name: "project was deleted successfully",
			id:   id.String(),
			mockSetup: func(p *MockProjectRepo) {
				p.On("FindById", context.Background(), id).Return(projectFound, nil)
				p.On("Delete", context.Background(), id).Return(nil)
			},
			expectedErr: nil,
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			projectRepo := new(MockProjectRepo)
			skillRepo := new(MockSkillRepo)
			service := NewService(projectRepo, skillRepo)
			if tt.mockSetup != nil {
				tt.mockSetup(projectRepo)
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
