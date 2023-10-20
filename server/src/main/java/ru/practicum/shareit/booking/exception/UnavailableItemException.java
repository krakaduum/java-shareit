package ru.practicum.shareit.booking.exception;

public class UnavailableItemException extends RuntimeException {

    public UnavailableItemException(String message) {
        super(message);
    }

}
