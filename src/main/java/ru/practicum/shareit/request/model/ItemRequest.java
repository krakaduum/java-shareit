package ru.practicum.shareit.request.model;

import lombok.Data;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * Запрос вещи. TODO Sprint add-item-requests.
 */
@Data
public class ItemRequest {

    long id;

    String description;

    User requestor;

    LocalDateTime created;

}
