package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor
@Getter
public class ItemRequestDto {

    long id;

    String description;

    LocalDateTime created;

    Set<ItemDto> items;

}
