package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestBody;

public interface BookingService {

    BookingDto addBooking(long bookerId, BookingRequestBody bookingRequestBody);

    BookingDto getBooking(long userId, long bookingId);

    BookingDto updateBooking(long ownerId, long bookingId, boolean approved);

}
