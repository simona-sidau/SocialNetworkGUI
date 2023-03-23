package org.example.domain.validators;

import org.example.domain.User;

public class UserValidator implements Validator<User> {
    @Override
    public void validate(User user) throws ValidationException {
        if( user.getUserName() == null)
            throw new ValidationException("Username can't be null!");
        if( user.getPword() == null)
            throw new  ValidationException("Password can't be null!");
    }
}
