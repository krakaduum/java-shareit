package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@AllArgsConstructor
public class ItemRequestDto {
    long id;
    String description;
    User requestor;
    LocalDateTime created;
}
