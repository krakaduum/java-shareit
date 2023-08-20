package ru.practicum.shareit.user.exception;

public class NullOrEmptyEmailException extends RuntimeException {

    public NullOrEmptyEmailException(String message) {
        super(message);
    }

}
