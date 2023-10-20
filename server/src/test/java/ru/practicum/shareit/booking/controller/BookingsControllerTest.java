package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestBody;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
public class BookingsControllerTest {

    private final BookingRequestBody bookingRequestBody = new BookingRequestBody(1L,
            LocalDateTime.now(),
            LocalDateTime.now());

    private final BookingDto bookingDto = new BookingDto(1L,
            LocalDateTime.now(),
            LocalDateTime.now(),
            new ItemDto(
                    1L,
                    "Item Name",
                    "Item Description",
                    true,
                    1L,
                    1L),
            new UserDto(1L, "Booker Name", "booker.name@mail.com"),
            Status.APPROVED);

    @Autowired
    ObjectMapper mapper;
    @MockBean
    BookingService bookingService;
    @Autowired
    MockMvc mvc;

    @Test
    public void addBooking_withValidData_returnsOk() throws Exception {
        // Arrange
        var headers = new HttpHeaders();
        headers.set("X-Sharer-User-Id", "1");

        when(bookingService.addBooking(anyLong(), any()))
                .thenReturn(bookingDto);

        // Act & Assert
        mvc.perform(post("/bookings")
                        .headers(headers)
                        .content(mapper.writeValueAsString(bookingRequestBody))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.item.id", is(bookingDto.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.booker.id", is(bookingDto.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().toString())));
    }

    @Test
    public void getBooking_withValidData_returnsOk() throws Exception {
        // Arrange
        var headers = new HttpHeaders();
        headers.set("X-Sharer-User-Id", "1");

        when(bookingService.getBooking(anyLong(), anyLong()))
                .thenReturn(bookingDto);

        // Act & Assert
        mvc.perform(get("/bookings/1")
                        .headers(headers))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.item.id", is(bookingDto.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.booker.id", is(bookingDto.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().toString())));
    }

    @Test
    public void updateBooking_withValidData_returnsOk() throws Exception {
        // Arrange
        var headers = new HttpHeaders();
        headers.set("X-Sharer-User-Id", "1");

        when(bookingService.updateBooking(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(bookingDto);

        // Act & Assert
        mvc.perform(patch("/bookings/1")
                        .headers(headers)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.item.id", is(bookingDto.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.booker.id", is(bookingDto.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().toString())));
    }

    @Test
    public void getBookings_withValidData_returnsOk() throws Exception {
        // Arrange
        var headers = new HttpHeaders();
        headers.set("X-Sharer-User-Id", "1");

        when(bookingService.getBookings(anyLong(), anyString(), any(), any()))
                .thenReturn(List.of(bookingDto));

        // Act & Assert
        mvc.perform(get("/bookings")
                        .headers(headers)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].item.id", is(bookingDto.getItem().getId()), Long.class))
                .andExpect(jsonPath("$[0].booker.id", is(bookingDto.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$[0].status", is(bookingDto.getStatus().toString())));
    }

    @Test
    public void getBookingsByItemOwner_withValidData_returnsOk() throws Exception {
        // Arrange
        var headers = new HttpHeaders();
        headers.set("X-Sharer-User-Id", "1");

        when(bookingService.getBookingsByItemOwner(anyLong(), anyString(), any(), any()))
                .thenReturn(List.of(bookingDto));

        // Act & Assert
        mvc.perform(get("/bookings/owner")
                        .headers(headers)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].item.id", is(bookingDto.getItem().getId()), Long.class))
                .andExpect(jsonPath("$[0].booker.id", is(bookingDto.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$[0].status", is(bookingDto.getStatus().toString())));
    }

}
