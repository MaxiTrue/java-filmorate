package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@RestController
@RequestMapping("users/{id}")
@RequiredArgsConstructor
@Slf4j
public class FriendController {

    private final UserService userService;

    @PutMapping("/friends/{friendId}")
    public Long addInFriends(
            @PathVariable("id") Long id,
            @PathVariable("friendId") Long friendId
    ) throws Throwable {
        log.debug("Получен запрос PUT на добавление в друзья друг друга пользователей id - {} и id - {}",
                id, friendId);
        return userService.addInFriends(id, friendId);
    }

    @DeleteMapping("/friends/{friendId}")
    public Long removeFromFriends(
            @PathVariable("id") Long id,
            @PathVariable("friendId") Long friendId
    ) throws Throwable {
        log.debug("Получен запрос DELETE на удаление из друзей друг друга пользователей id - {} и id - {}",
                id, friendId);
        return userService.removeFromFriends(id, friendId);
    }

    @GetMapping("/friends")
    public Collection<User> findAllFriendsById(@PathVariable("id") Long id) throws Throwable {
        log.debug("Получен запрос GET на друзей пользователя id - {}", id);
        return userService.findAllFriendsUserById(id);
    }

    @GetMapping("/friends/common/{otherId}")
    public Collection<User> findCommonFriends(
            @PathVariable("id") Long id,
            @PathVariable("otherId") Long otherId
    ) throws Throwable {
        log.debug("Получен запрос GET на общих друзей пользователя id - {} и id - {}", id, otherId);
        return userService.findCommonFriends(id, otherId);
    }

}
