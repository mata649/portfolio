package com.mata649.portfolio.user.exceptions;

import com.mata649.portfolio.shared.exceptions.EntityConflictException;

public class EmailAlreadyTakenException extends EntityConflictException {
    public EmailAlreadyTakenException(String email) {
        super(String.format("The email %s has been already taken", email));
    }
}
