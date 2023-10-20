package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Constants;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> addItem(@RequestHeader(Constants.UserIdRequestHeaderName) long userId,
                                          @Valid @RequestBody ItemDto itemDto) {
        log.info("Create item {}, userId={}", itemDto, userId);
        return itemClient.addItem(userId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@RequestHeader(Constants.UserIdRequestHeaderName) long userId,
                                          @PathVariable long itemId) {
        log.info("Get item {}, userId={}", itemId, userId);
        return itemClient.getItem(userId, itemId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader(Constants.UserIdRequestHeaderName) long userId,
                                             @PathVariable long itemId,
                                             @RequestBody ItemDto itemDto) {
        log.info("Update item {}, itemId={}, userId={}", itemDto, itemId, userId);
        return itemClient.updateItem(userId, itemId, itemDto);
    }

    @DeleteMapping("/{itemId}")
    public void removeItem(@RequestHeader(Constants.UserIdRequestHeaderName) long userId,
                           @PathVariable long itemId) {
        log.info("Remove item {}, userId={}", itemId, userId);
        itemClient.removeItem(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getItemsByOwnerId(@RequestHeader(Constants.UserIdRequestHeaderName) long userId,
                                                    @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                    @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Get items by owner id, from={}, size={}, userId={}", from, size, userId);
        return itemClient.getItemsByOwnerId(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestHeader(Constants.UserIdRequestHeaderName) long userId,
                                              @RequestParam(name = "text") String text,
                                              @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                              @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Search items, text={}, from={}, size={}, userId={}", text, from, size, userId);
        return itemClient.searchItems(userId, text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader(Constants.UserIdRequestHeaderName) long userId,
                                             @PathVariable long itemId,
                                             @Valid @RequestBody CommentDto commentDto) {
        log.info("Create comment {}, itemId={}, userId={}", commentDto, itemId, userId);
        return itemClient.addComment(userId, itemId, commentDto);
    }

}
