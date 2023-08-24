package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.practicum.shareit.booking.dto.BookingShortDto;

@AllArgsConstructor
@Getter
public class ItemExtendedDto {

    long id;

    String name;

    String description;

    boolean available;

    long ownerId;

    BookingShortDto lastBooking;

    BookingShortDto nextBooking;

}
