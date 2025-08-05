package main

import (
	"github.com/labstack/gommon/log"
	"github.com/mata649/portfolio/portfolio_api/internal/config"
	"github.com/mata649/portfolio/portfolio_api/internal/database"
	"github.com/mata649/portfolio/portfolio_api/internal/server"
)

func main() {
	cfg := config.LoadConfig()
	db := database.GetDbConnection(cfg)
	database.ApplyAutoMigrations(db)
	database.SetUpAdminAccount(db, cfg)
	serv := server.NewServer(db, cfg)
	err := serv.RunServer(cfg)
	if err != nil {
		log.Fatalf("Error starting server: %+v", err)
	}
}
