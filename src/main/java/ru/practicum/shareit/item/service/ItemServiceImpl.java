package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.exception.AccessDeniedException;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemExtendedDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.validation.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final Validator validator;

    public ItemServiceImpl(ItemRepository itemRepository,
                           UserRepository userRepository,
                           BookingRepository bookingRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @Override
    public ItemDto addItem(long ownerId, ItemDto itemDto) {
        Item item = ItemMapper.toItem(itemDto);
        item.setId(null);

        Set<ConstraintViolation<Item>> violations = validator.validate(item);
        for (ConstraintViolation<Item> violation : violations) {
            throw new ValidationException("Валидация не пройдена: " + violation.getMessage());
        }

        Optional<User> owner = userRepository.findById(ownerId);

        if (owner.isEmpty()) {
            throw new NoSuchElementException("Пользователя с идентификатором " + ownerId + " не существует");
        }

        item.setOwner(owner.get());

        item = itemRepository.save(item);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemExtendedDto getItem(long userId, long itemId) {
        Optional<Item> item = itemRepository.findById(itemId);

        if (item.isEmpty()) {
            throw new NoSuchElementException("Вещи с идентификатором " + itemId + " не существует");
        }

        BookingShortDto lastBooking = null;
        BookingShortDto nextBooking = null;
        if (item.get().getOwner().getId() == userId) {
            lastBooking = getLastBooking(item.get());
            nextBooking = getNextBooking(item.get());
        }

        return ItemMapper.toItemExtendedDto(item.get(), lastBooking, nextBooking);
    }

    @Override
    public ItemDto updateItem(long ownerId, long itemId, ItemDto itemDto) {
        Optional<Item> existingItem = itemRepository.findById(itemId);

        if (existingItem.isEmpty()) {
            throw new NoSuchElementException("Вещи с идентификатором " + itemId + " не существует");
        }

        Optional<User> owner = userRepository.findById(ownerId);

        if (owner.isEmpty()) {
            throw new NoSuchElementException("Пользователя с идентификатором " + ownerId + " не существует");
        }

        if (!Objects.equals(existingItem.get().getOwner().getId(), owner.get().getId())) {
            throw new NoSuchElementException("Вещь с идентификатором " + itemId + " принадлежит другому пользователю");
        }

        if (itemDto.getName() != null && !itemDto.getName().trim().isEmpty()) {
            existingItem.get().setName(itemDto.getName());
        }

        if (itemDto.getDescription() != null && !itemDto.getDescription().trim().isEmpty()) {
            existingItem.get().setDescription(itemDto.getDescription());
        }

        if (itemDto.getAvailable() != null) {
            existingItem.get().setAvailable(itemDto.getAvailable());
        }

        Set<ConstraintViolation<Item>> violations = validator.validate(existingItem.get());
        for (ConstraintViolation<Item> violation : violations) {
            throw new ValidationException("Валидация не пройдена: " + violation.getMessage());
        }

        Item updatedItem = itemRepository.save(existingItem.get());
        return ItemMapper.toItemDto(updatedItem);
    }

    @Override
    public void removeItem(long ownerId, long itemId) {
        Optional<Item> item = itemRepository.findById(itemId);

        if (item.isEmpty()) {
            throw new NoSuchElementException("Вещи с идентификатором " + itemId + " не существует");
        }

        if (item.get().getOwner().getId() != ownerId) {
            throw new AccessDeniedException("Удалить вещь может только её владелец");
        }

        itemRepository.deleteById(itemId);
    }

    @Override
    public Collection<ItemExtendedDto> getItemsByOwnerId(long ownerId) {
        return itemRepository
                .findAllByOwnerIdOrderByIdAsc(ownerId)
                .stream()
                .map(item -> ItemMapper.toItemExtendedDto(item, getLastBooking(item), getNextBooking(item)))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<ItemDto> searchItems(String query) {
        if (query.isEmpty() || query.isBlank()) {
            return new ArrayList<>();
        }

        return itemRepository
                .findAllAvailableItemsByNameOrDescription(query)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    private BookingShortDto getLastBooking(Item item) {
        BookingShortDto lastBookingShortDto = null;

        List<Booking> lastBookings = bookingRepository
                .findAllCurrentOrPastBookingsByItemIdOrderByStartDesc(item.getId());

        if (!lastBookings.isEmpty()) {
            Booking lastBooking = lastBookings.get(lastBookings.size() - 1);
            lastBookingShortDto = BookingMapper.toBookingShortDto(lastBooking);
        }

        return lastBookingShortDto;
    }

    private BookingShortDto getNextBooking(Item item) {
        BookingShortDto nextBookingShortDto = null;

        List<Booking> nextBookings = bookingRepository
                .findAllFutureBookingsByItemIdOrderByStartDesc(item.getId());

        if (!nextBookings.isEmpty()) {
            Booking nextBooking = nextBookings.get(nextBookings.size() - 1);
            nextBookingShortDto = BookingMapper.toBookingShortDto(nextBooking);
        }

        return nextBookingShortDto;
    }

}
