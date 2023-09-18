package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestBody;
import ru.practicum.shareit.booking.exception.*;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public BookingDto addBooking(long bookerId, BookingRequestBody bookingRequestBody) {
        Booking booking = new Booking();
        booking.setId(null);
        booking.setStatus(Status.WAITING);

        Optional<Item> item = itemRepository.findById(bookingRequestBody.getItemId());

        if (item.isEmpty()) {
            throw new NoSuchElementException("Вещи с идентификатором " + bookingRequestBody.getItemId() + " не существует");
        }

        if (!item.get().getAvailable()) {
            throw new UnavailableItemException("Вещь с идентификатором " + bookingRequestBody.getItemId() + " недоступна");
        }

        if (item.get().getOwner().getId() == bookerId) {
            throw new InvalidBookerException("Владелец не может бронировать свою вещь");
        }

        booking.setItem(item.get());

        Optional<User> booker = userRepository.findById(bookerId);

        if (booker.isEmpty()) {
            throw new NoSuchElementException("Пользователя с идентификатором " + bookerId + " не существует");
        }

        booking.setBooker(booker.get());

        LocalDateTime start = bookingRequestBody.getStart();
        LocalDateTime end = bookingRequestBody.getEnd();

        if (start == null) {
            throw new InvalidDatesException("Дата начала бронирования отсутствует");
        }

        if (end == null) {
            throw new InvalidDatesException("Дата окончания бронирования отсутствует");
        }

        if (start.isBefore(LocalDateTime.now())) {
            throw new InvalidDatesException("Дата начала бронирования раньше текущего дня");
        }

        if (end.isBefore(LocalDateTime.now())) {
            throw new InvalidDatesException("Дата окончания бронирования раньше текущего дня");
        }

        if (start.isEqual(end)) {
            throw new InvalidDatesException("Дата начала бронирования совпадает с датой окончания");
        }

        if (start.isAfter(end)) {
            throw new InvalidDatesException("Дата начала бронирования позже даты окончания");
        }

        booking.setStart(start);
        booking.setEnd(end);

        booking = bookingRepository.save(booking);
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingDto getBooking(long userId, long bookingId) {
        Optional<Booking> booking = bookingRepository.findById(bookingId);

        if (booking.isEmpty()) {
            throw new NoSuchElementException("Бронирования с идентификатором " + bookingId + " не существует");
        }

        if (booking.get().getBooker().getId() != userId &&
                booking.get().getItem().getOwner().getId() != userId) {
            throw new AccessDeniedException("Бронирование могут посмотреть только автор бронирования или владелец вещи");
        }

        return BookingMapper.toBookingDto(booking.get());
    }

    @Override
    public BookingDto updateBooking(long ownerId, long bookingId, boolean approved) {
        Optional<User> booker = userRepository.findById(ownerId);

        if (booker.isEmpty()) {
            throw new NoSuchElementException("Пользователя с идентификатором " + ownerId + " не существует");
        }

        Optional<Booking> existingBooking = bookingRepository.findById(bookingId);

        if (existingBooking.isEmpty()) {
            throw new NoSuchElementException("Бронирования с идентификатором " + bookingId + " не существует");
        }

        if (existingBooking.get().getStatus() != Status.WAITING) {
            throw new InvalidStatusException("Статус бронирования уже изменен");
        }

        if (existingBooking.get().getItem().getOwner().getId() != ownerId) {
            throw new AccessDeniedException("Статус бронирования может изменить только владелец вещи");
        }

        existingBooking.get().setStatus(approved ? Status.APPROVED : Status.REJECTED);

        Booking updatedBooking = bookingRepository.save(existingBooking.get());
        return BookingMapper.toBookingDto(updatedBooking);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<BookingDto> getBookings(long bookerId, String state) {
        Optional<User> booker = userRepository.findById(bookerId);

        if (booker.isEmpty()) {
            throw new NoSuchElementException("Пользователя с идентификатором " + bookerId + " не существует");
        }

        switch (parseState(state)) {
            case CURRENT:
                return bookingRepository
                        .findAllCurrentBookingsByBookerIdOrderByIdAsc(bookerId)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());

            case PAST:
                return bookingRepository
                        .findAllPastBookingsByBookerIdOrderByStartDesc(bookerId)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());

            case FUTURE:
                return bookingRepository
                        .findAllFutureBookingsByBookerIdOrderByStartDesc(bookerId)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());

            case WAITING:
                return bookingRepository
                        .findAllByBookerIdAndStatusOrderByIdAsc(bookerId, Status.WAITING)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());

            case REJECTED:
                return bookingRepository
                        .findAllByBookerIdAndStatusOrderByIdAsc(bookerId, Status.REJECTED)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());

            default:
                return bookingRepository
                        .findAllByBookerIdOrderByStartDesc(bookerId)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
        }
    }

    @Override
    public Collection<BookingDto> getBookingsByItemOwner(long ownerId, String state) {
        Optional<User> owner = userRepository.findById(ownerId);

        if (owner.isEmpty()) {
            throw new NoSuchElementException("Пользователя с идентификатором " + ownerId + " не существует");
        }

        switch (parseState(state)) {
            case CURRENT:
                return bookingRepository
                        .findAllCurrentBookingsByItemOwnerIdOrderByIdAsc(ownerId)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());

            case PAST:
                return bookingRepository
                        .findAllPastBookingsByItemOwnerIdOrderByStartDesc(ownerId)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());

            case FUTURE:
                return bookingRepository
                        .findAllFutureBookingsByItemOwnerIdOrderByStartDesc(ownerId)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());

            case WAITING:
                return bookingRepository
                        .findAllByItemOwnerIdAndStatusOrderByIdAsc(ownerId, Status.WAITING)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());

            case REJECTED:
                return bookingRepository
                        .findAllByItemOwnerIdAndStatusOrderByIdAsc(ownerId, Status.REJECTED)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());

            default:
                return bookingRepository
                        .findAllByItemOwnerIdOrderByStartDesc(ownerId)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
        }
    }

    private State parseState(String stateAsString) {
        State state;

        try {
            state = State.valueOf(stateAsString);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown state: UNSUPPORTED_STATUS");
        }

        return state;
    }

}
