package org.example.domain.validators;

public interface Validator<E> {
    void validate(E e) throws ValidationException;
}