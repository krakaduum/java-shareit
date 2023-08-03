package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

public class ItemMapper {

    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwnerId()
        );
    }

    public static Item toItem(long userId, ItemDto itemDto) {
        return new Item(
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                userId
        );
    }

    public static Item toItem(long userId, long itemId, Item existingItem, ItemDto itemDto) {
        return new Item(
                itemId,
                itemDto.getName() != null ? itemDto.getName() : existingItem.getName(),
                itemDto.getDescription() != null ? itemDto.getDescription() : existingItem.getDescription(),
                itemDto.getAvailable() != null ? itemDto.getAvailable() : existingItem.getAvailable(),
                userId
        );
    }

}
