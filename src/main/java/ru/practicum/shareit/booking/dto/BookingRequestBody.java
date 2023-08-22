package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class BookingRequestBody {

    long itemId;

    LocalDateTime start;

    LocalDateTime end;

}
