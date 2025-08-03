package experience

import (
	"time"

	"github.com/google/uuid"
	"github.com/mata649/portfolio/portfolio_api/internal/skill"
	"gorm.io/gorm"
)

type Experience struct {
	gorm.Model
	ID           uuid.UUID
	Position     string    `gorm:"size=48"`
	Location     string    `gorm:"size=24"`
	Description  string    `gorm:"size=2048"`
	StartDate    time.Time `gorm:"type=date"`
	EndDate      time.Time `gorm:"type=date"`
	IsCurrentJob bool
	Skills       []skill.Skill `gorm:"many2many:skill_experiences;"`
}

func NewExperience(id uuid.UUID, position string, location string, description string, startDate time.Time, endDate time.Time, isCurrentJob bool, skills []skill.Skill) *Experience {
	return &Experience{
		ID:           id,
		Position:     position,
		Location:     location,
		Description:  description,
		StartDate:    startDate,
		EndDate:      endDate,
		IsCurrentJob: isCurrentJob,
		Skills:       skills,
	}
}
