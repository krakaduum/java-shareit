package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.Collection;

@Service
@Slf4j
public class ItemServiceImpl implements ItemService {

    @Qualifier("inMemoryItemStorage")
    private final ItemStorage itemStorage;
    private final UserService userService;

    @Autowired
    public ItemServiceImpl(ItemStorage itemStorage, UserService userService) {
        this.itemStorage = itemStorage;
        this.userService = userService;
    }

    @Override
    public ItemDto addItem(long userId, ItemDto itemDto) {
        UserDto userDto = userService.getUser(userId);

        Item item = ItemMapper.toItem(userDto.getId(), itemDto);
        return ItemMapper.toItemDto(itemStorage.addItem(item));
    }

    @Override
    public ItemDto getItem(long id) {
        return ItemMapper.toItemDto(itemStorage.getItem(id));
    }

    @Override
    public ItemDto updateItem(long userId, long itemId, ItemDto itemDto) {
        Item existingItem = itemStorage.getItem(itemId);

        if (existingItem.getOwnerId() != userId) {
            throw new RuntimeException("Вещь с идентификатором " + itemId + " принадлежит другому пользователю");
        }

        Item item = ItemMapper.toItem(userId, itemId, existingItem, itemDto);

        return ItemMapper.toItemDto(itemStorage.updateItem(item));
    }

    @Override
    public void removeItem(long id) {
        itemStorage.removeItem(id);
    }

    @Override
    public Collection<ItemDto> getItems() {
        Collection<Item> items = itemStorage.getItems();
        Collection<ItemDto> itemDtos = new ArrayList<>();

        for (Item Item : items) {
            itemDtos.add(ItemMapper.toItemDto(Item));
        }

        return itemDtos;
    }

}
