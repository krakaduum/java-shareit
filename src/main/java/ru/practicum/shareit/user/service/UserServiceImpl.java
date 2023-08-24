package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.ArrayList;
import java.util.Collection;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Qualifier("inMemoryUserStorage")
    private final UserStorage userStorage;

    @Autowired
    public UserServiceImpl(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        return UserMapper.toUserDto(userStorage.addUser(user));
    }

    @Override
    public UserDto getUser(long id) {
        return UserMapper.toUserDto(userStorage.getUser(id));
    }

    @Override
    public UserDto updateUser(long id, UserDto userDto) {
        User existingUser = userStorage.getUser(id);
        User user = UserMapper.toUser(id, existingUser, userDto);

        return UserMapper.toUserDto(userStorage.updateUser(user));
    }

    @Override
    public void removeUser(long id) {
        userStorage.removeUser(id);
    }

    @Override
    public Collection<UserDto> getUsers() {
        Collection<User> users = userStorage.getUsers();
        Collection<UserDto> userDtos = new ArrayList<>();

        for (User user : users) {
            userDtos.add(UserMapper.toUserDto(user));
        }

        return userDtos;
    }

}
