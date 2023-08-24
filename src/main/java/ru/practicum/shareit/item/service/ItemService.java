package ru.practicum.shareit.item.service;


import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemExtendedDto;

import java.util.Collection;

public interface ItemService {

    ItemDto addItem(long ownerId, ItemDto itemDto);

    ItemExtendedDto getItem(long userId, long itemId);

    ItemDto updateItem(long ownerId, long itemId, ItemDto itemDto);

    void removeItem(long ownerId, long itemId);

    Collection<ItemExtendedDto> getItemsByOwnerId(long ownerId);

    Collection<ItemDto> searchItems(String query);

}
