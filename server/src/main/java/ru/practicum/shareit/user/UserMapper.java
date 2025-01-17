package ru.practicum.shareit.user;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserMapper {
    private final UserService userService;

    public UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public User toEntity(CreateUserDto createUserDto) {
        return User.builder()
                .name(createUserDto.getName())
                .email(createUserDto.getEmail())
                .build();
    }

    public User updateEntity(UpdateUserDto updateUserDto, Long userId) {
        User user = userService.getUserById(userId);
        return User.builder()
                .id(user.getId())
                .name(updateUserDto.getName() != null ? updateUserDto.getName() : user.getName())
                .email(updateUserDto.getEmail() != null ? updateUserDto.getEmail() : user.getEmail())
                .build();
    }

    public List<UserDto> toUserDto(List<User> films) {
        return films.stream()
                .map(this::toUserDto)
                .toList();
    }
}
