package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@RestController
@RequestMapping("users/{id}")
@Slf4j
public class FriendController {

    UserService userService;

    @Autowired
    public FriendController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/friends")
    public List<User> findAllFriendsById(@PathVariable("id") Long id) {
        log.debug("Получен запрос GET на друзей пользователя id - {}", id);
        return userService.findAllFriendsUserById(id);
    }

    @GetMapping("/friends/common/{otherId}")
    public List<User> findCommonFriends(
            @PathVariable("id") Long id,
            @PathVariable("otherId") Long otherId
    ) {
        log.debug("Получен запрос GET на обхих друзей пользователя id - {} и id - {}", id, otherId);
        return userService.findCommonFriends(id, otherId);
    }

    @PutMapping("/friends/{friendId}")
    public void addInFriends(
            @PathVariable("id") Long id,
            @PathVariable("friendId") Long friendId
    ) {
        log.debug("Получен запрос PUT на добавление в друзья друг друга пользователей id - {} и id - {}",
                id, friendId);
        userService.addInFriends(id, friendId);
    }

    @DeleteMapping("/friends/{friendId}")
    public void removeFromFriends(
            @PathVariable("id") Long id,
            @PathVariable("friendId") Long friendId
    ) {
        log.debug("Получен запрос DELETE на удаление из друзей друг друга пользователей id - {} и id - {}",
                id, friendId);
        userService.removeFromFriends(id, friendId);
    }

}
