package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.dto.UserDto;

import javax.validation.Valid;
import java.util.Set;


@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserControllerGateway {

    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<UserDto> addUser(@Valid @RequestBody UserDto dto) {
        return userClient.addUser(dto);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserDto> patchUser(@RequestBody UserDto dto, @PathVariable Long userId) {
        return userClient.updateUser(dto, userId);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long userId) {
        return userClient.getUser(userId);
    }

    @GetMapping
    public ResponseEntity<Set<UserDto>> getUsers() {
        return userClient.getUsers();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<UserDto> deleteUser(@PathVariable Long userId) {
        return userClient.deleteUser(userId);
    }
}
