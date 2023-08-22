package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
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
    private final Validator validator;

    public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;

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
    public ItemDto getItem(long id) {
        Optional<Item> item = itemRepository.findById(id);

        if (item.isEmpty()) {
            throw new NoSuchElementException("Вещи с идентификатором " + id + " не существует");
        }

        return ItemMapper.toItemDto(item.get());
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

        if (existingItem.get().getOwner().getId() != owner.get().getId()) {
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
    public void removeItem(long id) {
        if (itemRepository.existsById(id)) {
            itemRepository.deleteById(id);
        } else {
            throw new NoSuchElementException("Вещи с идентификатором " + id + " не существует");
        }
    }

    @Override
    public Collection<ItemDto> getItemsByOwnerId(long ownerId) {
        return getItems()
                .stream()
                .filter(x -> x.getOwnerId() == ownerId)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<ItemDto> searchItems(String query) {
        if (query.isEmpty() || query.isBlank()) {
            return new ArrayList<>();
        }
        Collection<ItemDto> items = getItems();
        return items.stream().filter(x -> (x.getName().toLowerCase().contains(query.toLowerCase())
                        || x.getDescription().toLowerCase().contains(query.toLowerCase()))
                        && x.getAvailable())
                .collect(Collectors.toList());
    }

    private Collection<ItemDto> getItems() {
        return itemRepository
                .findAll()
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

}
