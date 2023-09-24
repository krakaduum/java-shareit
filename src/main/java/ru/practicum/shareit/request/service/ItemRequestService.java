package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestRequestBody;

import java.util.Collection;

public interface ItemRequestService {

    ItemRequestDto addItemRequest(long requesterId, ItemRequestRequestBody itemRequestRequestBody);

    ItemRequestDto getItemRequest(long userId, long requestId);

    Collection<ItemRequestDto> getItemRequestsByRequesterId(long requesterId);

    Collection<ItemRequestDto> getItemRequests(long userId, Integer from, Integer size);

}
