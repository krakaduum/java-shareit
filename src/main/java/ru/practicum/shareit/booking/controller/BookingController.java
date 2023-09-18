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
    public BookingDto addBooking(@RequestHeader(value = "X-Sharer-User-Id") long userId,
                                 @RequestBody BookingRequestBody bookingRequestBody) {
        return bookingService.addBooking(userId, bookingRequestBody);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(@RequestHeader(value = "X-Sharer-User-Id") long userId,
                                 @PathVariable long bookingId) {
        return bookingService.getBooking(userId, bookingId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto updateBooking(@RequestHeader(value = "X-Sharer-User-Id") long userId,
                                    @PathVariable long bookingId,
                                    @RequestParam boolean approved) {
        return bookingService.updateBooking(userId, bookingId, approved);
    }

    @GetMapping
    Collection<BookingDto> getBookings(@RequestHeader(value = "X-Sharer-User-Id") long userId,
                                       @RequestParam(defaultValue = "ALL", required = false) String state) {
        return bookingService.getBookings(userId, state);
    }

    @GetMapping("/owner")
    Collection<BookingDto> getBookingsByItemOwner(@RequestHeader(value = "X-Sharer-User-Id") long userId,
                                                  @RequestParam(defaultValue = "ALL", required = false) String state) {
        return bookingService.getBookingsByItemOwner(userId, state);
    }

}
