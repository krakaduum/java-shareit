package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestBody;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

@SpringBootTest
public class BookingServiceTest {

    private final Booking booking = new Booking(1L,
            LocalDateTime.now().plusDays(1),
            LocalDateTime.now().plusDays(2),
            new Item(
                    1L,
                    "Item Name",
                    "Item Description",
                    true,
                    new User(1L, "Owner Name", "owner.name@mail.com"),
                    null),
            new User(2L, "Booker Name", "booker.name@mail.com"),
            Status.WAITING);

    private final BookingRequestBody bookingRequestBody = new BookingRequestBody(1L,
            LocalDateTime.now().plusDays(1),
            LocalDateTime.now().plusDays(2));

    private final BookingDto bookingDto = new BookingDto(1L,
            LocalDateTime.now().plusDays(1),
            LocalDateTime.now().plusDays(2),
            new ItemDto(
                    1L,
                    "Item Name",
                    "Item Description",
                    true,
                    1L,
                    1L),
            new UserDto(2L, "Booker Name", "booker.name@mail.com"),
            Status.WAITING);

    private final User booker = new User(2L,
            "Booker Name",
            "booker.name@mail.com");

    private final Item item = new Item(
            1L,
            "Item Name",
            "Item Description",
            true,
            new User(1L, "Owner Name", "owner.name@mail.com"),
            null);

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookingRepository bookingRepository;

    private BookingService bookingService;

    @BeforeEach
    public void setUp() {
        openMocks(this);
        bookingService = new BookingServiceImpl(
                bookingRepository,
                itemRepository,
                userRepository);
    }

    @Test
    public void addBookingTest() {
        // Arrange
        when(userRepository.findById(any()))
                .thenReturn(Optional.of(booker));
        when(itemRepository.findById(any()))
                .thenReturn(Optional.of(item));
        when(bookingRepository.save(any()))
                .thenReturn(booking);

        // Act
        var addedBookingDto = bookingService.addBooking(2L, bookingRequestBody);

        // Assert
        assertThat(addedBookingDto.getId(), notNullValue());
        assertEquals(bookingDto.getBooker().getId(), addedBookingDto.getBooker().getId());
        assertEquals(bookingDto.getItem().getId(), addedBookingDto.getItem().getId());
        assertEquals(bookingDto.getStatus(), addedBookingDto.getStatus());
    }

    @Test
    public void getBookingTest() {
        // Arrange
        when(bookingRepository.findById(any()))
                .thenReturn(Optional.of(booking));

        // Act
        var foundBookingDto = bookingService.getBooking(2L, 1L);

        // Assert
        assertThat(foundBookingDto.getId(), notNullValue());
        assertEquals(bookingDto.getBooker().getId(), foundBookingDto.getBooker().getId());
        assertEquals(bookingDto.getItem().getId(), foundBookingDto.getItem().getId());
        assertEquals(bookingDto.getStatus(), foundBookingDto.getStatus());
    }

    @Test
    public void updateBookingTest() {
        // Arrange
        when(userRepository.findById(any()))
                .thenReturn(Optional.of(booker));
        when(bookingRepository.findById(any()))
                .thenReturn(Optional.of(booking));
        when(bookingRepository.save(any()))
                .thenReturn(booking);

        // Act
        var updatedBookingDto = bookingService.updateBooking(1L, 1L, true);

        // Assert
        assertThat(updatedBookingDto.getId(), notNullValue());
        assertEquals(bookingDto.getBooker().getId(), updatedBookingDto.getBooker().getId());
        assertEquals(bookingDto.getItem().getId(), updatedBookingDto.getItem().getId());
        assertEquals(Status.APPROVED, updatedBookingDto.getStatus());
    }

    @Test
    public void getBookingsTest() {
        // Arrange
        when(userRepository.findById(any()))
                .thenReturn(Optional.of(booker));
        when(bookingRepository.findAllByBookerIdOrderByStartDesc(anyLong(), any()))
                .thenReturn(new PageImpl(List.of(booking)));

        // Act
        var bookings = bookingService.getBookings(2L, "ALL", null, null);
        var firstBooking = bookings.stream().findFirst().get();

        // Assert
        assertThat(firstBooking.getId(), notNullValue());
        assertEquals(bookingDto.getBooker().getId(), firstBooking.getBooker().getId());
        assertEquals(bookingDto.getItem().getId(), firstBooking.getItem().getId());
        assertEquals(bookingDto.getStatus(), firstBooking.getStatus());
    }

    @Test
    public void getBookingsByItemOwnerTest() {
        // Arrange
        when(userRepository.findById(any()))
                .thenReturn(Optional.of(booker));
        when(bookingRepository.findAllByItemOwnerIdOrderByStartDesc(anyLong(), any()))
                .thenReturn(new PageImpl(List.of(booking)));

        // Act
        var bookings = bookingService.getBookingsByItemOwner(1L, "ALL", null, null);
        var firstBooking = bookings.stream().findFirst().get();

        // Assert
        assertThat(firstBooking.getId(), notNullValue());
        assertEquals(bookingDto.getBooker().getId(), firstBooking.getBooker().getId());
        assertEquals(bookingDto.getItem().getId(), firstBooking.getItem().getId());
        assertEquals(bookingDto.getStatus(), firstBooking.getStatus());
    }

}
