package auth

import "github.com/google/uuid"

type User struct {
	ID       uuid.UUID
	Email    string `gorm:"size=32"`
	Password string `gorm:"size=64"`
}

func NewUser(id uuid.UUID, email, password string) *User {
	return &User{
		ID:       id,
		Email:    email,
		Password: password,
	}
}
