package ru.practicum.shareit.request.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestRequestBody;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.Collection;


@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @Autowired
    public ItemRequestController(ItemRequestService itemRequestService) {
        this.itemRequestService = itemRequestService;
    }

    @PostMapping
    public ItemRequestDto addItemRequest(@RequestHeader(value = "X-Sharer-User-Id") long requesterId,
                                         @RequestBody ItemRequestRequestBody itemRequestRequestBody) {
        return itemRequestService.addItemRequest(requesterId, itemRequestRequestBody);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getItemRequest(@RequestHeader(value = "X-Sharer-User-Id") long userId,
                                         @PathVariable long requestId) {
        return itemRequestService.getItemRequest(userId, requestId);
    }

    @GetMapping
    Collection<ItemRequestDto> getItemRequestsByRequesterId(@RequestHeader(value = "X-Sharer-User-Id") long requesterId) {
        return itemRequestService.getItemRequestsByRequesterId(requesterId);
    }

    @GetMapping("/all")
    Collection<ItemRequestDto> getItemRequests(@RequestHeader(value = "X-Sharer-User-Id") long userId,
                                               @RequestParam(required = false) Integer from,
                                               @RequestParam(required = false) Integer size) {
        return itemRequestService.getItemRequests(userId, from, size);
    }

}
