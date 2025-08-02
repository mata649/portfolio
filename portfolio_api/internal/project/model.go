package project

import (
	"github.com/google/uuid"
	"github.com/mata649/portfolio/portfolio_api/internal/skill"
	"gorm.io/gorm"
)

type Project struct {
	gorm.Model
	ID          uuid.UUID
	Name        string         `gorm:"size=24"`
	Description string         `gorm:"size=1024"`
	GithubLink  string         `gorm:"size=64"`
	Skills      []skill.Skill `gorm:"many2many:skill_projects;"`
}

func NewProject(id uuid.UUID, name string, description string, githubLink string, skills []skill.Skill) *Project {
	return &Project{
		ID:          id,
		Name:        name,
		Description: description,
		GithubLink:  githubLink,
		Skills:      skills,
	}
}
