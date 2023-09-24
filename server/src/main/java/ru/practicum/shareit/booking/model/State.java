package ru.practicum.shareit.booking.model;

/**
 * Состояние бронирования.
 */
public enum State {
    /**
     * Все.
     */
    ALL,

    /**
     * Текущие.
     */
    CURRENT,

    /**
     * Завершённые.
     */
    PAST,

    /**
     * Будущие.
     */
    FUTURE,

    /**
     * Ожидающие подтверждения.
     */
    WAITING,

    /**
     * Отклонённые.
     */
    REJECTED
}
