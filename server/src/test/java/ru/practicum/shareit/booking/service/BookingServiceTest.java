package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import ru.practicum.shareit.booking.dto.BookingRequestBody;
import ru.practicum.shareit.booking.exception.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

@SpringBootTest
public class BookingServiceTest {

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
    public void addBooking_withValidData_returnsBookingDto()
            throws NoSuchElementException, UnavailableItemException, InvalidBookerException, InvalidDatesException {
        // Arrange
        var owner = new User(1L,
                "Owner Name",
                "owner.name@mail.com");
        var booker = new User(2L,
                "Booker Name",
                "booker.name@mail.com");
        var item = new Item(
                1L,
                "Item Name",
                "Item Description",
                true,
                owner,
                null);
        var booking = new Booking(1L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                item,
                booker,
                Status.WAITING);
        var bookingRequestBody = new BookingRequestBody(1L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2));

        when(itemRepository.findById(any()))
                .thenReturn(Optional.of(item));
        when(userRepository.findById(any()))
                .thenReturn(Optional.of(booker));
        when(bookingRepository.save(any()))
                .thenReturn(booking);

        // Act
        var addedBookingDto = bookingService.addBooking(2L, bookingRequestBody);

        // Assert
        assertThat(addedBookingDto.getId(), notNullValue());
        assertEquals(booking.getBooker().getId(), addedBookingDto.getBooker().getId());
        assertEquals(booking.getItem().getId(), addedBookingDto.getItem().getId());
        assertEquals(booking.getStatus(), addedBookingDto.getStatus());
    }

    @Test
    public void addBooking_withInvalidItemId_throwsNoSuchElementException() throws NoSuchElementException {
        // Arrange
        var bookingRequestBody = new BookingRequestBody(1L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2));

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> bookingService.addBooking(2L, bookingRequestBody));
    }

    @Test
    public void addBooking_withUnavailableItem_throwsUnavailableItemException() throws UnavailableItemException {
        // Arrange
        var owner = new User(1L,
                "Owner Name",
                "owner.name@mail.com");
        var item = new Item(
                1L,
                "Item Name",
                "Item Description",
                false,
                owner,
                null);
        var bookingRequestBody = new BookingRequestBody(1L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2));

        when(itemRepository.findById(any()))
                .thenReturn(Optional.of(item));

        // Act & Assert
        assertThrows(UnavailableItemException.class, () -> bookingService.addBooking(2L, bookingRequestBody));
    }

    @Test
    public void addBooking_withOwnerAsBooker_throwsInvalidBookerException() throws InvalidBookerException {
        // Arrange
        var owner = new User(1L,
                "Owner Name",
                "owner.name@mail.com");
        var item = new Item(
                1L,
                "Item Name",
                "Item Description",
                true,
                owner,
                null);
        var bookingRequestBody = new BookingRequestBody(1L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2));

        when(itemRepository.findById(any()))
                .thenReturn(Optional.of(item));

        // Act & Assert
        assertThrows(InvalidBookerException.class, () -> bookingService.addBooking(1L, bookingRequestBody));
    }

    @Test
    public void addBooking_withInvalidBookerId_throwsNoSuchElementException() throws NoSuchElementException {
        // Arrange
        var owner = new User(1L,
                "Owner Name",
                "owner.name@mail.com");
        var item = new Item(
                1L,
                "Item Name",
                "Item Description",
                true,
                owner,
                null);
        var bookingRequestBody = new BookingRequestBody(1L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2));

        when(itemRepository.findById(any()))
                .thenReturn(Optional.of(item));

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> bookingService.addBooking(2L, bookingRequestBody));
    }

    @Test
    public void addBooking_withEmptyStart_throwsInvalidDatesException() throws InvalidDatesException {
        // Arrange
        var owner = new User(1L,
                "Owner Name",
                "owner.name@mail.com");
        var booker = new User(2L,
                "Booker Name",
                "booker.name@mail.com");
        var item = new Item(
                1L,
                "Item Name",
                "Item Description",
                true,
                owner,
                null);
        var bookingRequestBody = new BookingRequestBody(1L,
                null,
                LocalDateTime.now().plusDays(2));

        when(itemRepository.findById(any()))
                .thenReturn(Optional.of(item));
        when(userRepository.findById(any()))
                .thenReturn(Optional.of(booker));

        // Act & Assert
        assertThrows(InvalidDatesException.class, () -> bookingService.addBooking(2L, bookingRequestBody));
    }

    @Test
    public void addBooking_withEmptyEnd_throwsInvalidDatesException() throws InvalidDatesException {
        // Arrange
        var owner = new User(1L,
                "Owner Name",
                "owner.name@mail.com");
        var booker = new User(2L,
                "Booker Name",
                "booker.name@mail.com");
        var item = new Item(
                1L,
                "Item Name",
                "Item Description",
                true,
                owner,
                null);
        var bookingRequestBody = new BookingRequestBody(1L,
                LocalDateTime.now().plusDays(1),
                null);

        when(itemRepository.findById(any()))
                .thenReturn(Optional.of(item));
        when(userRepository.findById(any()))
                .thenReturn(Optional.of(booker));

        // Act & Assert
        assertThrows(InvalidDatesException.class, () -> bookingService.addBooking(2L, bookingRequestBody));
    }

    @Test
    public void addBooking_withStartBeforeNow_throwsInvalidDatesException() throws InvalidDatesException {
        // Arrange
        var owner = new User(1L,
                "Owner Name",
                "owner.name@mail.com");
        var booker = new User(2L,
                "Booker Name",
                "booker.name@mail.com");
        var item = new Item(
                1L,
                "Item Name",
                "Item Description",
                true,
                owner,
                null);
        var bookingRequestBody = new BookingRequestBody(1L,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(2));

        when(itemRepository.findById(any()))
                .thenReturn(Optional.of(item));
        when(userRepository.findById(any()))
                .thenReturn(Optional.of(booker));

        // Act & Assert
        assertThrows(InvalidDatesException.class, () -> bookingService.addBooking(2L, bookingRequestBody));
    }

    @Test
    public void addBooking_withEndBeforeNow_throwsInvalidDatesException() throws InvalidDatesException {
        // Arrange
        var owner = new User(1L,
                "Owner Name",
                "owner.name@mail.com");
        var booker = new User(2L,
                "Booker Name",
                "booker.name@mail.com");
        var item = new Item(
                1L,
                "Item Name",
                "Item Description",
                true,
                owner,
                null);
        var bookingRequestBody = new BookingRequestBody(1L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().minusDays(1));

        when(itemRepository.findById(any()))
                .thenReturn(Optional.of(item));
        when(userRepository.findById(any()))
                .thenReturn(Optional.of(booker));

        // Act & Assert
        assertThrows(InvalidDatesException.class, () -> bookingService.addBooking(2L, bookingRequestBody));
    }

    @Test
    public void addBooking_withStartEqualsEnd_throwsInvalidDatesException() throws InvalidDatesException {
        // Arrange
        var owner = new User(1L,
                "Owner Name",
                "owner.name@mail.com");
        var booker = new User(2L,
                "Booker Name",
                "booker.name@mail.com");
        var item = new Item(
                1L,
                "Item Name",
                "Item Description",
                true,
                owner,
                null);
        var date = LocalDateTime.now().plusDays(1);
        var bookingRequestBody = new BookingRequestBody(1L,
                date,
                date);

        when(itemRepository.findById(any()))
                .thenReturn(Optional.of(item));
        when(userRepository.findById(any()))
                .thenReturn(Optional.of(booker));

        // Act & Assert
        assertThrows(InvalidDatesException.class, () -> bookingService.addBooking(2L, bookingRequestBody));
    }

    @Test
    public void addBooking_withStartAfterEnd_throwsInvalidDatesException() throws InvalidDatesException {
        // Arrange
        var owner = new User(1L,
                "Owner Name",
                "owner.name@mail.com");
        var booker = new User(2L,
                "Booker Name",
                "booker.name@mail.com");
        var item = new Item(
                1L,
                "Item Name",
                "Item Description",
                true,
                owner,
                null);
        var bookingRequestBody = new BookingRequestBody(1L,
                LocalDateTime.now().plusDays(2),
                LocalDateTime.now().plusDays(1));

        when(itemRepository.findById(any()))
                .thenReturn(Optional.of(item));
        when(userRepository.findById(any()))
                .thenReturn(Optional.of(booker));

        // Act & Assert
        assertThrows(InvalidDatesException.class, () -> bookingService.addBooking(2L, bookingRequestBody));
    }

    @Test
    public void getBooking_withValidData_returnsBookingDto() throws NoSuchElementException, AccessDeniedException {
        // Arrange
        var owner = new User(1L,
                "Owner Name",
                "owner.name@mail.com");
        var booker = new User(2L,
                "Booker Name",
                "booker.name@mail.com");
        var item = new Item(
                1L,
                "Item Name",
                "Item Description",
                true,
                owner,
                null);
        var booking = new Booking(1L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                item,
                booker,
                Status.WAITING);

        when(bookingRepository.findById(any()))
                .thenReturn(Optional.of(booking));

        // Act
        var foundBookingDto = bookingService.getBooking(2L, 1L);

        // Assert
        assertThat(foundBookingDto.getId(), notNullValue());
        assertEquals(booking.getBooker().getId(), foundBookingDto.getBooker().getId());
        assertEquals(booking.getItem().getId(), foundBookingDto.getItem().getId());
        assertEquals(booking.getStatus(), foundBookingDto.getStatus());
    }

    @Test
    public void getBooking_withInvalidBookingId_throwsNoSuchElementException() throws NoSuchElementException {
        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> bookingService.getBooking(1L, 1L));
    }

    @Test
    public void getBooking_withInvalidUserId_throwsAccessDeniedException() throws AccessDeniedException {
        // Arrange
        var owner = new User(1L,
                "Owner Name",
                "owner.name@mail.com");
        var booker = new User(2L,
                "Booker Name",
                "booker.name@mail.com");
        var item = new Item(
                1L,
                "Item Name",
                "Item Description",
                true,
                owner,
                null);
        var booking = new Booking(1L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                item,
                booker,
                Status.WAITING);

        when(bookingRepository.findById(any()))
                .thenReturn(Optional.of(booking));

        // Act & Assert
        assertThrows(AccessDeniedException.class, () -> bookingService.getBooking(3L, 1L));
    }

    @Test
    public void updateBooking_withValidData_returnsBookingDto()
            throws NoSuchElementException, InvalidStatusException, AccessDeniedException {
        // Arrange
        var owner = new User(1L,
                "Owner Name",
                "owner.name@mail.com");
        var booker = new User(2L,
                "Booker Name",
                "booker.name@mail.com");
        var item = new Item(
                1L,
                "Item Name",
                "Item Description",
                true,
                owner,
                null);
        var booking = new Booking(1L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                item,
                booker,
                Status.WAITING);

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
        assertEquals(booking.getBooker().getId(), updatedBookingDto.getBooker().getId());
        assertEquals(booking.getItem().getId(), updatedBookingDto.getItem().getId());
        assertEquals(Status.APPROVED, updatedBookingDto.getStatus());
    }

    @Test
    public void updateBooking_withInvalidOwnerId_throwsNoSuchElementException() throws NoSuchElementException {
        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> bookingService.updateBooking(1L, 1L, true));
    }

    @Test
    public void updateBooking_withInvalidBookingId_throwsNoSuchElementException() throws NoSuchElementException {
        // Arrange
        var booker = new User(2L,
                "Booker Name",
                "booker.name@mail.com");

        when(userRepository.findById(any()))
                .thenReturn(Optional.of(booker));

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> bookingService.updateBooking(1L, 1L, true));
    }

    @Test
    public void updateBooking_withInvalidOwnerId_throwsAccessDeniedException() throws AccessDeniedException {
        // Arrange
        var owner = new User(1L,
                "Owner Name",
                "owner.name@mail.com");
        var booker = new User(2L,
                "Booker Name",
                "booker.name@mail.com");
        var item = new Item(
                1L,
                "Item Name",
                "Item Description",
                true,
                owner,
                null);
        var booking = new Booking(1L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                item,
                booker,
                Status.WAITING);

        when(userRepository.findById(any()))
                .thenReturn(Optional.of(booker));
        when(bookingRepository.findById(any()))
                .thenReturn(Optional.of(booking));

        // Act & Assert
        assertThrows(AccessDeniedException.class, () -> bookingService.updateBooking(2L, 1L, true));
    }

    @Test
    public void updateBooking_withInvalidBookingStatus_throwsNoSuchElementException() throws InvalidStatusException {
        // Arrange
        var owner = new User(1L,
                "Owner Name",
                "owner.name@mail.com");
        var booker = new User(2L,
                "Booker Name",
                "booker.name@mail.com");
        var item = new Item(
                1L,
                "Item Name",
                "Item Description",
                true,
                owner,
                null);
        var booking = new Booking(1L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                item,
                booker,
                Status.APPROVED);

        when(userRepository.findById(any()))
                .thenReturn(Optional.of(booker));
        when(bookingRepository.findById(any()))
                .thenReturn(Optional.of(booking));

        // Act & Assert
        assertThrows(InvalidStatusException.class, () -> bookingService.updateBooking(1L, 1L, true));
    }

    @Test
    public void getBookings_withValidDataWithAllState_returnsBookingCollection()
            throws NoSuchElementException, IllegalArgumentException {
        // Arrange
        var owner = new User(1L,
                "Owner Name",
                "owner.name@mail.com");
        var booker = new User(2L,
                "Booker Name",
                "booker.name@mail.com");
        var item = new Item(
                1L,
                "Item Name",
                "Item Description",
                true,
                owner,
                null);
        var booking = new Booking(1L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                item,
                booker,
                Status.WAITING);

        when(userRepository.findById(any()))
                .thenReturn(Optional.of(booker));
        when(bookingRepository.findAllByBookerIdOrderByStartDesc(anyLong(), any()))
                .thenReturn(new PageImpl<>(List.of(booking)));

        // Act
        var bookings = bookingService.getBookings(2L, "ALL", null, null);

        // Assert
        assertTrue(bookings.stream().findFirst().isPresent());

        var firstBooking = bookings.stream().findFirst().get();
        assertThat(firstBooking.getId(), notNullValue());
        assertEquals(booking.getBooker().getId(), firstBooking.getBooker().getId());
        assertEquals(booking.getItem().getId(), firstBooking.getItem().getId());
        assertEquals(booking.getStatus(), firstBooking.getStatus());
    }

    @Test
    public void getBookings_withValidDataWithCurrentState_returnsBookingCollection()
            throws NoSuchElementException, IllegalArgumentException {
        // Arrange
        var owner = new User(1L,
                "Owner Name",
                "owner.name@mail.com");
        var booker = new User(2L,
                "Booker Name",
                "booker.name@mail.com");
        var item = new Item(
                1L,
                "Item Name",
                "Item Description",
                true,
                owner,
                null);
        var booking = new Booking(1L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                item,
                booker,
                Status.WAITING);

        when(userRepository.findById(any()))
                .thenReturn(Optional.of(booker));
        when(bookingRepository.findAllCurrentBookingsByBookerIdOrderByIdAsc(anyLong(), any()))
                .thenReturn(new PageImpl<>(List.of(booking)));

        // Act
        var bookings = bookingService.getBookings(2L, "CURRENT", null, null);

        // Assert
        assertTrue(bookings.stream().findFirst().isPresent());

        var firstBooking = bookings.stream().findFirst().get();
        assertThat(firstBooking.getId(), notNullValue());
        assertEquals(booking.getBooker().getId(), firstBooking.getBooker().getId());
        assertEquals(booking.getItem().getId(), firstBooking.getItem().getId());
        assertEquals(booking.getStatus(), firstBooking.getStatus());
    }

    @Test
    public void getBookings_withValidDataWithPastState_returnsBookingCollection()
            throws NoSuchElementException, IllegalArgumentException {
        // Arrange
        var owner = new User(1L,
                "Owner Name",
                "owner.name@mail.com");
        var booker = new User(2L,
                "Booker Name",
                "booker.name@mail.com");
        var item = new Item(
                1L,
                "Item Name",
                "Item Description",
                false,
                owner,
                null);
        var booking = new Booking(1L,
                LocalDateTime.now().minusDays(2),
                LocalDateTime.now().minusDays(1),
                item,
                booker,
                Status.APPROVED);

        when(userRepository.findById(any()))
                .thenReturn(Optional.of(booker));
        when(bookingRepository.findAllPastBookingsByBookerIdOrderByStartDesc(anyLong(), any()))
                .thenReturn(new PageImpl<>(List.of(booking)));

        // Act
        var bookings = bookingService.getBookings(2L, "PAST", null, null);

        // Assert
        assertTrue(bookings.stream().findFirst().isPresent());

        var firstBooking = bookings.stream().findFirst().get();
        assertThat(firstBooking.getId(), notNullValue());
        assertEquals(booking.getBooker().getId(), firstBooking.getBooker().getId());
        assertEquals(booking.getItem().getId(), firstBooking.getItem().getId());
        assertEquals(booking.getStatus(), firstBooking.getStatus());
    }

    @Test
    public void getBookings_withValidDataWithFutureState_returnsBookingCollection()
            throws NoSuchElementException, IllegalArgumentException {
        // Arrange
        var owner = new User(1L,
                "Owner Name",
                "owner.name@mail.com");
        var booker = new User(2L,
                "Booker Name",
                "booker.name@mail.com");
        var item = new Item(
                1L,
                "Item Name",
                "Item Description",
                true,
                owner,
                null);
        var booking = new Booking(1L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                item,
                booker,
                Status.APPROVED);

        when(userRepository.findById(any()))
                .thenReturn(Optional.of(booker));
        when(bookingRepository.findAllFutureBookingsByBookerIdOrderByStartDesc(anyLong(), any()))
                .thenReturn(new PageImpl<>(List.of(booking)));

        // Act
        var bookings = bookingService.getBookings(2L, "FUTURE", null, null);

        // Assert
        assertTrue(bookings.stream().findFirst().isPresent());

        var firstBooking = bookings.stream().findFirst().get();
        assertThat(firstBooking.getId(), notNullValue());
        assertEquals(booking.getBooker().getId(), firstBooking.getBooker().getId());
        assertEquals(booking.getItem().getId(), firstBooking.getItem().getId());
        assertEquals(booking.getStatus(), firstBooking.getStatus());
    }

    @Test
    public void getBookings_withValidDataWithWaitingState_returnsBookingCollection()
            throws NoSuchElementException, IllegalArgumentException {
        // Arrange
        var owner = new User(1L,
                "Owner Name",
                "owner.name@mail.com");
        var booker = new User(2L,
                "Booker Name",
                "booker.name@mail.com");
        var item = new Item(
                1L,
                "Item Name",
                "Item Description",
                true,
                owner,
                null);
        var booking = new Booking(1L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                item,
                booker,
                Status.WAITING);

        when(userRepository.findById(any()))
                .thenReturn(Optional.of(booker));
        when(bookingRepository.findAllByBookerIdAndStatusOrderByIdAsc(anyLong(), any(), any()))
                .thenReturn(new PageImpl<>(List.of(booking)));

        // Act
        var bookings = bookingService.getBookings(2L, "WAITING", null, null);

        // Assert
        assertTrue(bookings.stream().findFirst().isPresent());

        var firstBooking = bookings.stream().findFirst().get();
        assertThat(firstBooking.getId(), notNullValue());
        assertEquals(booking.getBooker().getId(), firstBooking.getBooker().getId());
        assertEquals(booking.getItem().getId(), firstBooking.getItem().getId());
        assertEquals(booking.getStatus(), firstBooking.getStatus());
    }

    @Test
    public void getBookings_withValidDataWithRejectedState_returnsBookingCollection()
            throws NoSuchElementException, IllegalArgumentException {
        // Arrange
        var owner = new User(1L,
                "Owner Name",
                "owner.name@mail.com");
        var booker = new User(2L,
                "Booker Name",
                "booker.name@mail.com");
        var item = new Item(
                1L,
                "Item Name",
                "Item Description",
                true,
                owner,
                null);
        var booking = new Booking(1L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                item,
                booker,
                Status.REJECTED);

        when(userRepository.findById(any()))
                .thenReturn(Optional.of(booker));
        when(bookingRepository.findAllByBookerIdAndStatusOrderByIdAsc(anyLong(), any(), any()))
                .thenReturn(new PageImpl<>(List.of(booking)));

        // Act
        var bookings = bookingService.getBookings(2L, "REJECTED", null, null);

        // Assert
        assertTrue(bookings.stream().findFirst().isPresent());

        var firstBooking = bookings.stream().findFirst().get();
        assertThat(firstBooking.getId(), notNullValue());
        assertEquals(booking.getBooker().getId(), firstBooking.getBooker().getId());
        assertEquals(booking.getItem().getId(), firstBooking.getItem().getId());
        assertEquals(booking.getStatus(), firstBooking.getStatus());
    }

    @Test
    public void getBookings_withInvalidBookerId_throwsNoSuchElementException() throws NoSuchElementException {
        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> bookingService.getBookings(1L, "ALL", null, null));
    }

    @Test
    public void getBookings_withInvalidSearchParams_throwsNoSuchElementException() throws IllegalArgumentException {
        // Arrange
        var booker = new User(2L,
                "Booker Name",
                "booker.name@mail.com");

        when(userRepository.findById(any()))
                .thenReturn(Optional.of(booker));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> bookingService.getBookings(1L, "ALL", -1, -1));
    }

    @Test
    public void getBookingsByItemOwner_withValidDataWithAllState_returnsBookingCollection()
            throws NoSuchElementException, IllegalArgumentException {
        // Arrange
        var owner = new User(1L,
                "Owner Name",
                "owner.name@mail.com");
        var booker = new User(2L,
                "Booker Name",
                "booker.name@mail.com");
        var item = new Item(
                1L,
                "Item Name",
                "Item Description",
                true,
                owner,
                null);
        var booking = new Booking(1L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                item,
                booker,
                Status.WAITING);

        when(userRepository.findById(any()))
                .thenReturn(Optional.of(booker));
        when(bookingRepository.findAllByItemOwnerIdOrderByStartDesc(anyLong(), any()))
                .thenReturn(new PageImpl<>(List.of(booking)));

        // Act
        var bookings = bookingService.getBookingsByItemOwner(1L, "ALL", null, null);

        // Assert
        assertTrue(bookings.stream().findFirst().isPresent());

        var firstBooking = bookings.stream().findFirst().get();
        assertThat(firstBooking.getId(), notNullValue());
        assertEquals(booking.getBooker().getId(), firstBooking.getBooker().getId());
        assertEquals(booking.getItem().getId(), firstBooking.getItem().getId());
        assertEquals(booking.getStatus(), firstBooking.getStatus());
    }

    @Test
    public void getBookingsByItemOwner_withValidDataWithCurrentState_returnsBookingCollection()
            throws NoSuchElementException, IllegalArgumentException {
        // Arrange
        var owner = new User(1L,
                "Owner Name",
                "owner.name@mail.com");
        var booker = new User(2L,
                "Booker Name",
                "booker.name@mail.com");
        var item = new Item(
                1L,
                "Item Name",
                "Item Description",
                true,
                owner,
                null);
        var booking = new Booking(1L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                item,
                booker,
                Status.APPROVED);

        when(userRepository.findById(any()))
                .thenReturn(Optional.of(booker));
        when(bookingRepository.findAllCurrentBookingsByItemOwnerIdOrderByIdAsc(anyLong(), any()))
                .thenReturn(new PageImpl<>(List.of(booking)));

        // Act
        var bookings = bookingService.getBookingsByItemOwner(1L, "CURRENT", null, null);

        // Assert
        assertTrue(bookings.stream().findFirst().isPresent());

        var firstBooking = bookings.stream().findFirst().get();
        assertThat(firstBooking.getId(), notNullValue());
        assertEquals(booking.getBooker().getId(), firstBooking.getBooker().getId());
        assertEquals(booking.getItem().getId(), firstBooking.getItem().getId());
        assertEquals(booking.getStatus(), firstBooking.getStatus());
    }

    @Test
    public void getBookingsByItemOwner_withValidDataWithPastState_returnsBookingCollection()
            throws NoSuchElementException, IllegalArgumentException {
        // Arrange
        var owner = new User(1L,
                "Owner Name",
                "owner.name@mail.com");
        var booker = new User(2L,
                "Booker Name",
                "booker.name@mail.com");
        var item = new Item(
                1L,
                "Item Name",
                "Item Description",
                true,
                owner,
                null);
        var booking = new Booking(1L,
                LocalDateTime.now().minusDays(2),
                LocalDateTime.now().minusDays(1),
                item,
                booker,
                Status.APPROVED);

        when(userRepository.findById(any()))
                .thenReturn(Optional.of(booker));
        when(bookingRepository.findAllPastBookingsByItemOwnerIdOrderByStartDesc(anyLong(), any()))
                .thenReturn(new PageImpl<>(List.of(booking)));

        // Act
        var bookings = bookingService.getBookingsByItemOwner(1L, "PAST", null, null);

        // Assert
        assertTrue(bookings.stream().findFirst().isPresent());

        var firstBooking = bookings.stream().findFirst().get();
        assertThat(firstBooking.getId(), notNullValue());
        assertEquals(booking.getBooker().getId(), firstBooking.getBooker().getId());
        assertEquals(booking.getItem().getId(), firstBooking.getItem().getId());
        assertEquals(booking.getStatus(), firstBooking.getStatus());
    }

    @Test
    public void getBookingsByItemOwner_withValidDataWithFutureState_returnsBookingCollection()
            throws NoSuchElementException, IllegalArgumentException {
        // Arrange
        var owner = new User(1L,
                "Owner Name",
                "owner.name@mail.com");
        var booker = new User(2L,
                "Booker Name",
                "booker.name@mail.com");
        var item = new Item(
                1L,
                "Item Name",
                "Item Description",
                true,
                owner,
                null);
        var booking = new Booking(1L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                item,
                booker,
                Status.WAITING);

        when(userRepository.findById(any()))
                .thenReturn(Optional.of(booker));
        when(bookingRepository.findAllFutureBookingsByItemOwnerIdOrderByStartDesc(anyLong(), any()))
                .thenReturn(new PageImpl<>(List.of(booking)));

        // Act
        var bookings = bookingService.getBookingsByItemOwner(1L, "FUTURE", null, null);

        // Assert
        assertTrue(bookings.stream().findFirst().isPresent());

        var firstBooking = bookings.stream().findFirst().get();
        assertThat(firstBooking.getId(), notNullValue());
        assertEquals(booking.getBooker().getId(), firstBooking.getBooker().getId());
        assertEquals(booking.getItem().getId(), firstBooking.getItem().getId());
        assertEquals(booking.getStatus(), firstBooking.getStatus());
    }

    @Test
    public void getBookingsByItemOwner_withValidDataWithWaitingState_returnsBookingCollection()
            throws NoSuchElementException, IllegalArgumentException {
        // Arrange
        var owner = new User(1L,
                "Owner Name",
                "owner.name@mail.com");
        var booker = new User(2L,
                "Booker Name",
                "booker.name@mail.com");
        var item = new Item(
                1L,
                "Item Name",
                "Item Description",
                true,
                owner,
                null);
        var booking = new Booking(1L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                item,
                booker,
                Status.WAITING);

        when(userRepository.findById(any()))
                .thenReturn(Optional.of(booker));
        when(bookingRepository.findAllByItemOwnerIdAndStatusOrderByIdAsc(anyLong(), any(), any()))
                .thenReturn(new PageImpl<>(List.of(booking)));

        // Act
        var bookings = bookingService.getBookingsByItemOwner(1L, "WAITING", null, null);

        // Assert
        assertTrue(bookings.stream().findFirst().isPresent());

        var firstBooking = bookings.stream().findFirst().get();
        assertThat(firstBooking.getId(), notNullValue());
        assertEquals(booking.getBooker().getId(), firstBooking.getBooker().getId());
        assertEquals(booking.getItem().getId(), firstBooking.getItem().getId());
        assertEquals(booking.getStatus(), firstBooking.getStatus());
    }

    @Test
    public void getBookingsByItemOwner_withValidDataWithRejectedState_returnsBookingCollection()
            throws NoSuchElementException, IllegalArgumentException {
        // Arrange
        var owner = new User(1L,
                "Owner Name",
                "owner.name@mail.com");
        var booker = new User(2L,
                "Booker Name",
                "booker.name@mail.com");
        var item = new Item(
                1L,
                "Item Name",
                "Item Description",
                true,
                owner,
                null);
        var booking = new Booking(1L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                item,
                booker,
                Status.REJECTED);

        when(userRepository.findById(any()))
                .thenReturn(Optional.of(booker));
        when(bookingRepository.findAllByItemOwnerIdAndStatusOrderByIdAsc(anyLong(), any(), any()))
                .thenReturn(new PageImpl<>(List.of(booking)));

        // Act
        var bookings = bookingService.getBookingsByItemOwner(1L, "REJECTED", null, null);

        // Assert
        assertTrue(bookings.stream().findFirst().isPresent());

        var firstBooking = bookings.stream().findFirst().get();
        assertThat(firstBooking.getId(), notNullValue());
        assertEquals(booking.getBooker().getId(), firstBooking.getBooker().getId());
        assertEquals(booking.getItem().getId(), firstBooking.getItem().getId());
        assertEquals(booking.getStatus(), firstBooking.getStatus());
    }

    @Test
    public void getBookingsByItemOwner_withInvalidBookerId_throwsNoSuchElementException() throws NoSuchElementException {
        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> bookingService.getBookingsByItemOwner(1L, "ALL", null, null));
    }

    @Test
    public void getBookingsByItemOwner_withInvalidSearchParams_throwsNoSuchElementException() throws IllegalArgumentException {
        // Arrange
        var owner = new User(1L,
                "Owner Name",
                "owner.name@mail.com");

        when(userRepository.findById(any()))
                .thenReturn(Optional.of(owner));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> bookingService.getBookingsByItemOwner(1L, "ALL", -1, -1));
    }

}
