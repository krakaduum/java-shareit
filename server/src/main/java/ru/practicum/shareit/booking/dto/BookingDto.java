package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class BookingDto {

    long id;

    LocalDateTime start;

    LocalDateTime end;

    ItemDto item;

    UserDto booker;

    Status status;

}
