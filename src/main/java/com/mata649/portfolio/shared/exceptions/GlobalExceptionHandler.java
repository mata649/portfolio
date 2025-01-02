package com.mata649.portfolio.shared.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exc,
                                                                           ServletWebRequest request) {
        List<FieldError> errors = exc.getFieldErrors()
                .stream()
                .map(fieldError -> new FieldError(fieldError.getField(), fieldError.getDefaultMessage()))
                .toList();
        return new ResponseEntity<>(
                ApiError.builder().message(errors)
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .path(request.getRequest().getRequestURL().toString())
                        .method(request.getRequest().getMethod())
                        .build(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException exc,
                                                                           ServletWebRequest request) {
        return new ResponseEntity<>(
                ApiError.builder().message("the request body is required")
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .path(request.getRequest().getRequestURL().toString())
                        .method(request.getRequest().getMethod())
                        .build(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<ApiError> badCredentialsExceptionHandler(InternalAuthenticationServiceException exc, ServletWebRequest request) {
        return new ResponseEntity<>(
                ApiError.builder()
                        .message("Invalid credentials")
                        .statusCode(HttpStatus.UNAUTHORIZED.value())
                        .path(request.getRequest().getRequestURL().toString())
                        .method(request.getRequest().getMethod())
                        .build(),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> badCredentialsExceptionHandler(BadCredentialsException exc, ServletWebRequest request) {
        return new ResponseEntity<>(
                ApiError.builder()
                        .message("Invalid credentials")
                        .statusCode(HttpStatus.UNAUTHORIZED.value())
                        .path(request.getRequest().getRequestURL().toString())
                        .method(request.getRequest().getMethod())
                        .build(),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(EntityConflictException.class)
    public ResponseEntity<ApiError> entityConflictExceptionHandler(EntityConflictException exc, ServletWebRequest request) {
        return new ResponseEntity<>(
                ApiError.builder()
                        .message(exc.getMessage())
                        .statusCode(HttpStatus.CONFLICT.value())
                        .path(request.getRequest().getRequestURL().toString())
                        .method(request.getRequest().getMethod())
                        .build(),
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> entityNotFoundExceptionHandler(EntityNotFoundException exc, ServletWebRequest request) {
        return new ResponseEntity<>(
                ApiError.builder()
                        .message(exc.getMessage())
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .path(request.getRequest().getRequestURL().toString())
                        .method(request.getRequest().getMethod())
                        .build(),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> badRequestExceptionHandler(BadRequestException exc, ServletWebRequest request) {
        return new ResponseEntity<>(
                ApiError.builder().message(exc.getFieldErrors())
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .path(request.getRequest().getRequestURL().toString())
                        .method(request.getRequest().getMethod())
                        .build(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiError> runtimeExceptionHandler(RuntimeException ex, ServletWebRequest request) {
        return new ResponseEntity<>(
                ApiError.builder()
                        .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .path(request.getRequest().getRequestURL().toString())
                        .method(request.getRequest().getMethod())
                        .build(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }


}