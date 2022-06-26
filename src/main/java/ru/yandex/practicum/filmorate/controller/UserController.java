package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping
    public User create(@RequestBody User user) throws ValidationException {
        log.debug("Получен запрос POST на пользователя email - {}", user.getEmail());
        return userService.create(user);
    }

    @PutMapping
    public User update(@RequestBody User user) throws ValidationException, ObjectNotFoundException {
        log.debug("Получен запрос PUT на пользователя id - {}", user.getId());
        return userService.update(user);
    }

    @DeleteMapping
    public Long delete(@PathVariable("id") Long id) {
        log.debug("Получен запрос DELETE на пользователя id - {}", id);
        return userService.deleteUserById(id);
    }

    @GetMapping
    public Collection<User> findAll() {
        log.debug("Получен запрос GET на все записи с пользователями");
        return userService.findAllUser();
    }

    @GetMapping("{id}")
    public User findById(@PathVariable("id") Long id) throws ObjectNotFoundException {
        log.debug("Получен запрос GET на пользователя id - {}", id);
        return userService.findUserById(id);
    }

}
