package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ItemDto {

    long id;

    String name;

    String description;

    Boolean available;

    long ownerId;

}
