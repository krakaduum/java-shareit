package ru.practicum.shareit.item.service;


import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

public interface ItemService {

    ItemDto addItem(long ownerId, ItemDto itemDto);

    ItemDto getItem(long itemId);

    ItemDto updateItem(long ownerId, long itemId, ItemDto itemDto);

    void removeItem(long itemId);

    Collection<ItemDto> getItemsByOwnerId(long ownerId);

    Collection<ItemDto> searchItems(String query);

}
