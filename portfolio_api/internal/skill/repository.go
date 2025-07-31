package skill

import (
	"context"
	"errors"
	"github.com/google/uuid"
	"gorm.io/gorm"
)

type Repository interface {
	Create(ctx context.Context, skill *Skill) error
	FindById(ctx context.Context, id uuid.UUID) (*Skill, error)
	FindAll(ctx context.Context) ([]Skill, error)
	Update(ctx context.Context, skill *Skill) error
	Delete(ctx context.Context, id uuid.UUID) error
	FindSkillsByIDs(ctx context.Context, ids []uuid.UUID) ([]*Skill, error)
}

type RepositoryImpl struct {
	db *gorm.DB
}

func NewRepository(db *gorm.DB) Repository {
	return &RepositoryImpl{
		db: db,
	}
}

func (s RepositoryImpl) Create(ctx context.Context, skill *Skill) error {
	err := s.db.WithContext(ctx).Create(skill).Error
	if err != nil {
		return err
	}
	return nil
}

func (s RepositoryImpl) FindById(ctx context.Context, id uuid.UUID) (*Skill, error) {
	var skill *Skill
	err := s.db.WithContext(ctx).First(&skill, id).Error
	if errors.Is(err, gorm.ErrRecordNotFound) {
		return nil, nil
	}
	if err != nil {
		return nil, err
	}
	return skill, nil
}

func (s RepositoryImpl) FindAll(ctx context.Context) ([]Skill, error) {
	var skills []Skill
	err := s.db.WithContext(ctx).Find(&skills).Error
	if err != nil {
		return nil, err
	}
	return skills, nil
}

func (s RepositoryImpl) Update(ctx context.Context, skill *Skill) error {
	err := s.db.WithContext(ctx).Save(skill).Error
	if err != nil {
		return err
	}
	return nil
}

func (s RepositoryImpl) Delete(ctx context.Context, id uuid.UUID) error {
	err := s.db.WithContext(ctx).Delete(&Skill{ID: id}).Error
	if err != nil {
		return err
	}
	return nil
}

func (s RepositoryImpl) FindSkillsByIDs(ctx context.Context, ids []uuid.UUID) ([]*Skill, error) {
	var skills []*Skill

	return skills, nil
}
