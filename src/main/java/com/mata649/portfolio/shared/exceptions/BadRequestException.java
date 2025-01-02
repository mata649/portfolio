package com.mata649.portfolio.shared.exceptions;

import java.util.ArrayList;
import java.util.List;

public class BadRequestException extends RuntimeException {
    private List<FieldError> fieldErrors = new ArrayList<>();

    public List<FieldError> getFieldErrors() {
        return fieldErrors;
    }

    public BadRequestException(String field, String message) {
        super(String.format("%s: %s", field, message));
        fieldErrors.add(new FieldError(field, message));
    }

    public BadRequestException(FieldError error) {
        super(String.format("%s: %s", error.getField(), error.getError()));
        fieldErrors.add(error);
    }

    public BadRequestException(List<FieldError> errors) {

        super(generateMessageError(errors));
        fieldErrors = errors;
    }

    private static String generateMessageError(List<FieldError> errors) {
        StringBuilder sb = new StringBuilder();
        for (var error : errors) {
            sb.append(String.format("%s: %s", error.getField(), error.getError()));
            if (!error.equals(errors.getLast())) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }


}
