package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> findAllUsers() {
        return userClient.getUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable("id") Long id) {
        return userClient.getUserById(id);
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody @Valid CreateUserDto createUserDto) {
        return userClient.createUser(createUserDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable("id") Long id,
                                             @RequestBody @Valid UpdateUserDto updateUserDto) {
        return userClient.updateUser(id, updateUserDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable("id") Long id) {
        return userClient.deleteUser(id);
    }
}
