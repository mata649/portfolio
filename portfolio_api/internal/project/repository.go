package project

import (
	"context"
	"errors"
	"fmt"
	"github.com/mata649/portfolio/portfolio_api/internal/sorting"

	"github.com/google/uuid"
	"gorm.io/gorm"
)

type Repository interface {
	Create(ctx context.Context, project *Project) error
	FindById(ctx context.Context, id uuid.UUID) (*Project, error)
	FindAll(ctx context.Context, sort sorting.Sort) ([]Project, error)
	Update(ctx context.Context, project *Project) error
	Delete(ctx context.Context, id uuid.UUID) error
}

type RepositoryImpl struct {
	db *gorm.DB
}

func NewRepository(db *gorm.DB) Repository {
	return &RepositoryImpl{
		db: db,
	}
}

func (s RepositoryImpl) Create(ctx context.Context, project *Project) error {
	err := s.db.WithContext(ctx).Create(project).Error
	if err != nil {
		return err
	}
	return nil
}

func (s RepositoryImpl) FindById(ctx context.Context, id uuid.UUID) (*Project, error) {
	var project Project
	err := s.db.WithContext(ctx).Preload("Skills").First(&project, id).Error
	if errors.Is(err, gorm.ErrRecordNotFound) {
		return nil, nil
	}
	if err != nil {
		return nil, err
	}
	return &project, nil
}

func (s RepositoryImpl) FindAll(ctx context.Context, sort sorting.Sort) ([]Project, error) {
	var projects []Project
	query := s.db.WithContext(ctx)
	if sort.By != "" {
		query = query.Order(fmt.Sprintf("%s %s", sort.By, sort.Order))
	}
	err := query.Preload("Skills").Find(&projects).Error
	if err != nil {
		return nil, err
	}
	return projects, nil
}

func (s RepositoryImpl) Update(ctx context.Context, project *Project) error {
	tx := s.db.WithContext(ctx).Begin()
	if tx.Error != nil {
		tx.Rollback()
		return tx.Error
	}

	err := tx.Save(project).Error
	if err != nil {
		return err
	}

	err = tx.Model(project).Association("Skills").Replace(project.Skills)
	if err != nil {
		tx.Rollback()
		return err
	}
	err = tx.Commit().Error
	if err != nil {
		return err
	}
	return nil
}

func (s RepositoryImpl) Delete(ctx context.Context, id uuid.UUID) error {
	err := s.db.WithContext(ctx).Delete(&Project{ID: id}).Error
	if err != nil {
		return err
	}
	return nil
}
