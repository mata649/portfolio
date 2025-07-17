package skill

import (
	"github.com/google/uuid"
	"gorm.io/gorm"
)

type Skill struct {
	gorm.Model
	ID   uuid.UUID
	Name string
}

func NewSkill(id uuid.UUID, name string) *Skill {
	return &Skill{ID: id, Name: name}
}
