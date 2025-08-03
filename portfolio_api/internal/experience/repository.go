package experience



import (
	"context"
	"errors"

	"github.com/google/uuid"
	"gorm.io/gorm"
)

type Repository interface {
	Create(ctx context.Context, experience *Experience) error
	FindById(ctx context.Context, id uuid.UUID) (*Experience, error)
	FindAll(ctx context.Context) ([]Experience, error)
	Update(ctx context.Context, experience *Experience) error
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

func (s RepositoryImpl) Create(ctx context.Context, experience *Experience) error {
	err := s.db.WithContext(ctx).Create(experience).Error
	if err != nil {
		return err
	}
	return nil
}

func (s RepositoryImpl) FindById(ctx context.Context, id uuid.UUID) (*Experience, error) {
	var experience *Experience
	err := s.db.WithContext(ctx).Preload("Skills").First(&experience, id).Error
	if errors.Is(err, gorm.ErrRecordNotFound) {
		return nil, nil
	}
	if err != nil {
		return nil, err
	}
	return experience, nil
}

func (s RepositoryImpl) FindAll(ctx context.Context) ([]Experience, error) {
	var experiences []Experience
	err := s.db.WithContext(ctx).Preload("Skills").Find(&experiences).Error
	if err != nil {
		return nil, err
	}
	return experiences, nil
}

func (s RepositoryImpl) Update(ctx context.Context, experience *Experience) error {
	err := s.db.WithContext(ctx).Save(experience).Error
	if err != nil {
		return err
	}
	return nil
}

func (s RepositoryImpl) Delete(ctx context.Context, id uuid.UUID) error {
	err := s.db.WithContext(ctx).Delete(&Experience{ID: id}).Error
	if err != nil {
		return err
	}
	return nil
}
