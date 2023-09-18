package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.practicum.shareit.booking.dto.BookingShortDto;

import java.util.Collection;

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

    Collection<CommentDto> comments;

}
