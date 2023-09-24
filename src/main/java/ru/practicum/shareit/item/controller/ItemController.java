package ru.practicum.shareit.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentRequestBody;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemExtendedDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;

@RestController
@RequestMapping(path = "/items")
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ItemDto addItem(@RequestHeader(value = "X-Sharer-User-Id") long ownerId, @RequestBody ItemDto itemDto) {
        return itemService.addItem(ownerId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemExtendedDto getItem(@RequestHeader(value = "X-Sharer-User-Id") long userId,
                                   @PathVariable long itemId) {
        return itemService.getItem(userId, itemId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader(value = "X-Sharer-User-Id") long ownerId,
                              @PathVariable long itemId,
                              @RequestBody ItemDto itemDto) {
        return itemService.updateItem(ownerId, itemId, itemDto);
    }

    @DeleteMapping("/{itemId}")
    public void removeItem(@RequestHeader(value = "X-Sharer-User-Id") long ownerId,
                           @PathVariable long itemId) {
        itemService.removeItem(ownerId, itemId);
    }

    @GetMapping
    public Collection<ItemExtendedDto> getItemsByOwnerId(@RequestHeader(value = "X-Sharer-User-Id") long ownerId,
                                                         @RequestParam(required = false) Integer from,
                                                         @RequestParam(required = false) Integer size) {
        return itemService.getItemsByOwnerId(ownerId, from, size);
    }

    @GetMapping("/search")
    public Collection<ItemDto> searchItems(@RequestParam String text,
                                           @RequestParam(required = false) Integer from,
                                           @RequestParam(required = false) Integer size) {
        return itemService.searchItems(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader(value = "X-Sharer-User-Id") long authorId,
                                 @PathVariable long itemId,
                                 @RequestBody CommentRequestBody commentRequestBody) {
        return itemService.addComment(authorId, itemId, commentRequestBody);
    }

}
