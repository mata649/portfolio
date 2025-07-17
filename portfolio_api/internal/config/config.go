package config

import (
	"github.com/caarlos0/env/v6"
	"github.com/joho/godotenv"
	"github.com/labstack/gommon/log"
)

type Config struct {
	DbPassword string `env:"DB_PASSWORD" envDefault:"postgres"`
	DbHost     string `env:"DB_HOST" envDefault:"localhost"`
	DbPort     string `env:"DB_PORT" envDefault:"5432"`
	DbUser     string `env:"DB_USER" envDefault:"postgres"`
	DbName     string `env:"DB_NAME" envDefault:"postgres"`
	WebPort    string `env:"WEB_PORT" envDefault:"3000"`
}

func LoadConfig() *Config {
	err := godotenv.Load()

	if err != nil {
		log.Fatalf("Error loading .env file %v", err)
	}
	cfg :=
		&Config{}
	err = env.Parse(cfg)
	if err != nil {
		log.Fatalf("Error parsing env vars to config: %+v", err)
	}
	return cfg
}
