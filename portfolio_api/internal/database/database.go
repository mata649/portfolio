package database

import (
	"context"
	"fmt"
	"log/slog"
	"os"

	"github.com/google/uuid"
	"github.com/labstack/gommon/log"
	"github.com/mata649/portfolio/portfolio_api/internal/auth"
	"github.com/mata649/portfolio/portfolio_api/internal/config"
	"github.com/mata649/portfolio/portfolio_api/internal/errs"
	"github.com/mata649/portfolio/portfolio_api/internal/experience"
	"github.com/mata649/portfolio/portfolio_api/internal/jwt"
	"github.com/mata649/portfolio/portfolio_api/internal/project"
	"github.com/mata649/portfolio/portfolio_api/internal/skill"
	"gorm.io/driver/postgres"
	"gorm.io/gorm"
)

func GetDbConnection(cfg *config.Config) *gorm.DB {
	db, err := gorm.Open(postgres.New(postgres.Config{
		DSN: fmt.Sprintf("host=%s port=%s user=%s dbname=%s password=%s sslmode=disable", cfg.DbHost, cfg.DbPort, cfg.DbUser, cfg.DbName, cfg.DbPassword),
	}), &gorm.Config{})
	if err != nil {
		log.Fatalf("Error connecting to the DB: %+v", err)
	}
	return db
}

func ApplyAutoMigrations(db *gorm.DB) {

	err := db.AutoMigrate(&skill.Skill{})
	if err != nil {
		log.Fatalf("Error migrating skills: %+v", err)
	}

	err = db.AutoMigrate(&project.Project{})
	if err != nil {
		log.Fatalf("Error migrating projects: %+v", err)
	}

	err = db.AutoMigrate(&experience.Experience{})
	if err != nil {
		log.Fatalf("Error migrating experiences: %+v", err)
	}
	err = db.AutoMigrate(&auth.User{})
	if err != nil {
		log.Fatalf("Error migrating user: %+v", err)
	}
}

func SetUpAdminAccount(db *gorm.DB, cfg *config.Config) {
	authRepository := auth.NewRepository(db)
	jwtService := jwt.NewService(cfg.JwtSigningKey)
	authService := auth.NewService(authRepository, jwtService)
	randomID, err := uuid.NewRandom()
	if err != nil {
		slog.Error("UUID couldn't be generated")
		os.Exit(1)
	}
	req := &auth.RegisterUserRequest{
		ID:       randomID,
		Email:    cfg.AdminEmail,
		Password: cfg.AdminPassword,
	}
	err = authService.RegisterUser(context.Background(), req)
	if err != nil {
		switch e := err.(type) {
		case errs.InternalServerError:
			slog.Error(e.Error())
			os.Exit(1)
		default:
			slog.Warn(e.Error())
		}
	}
}
