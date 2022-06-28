package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage<User> {

    private final Map<Long, User> users = new ConcurrentHashMap<>();

    private static long globalId = 0;

    /**
     * Внутренний метод генерации уникального id
     */
    private long generateNewId() {
        return ++globalId;
    }

    /**
     * Методы CRUD над сущностью User
     */
    @Override
    public User create(User user) {
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
    public Long delete(Long userId) {
        users.remove(userId);
        log.debug("Запись с пользователем успешно удалена, его id - {}", userId);
        return userId;
    }

    /**
     * Методы получения данных из хранилища
     */
    @Override
    public Collection<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> findById(Long id) {
        /**if (!users.containsKey(id)) {
            throw new ObjectNotFoundException("пользователь", id);
        }
        return users.get(id);*/
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public Collection<User> findAllFriendsUserById(Long id) {
        Set<User> friends = new HashSet<>();
        for (Long idFriend : users.get(id).getFriends()) {
            friends.add(users.get(idFriend));
        }
        return friends;
    }

    @Override
    public Collection<User> findCommonFriends(Long id, Long otherId) {
        return users.get(id).getFriends().stream().
                filter(idUser -> users.get(otherId).getFriends().contains(idUser)).
                map(users::get).collect(Collectors.toSet());
    }

    /**
     * Методы добавления/удаления друзей
     */
    @Override
    public Long addInFriends(Long id, Long friendId) {
        users.get(id).getFriends().add(friendId);
        log.debug("Пользователь - {}, добавил в друзья пользователя - {}", id, friendId);
        return id;
    }

    @Override
    public Long removeFromFriends(Long id, Long friendId) {
        users.get(id).getFriends().remove(friendId);
        log.debug("Пользователь - {}, удалил из друзей пользователя - {}", id, friendId);
        return id;
    }

}
