package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserStorage {

    User addUser(User user);

    User getUser(long id);

    User updateUser(User user);

    void removeUser(long id);

    Collection<User> getUsers();

}
