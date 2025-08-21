package config

import (
	"github.com/caarlos0/env/v6"
	"github.com/joho/godotenv"
	"github.com/labstack/gommon/log"
	"os"
)

type Config struct {
	DbPassword    string `env:"DB_PASSWORD" envDefault:"postgres"`
	DbHost        string `env:"DB_HOST" envDefault:"localhost"`
	DbPort        string `env:"DB_PORT" envDefault:"5432"`
	DbUser        string `env:"DB_USER" envDefault:"postgres"`
	DbName        string `env:"DB_NAME" envDefault:"postgres"`
	WebPort       string `env:"WEB_PORT" envDefault:"3000"`
	JwtSigningKey string `env:"JWT_SIGNING_KEY" envDefault:"This is the default key, please don't use it in production"`
	AdminEmail    string `env:"ADMIN_EMAIL" envDefault:"test@test.com"`
	AdminPassword string `env:"ADMIN_PASSWORD" envDefault:"test1234"`
}

func LoadConfig() *Config {
	_, err := os.Open("./.env")
	if !os.IsNotExist(err) {
		err = godotenv.Load()
		if err != nil {
			log.Fatalf("Error loading .env file %v", err)
		}
	}

	cfg :=
		&Config{}
	err = env.Parse(cfg)
	if err != nil {
		log.Fatalf("Error parsing env vars to config: %+v", err)
	}
	return cfg
}
