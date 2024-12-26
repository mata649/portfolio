package com.mata649.portfolio.user.exceptions;

import com.mata649.portfolio.shared.exceptions.EntityNotFoundException;

public class UserWithEmailNotFoundException extends EntityNotFoundException {

    public UserWithEmailNotFoundException(String email) {
        super(String.format("The user with email %s was not found", email));
    }
}
