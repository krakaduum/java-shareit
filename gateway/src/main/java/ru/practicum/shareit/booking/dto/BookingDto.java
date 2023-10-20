package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class BookingDto {

    @NotNull
    @Positive
    Long itemId;

    @FutureOrPresent
    @NotNull
    LocalDateTime start;

    @Future
    @NotNull
    LocalDateTime end;

}
