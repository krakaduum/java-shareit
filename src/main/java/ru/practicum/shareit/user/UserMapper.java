package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

public class UserMapper {

    public static UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public static User toUser(UserDto userDto) {
        return new User(
                userDto.getId(),
                userDto.getName(),
                userDto.getEmail()
        );
    }

    public static User toUser(long id, User existingUser, UserDto userDto) {
        return new User(
                id,
                userDto.getName() != null ? userDto.getName() : existingUser.getName(),
                userDto.getEmail() != null ? userDto.getEmail() : existingUser.getEmail()
        );
    }

}
