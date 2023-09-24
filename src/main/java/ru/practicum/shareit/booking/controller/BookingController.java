package ru.practicum.shareit.booking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestBody;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.Collection;

@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingDto addBooking(@RequestHeader(value = "X-Sharer-User-Id") long bookerId,
                                 @RequestBody BookingRequestBody bookingRequestBody) {
        return bookingService.addBooking(bookerId, bookingRequestBody);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(@RequestHeader(value = "X-Sharer-User-Id") long userId,
                                 @PathVariable long bookingId) {
        return bookingService.getBooking(userId, bookingId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto updateBooking(@RequestHeader(value = "X-Sharer-User-Id") long ownerId,
                                    @PathVariable long bookingId,
                                    @RequestParam boolean approved) {
        return bookingService.updateBooking(ownerId, bookingId, approved);
    }

    @GetMapping
    Collection<BookingDto> getBookings(@RequestHeader(value = "X-Sharer-User-Id") long bookerId,
                                       @RequestParam(defaultValue = "ALL", required = false) String state,
                                       @RequestParam(required = false) Integer from,
                                       @RequestParam(required = false) Integer size) {
        return bookingService.getBookings(bookerId, state, from, size);
    }

    @GetMapping("/owner")
    Collection<BookingDto> getBookingsByItemOwner(@RequestHeader(value = "X-Sharer-User-Id") long ownerId,
                                                  @RequestParam(defaultValue = "ALL", required = false) String state,
                                                  @RequestParam(required = false) Integer from,
                                                  @RequestParam(required = false) Integer size) {
        return bookingService.getBookingsByItemOwner(ownerId, state, from, size);
    }

}
