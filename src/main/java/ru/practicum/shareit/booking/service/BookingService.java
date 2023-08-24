package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestBody;

import java.util.Collection;

public interface BookingService {

    BookingDto addBooking(long bookerId, BookingRequestBody bookingRequestBody);

    BookingDto getBooking(long userId, long bookingId);

    BookingDto updateBooking(long ownerId, long bookingId, boolean approved);

    Collection<BookingDto> getBookings(long bookerId, String state);

    Collection<BookingDto> getBookingsByItemOwner(long ownerId, String state);

}
