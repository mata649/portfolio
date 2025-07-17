package database

import (
	"fmt"
	"github.com/labstack/gommon/log"
	"github.com/mata649/portfolio/portfolio_api/internal/config"
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
		log.Fatalf("%+v", err)
	}
}
