package jwt

import (
	"errors"
	"time"

	"github.com/golang-jwt/jwt"
)

type Service struct {
	signingKey []byte
}

func NewService(signingKey string) Service {
	return Service{
		signingKey: []byte(signingKey),
	}
}

func (j Service) CreateToken(email string) (string, error) {
	token := jwt.NewWithClaims(jwt.SigningMethodHS256, jwt.MapClaims{
		"email": "email",
		"exp":   time.Now().Add(time.Hour * 24).Unix(),
	})
	tokenString, err := token.SignedString(j.signingKey)
	if err != nil {
		return "", err
	}
	return tokenString, nil
}

func (j Service) VerifyToken(tokenString string) error {
	token, err := jwt.Parse(tokenString, func(token *jwt.Token) (any, error) {
		return j.signingKey, nil
	})

	if err != nil {
		return err
	}

	if !token.Valid {
		return errors.New("The token is not valid")
	}

	return nil
}
