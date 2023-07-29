package ru.practicum.shareit.item.service;


import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

public interface ItemService {

    ItemDto addItem(long userId, ItemDto itemDto);

    ItemDto getItem(long id);

    ItemDto updateItem(long userId, long itemId, ItemDto itemDto);

    void removeItem(long id);

    Collection<ItemDto> getItems();

}
