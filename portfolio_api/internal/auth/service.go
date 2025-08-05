package auth

import (
	"context"
	"net/http"

	"github.com/go-playground/validator/v10"
	"github.com/google/uuid"
	"github.com/mata649/portfolio/portfolio_api/internal/errs"
	"github.com/mata649/portfolio/portfolio_api/internal/jwt"
	"golang.org/x/crypto/bcrypt"
)

type RegisterUserRequest struct {
	ID       uuid.UUID `json:"id" validate:"required,uuid"`
	Email    string    `json:"name" validate:"required,min=2,max=32,email"`
	Password string    `json:"password" validate:"required,min=8,max=64"`
}

func (s *RegisterUserRequest) Bind(_ *http.Request) error {
	return nil
}

type LoginUserRequest struct {
	Email    string `json:"email" validate:"required,min=2,max=32,email"`
	Password string `json:"password" validate:"required,min=1,max=64"`
}

func (s *LoginUserRequest) Bind(_ *http.Request) error {
	return nil
}

type LoginResponse struct {
	Token string `json:"token"`
}

func NewLoginResponse(token string) *LoginResponse {
	return &LoginResponse{
		Token: token,
	}
}

var validate = validator.New()

type Service struct {
	authRepository Repository
	jwtService     jwt.Service
}

func NewService(authRepository Repository, jwtService jwt.Service) Service {

	return Service{
		authRepository: authRepository,
		jwtService:     jwtService,
	}
}

func (s Service) RegisterUser(ctx context.Context, request *RegisterUserRequest) error {
	err := validate.Struct(request)
	if err != nil {
		requestErrors := errs.GetRequestErrors(err)
		return NewBadRequestError(requestErrors)
	}
	userFound, err := s.authRepository.FindUserByEmail(ctx, request.Email)
	if err != nil {
		return NewInternalServerError(err)
	}
	if userFound != nil {
		return NewEmailAlreadyTakenError(request.Email)
	}
	hashedPassword, err := hashPassword(request.Password)
	if err != nil {
		return NewInternalServerError(err)
	}
	user := NewUser(request.ID, request.Email, hashedPassword)
	err = s.authRepository.CreateUser(ctx, user)
	if err != nil {
		return NewInternalServerError(err)
	}
	return nil
}

func (s Service) LoginUser(ctx context.Context, request *LoginUserRequest) (*LoginResponse, error) {
	userFound, err := s.authRepository.FindUserByEmail(ctx, request.Email)
	if err != nil {
		return nil, NewInternalServerError(err)
	}
	if userFound == nil {
		return nil, NewInvalidCredentialsError()
	}
	if !verifyPassword(request.Password, userFound.Password) {
		return nil, NewInvalidCredentialsError()
	}
	token, err := s.jwtService.CreateToken(userFound.Email)
	if err != nil {
		return nil, NewInternalServerError(err)
	}

	return NewLoginResponse(token), nil
}

func hashPassword(password string) (string, error) {
	bytes, err := bcrypt.GenerateFromPassword([]byte(password), 14)
	return string(bytes), err
}

func verifyPassword(password, hash string) bool {
	err := bcrypt.CompareHashAndPassword([]byte(hash), []byte(password))
	return err == nil
}
