package ru.practicum.shareit.item.storage;


import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemStorage {

    Item addItem(Item item);

    Item getItem(long id);

    Item updateItem(Item item);

    void removeItem(long id);

    Collection<Item> getItems();

}
