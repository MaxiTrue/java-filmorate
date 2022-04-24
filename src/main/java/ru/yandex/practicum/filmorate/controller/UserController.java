package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final Map<Long, User> users = new HashMap<>();

    private long newId = 0;

    @GetMapping
    public List<User> findAll() {
        log.debug("Текущее количество пользователей: {}", users.size());
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User create(@RequestBody User user) {
        log.debug("Получен запрос POST");
        user.setId(generateNewId());
        checkObject(user, "POST");
        users.put(user.getId(), user);
        log.debug("Запись с пользователем успешно добавлена, его id - {}", user.getId());
        return user;
    }

    @PutMapping
    public User put(@RequestBody User user) {
        log.debug("Получен запрос PUT");
        checkObject(user, "PUT");
        users.put(user.getId(), user);
        log.debug("Запись с пользователем успешно обновлена, его id - {}", user.getId());
        return user;
    }

    private long generateNewId() {
        return ++newId;
    }

    private void checkObject(User user, String method) {

        if (user.getId() == 0 ||
                user.getEmail() == null ||
                user.getLogin() == null ||
                user.getBirthday() == null) {
            log.debug("Одно из полей объекта user равно null");
            throw new ValidationException("Поля не должны быть null.");
        }

        if (method.equals("PUT")) {
            if (!users.containsKey(user.getId())) {
                log.debug("Объект user с таким id - {} НЕ существует.", user.getId());
                throw new ValidationException("Пользователь с таким значение поля id НЕ существует.");
            }
        }

        if (user.getEmail().isBlank() ||
                !user.getEmail().contains("@")) {
            log.debug("Поле email имеет неверный формат.");
            throw new ValidationException("Поле email НЕ должно быть пустой, и должно содержать символ - @");
        }

        if (user.getLogin().isBlank() ||
                user.getLogin().contains(" ")) {
            log.debug("Поле login имеет неверный формат.");
            throw new ValidationException("Поле login НЕ должно быть пустым, и НЕ должно содержать пробелы.");
        }

        if (user.getName() == null ||
                user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.debug("Поле nameUser было пустым, для поля установлено значение из login.");
        }

        if (user.getBirthday().isAfter(LocalDate.now().minusYears(10))) {
            log.debug("Поле birthday задано неверно.");
            throw new ValidationException("Пользователь не может быть младше 10 лет.");
        }


    }


}
