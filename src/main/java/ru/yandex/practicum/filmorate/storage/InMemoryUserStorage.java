package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage<User> {

    private final Map<Long, User> users = new ConcurrentHashMap<>();

    private static long globalId = 0;

    private long generateNewId() {
        return ++globalId;
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User findById(Long id) {

        if (!users.containsKey(id)) {
            throw new UserNotFoundException("Пользователь с ID - " + id + " не найден");
        }

        return users.get(id);
    }

    @Override
    public User add(User user) {
        user.setId(generateNewId());
        users.put(user.getId(), user);
        log.debug("Запись с пользователем успешно добавлена, его id - {}", user.getId());
        return user;
    }

    @Override
    public User update(User user) {
        users.put(user.getId(), user);
        log.debug("Запись с пользователем успешно обновлена, его id - {}", user.getId());
        return user;
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            //TODO Сделать бросок исключения
        }

        if (!users.containsKey(id)) {
            //TODO Сделать бросок исключения
        }

        users.remove(id);
    }

    @Override
    public Integer getSize() {
        return users.size();
    }

    @Override
    public Boolean containsUser(Long id) {
        return users.containsKey(id);
    }


}
