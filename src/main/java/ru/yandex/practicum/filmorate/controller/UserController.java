package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<User> findAll() {
        return userService.findAllUser();
    }

    @GetMapping("{id}")
    public User findById(@PathVariable("id") Long id) throws UserNotFoundException {
        return userService.findById(id);
    }

    @PostMapping
    public User create(@RequestBody User user) throws ValidationException {
        log.debug("Получен запрос POST");
        return userService.create(user);
    }

    @PutMapping
    public User put(@RequestBody User user) throws ValidationException {
        log.debug("Получен запрос PUT");
        return userService.update(user);
    }

}
