package ru.practicum.shareit.item.service;


import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentRequestBody;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemExtendedDto;

import java.util.Collection;

public interface ItemService {

    ItemDto addItem(long ownerId, ItemDto itemDto);

    ItemExtendedDto getItem(long userId, long itemId);

    ItemDto updateItem(long ownerId, long itemId, ItemDto itemDto);

    void removeItem(long ownerId, long itemId);

    Collection<ItemExtendedDto> getItemsByOwnerId(long ownerId, Integer from, Integer size);

    Collection<ItemDto> searchItems(String query, Integer from, Integer size);

    CommentDto addComment(long authorId, long itemId, CommentRequestBody commentRequestBody);

}
