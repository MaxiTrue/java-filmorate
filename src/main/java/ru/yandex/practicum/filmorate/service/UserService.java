package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserStorage<User> storageUsers;

    /**
     * Методы CRUD над сущностью User
     */
    public User create(User user) throws ValidationException {
        checkObject(user);
        return storageUsers.create(user);
    }

    public User update(User user) throws ValidationException, ObjectNotFoundException {
        checkObject(user);
        User checkUser = storageUsers.findById(user.getId());
        return storageUsers.update(user);
    }

    public Long deleteUserById(Long userId) {
        return storageUsers.delete(userId);
    }

    /**
     * Методы получения данных из хранилища
     */
    public Collection<User> findAllUser() {
        return storageUsers.findAll();
    }

    public User findUserById(Long userId) throws ObjectNotFoundException {
        return storageUsers.findById(userId);
    }

    public Collection<User> findAllFriendsUserById(Long userId) throws ObjectNotFoundException {
        User user = storageUsers.findById(userId);
        return storageUsers.findAllFriendsUserById(userId);
    }

    public Collection<User> findCommonFriends(Long userId, Long otherUserId) throws ObjectNotFoundException {
        User firstUser = storageUsers.findById(userId);
        User secondUser = storageUsers.findById(otherUserId);
        return storageUsers.findCommonFriends(userId, otherUserId);
    }

    /**
     * Методы добавления/удаления друзей
     */
    public Long addInFriends(Long userId, Long friendId) throws ObjectNotFoundException {
        User user = storageUsers.findById(userId);
        User friend = storageUsers.findById(friendId);

        return storageUsers.addInFriends(userId, friendId);
    }

    public Long removeFromFriends(Long userId, Long friendId) throws ObjectNotFoundException {
        User user = storageUsers.findById(userId);
        User friend = storageUsers.findById(friendId);

        if (!user.getFriends().contains(friendId)) {
            throw new ObjectNotFoundException("друг", friendId);
        }
        return storageUsers.removeFromFriends(userId, friendId);
    }

    /**
     * Метод валидации объекта User
     */
    private void checkObject(User user) throws ValidationException {
        if (user.getEmail().isBlank() ||
                user.getLogin().isBlank() ||
                user.getBirthday() == null) {
            log.debug("Одно из полей объекта user пустое или равно null");
            throw new ValidationException("Поля не должны быть пустыми или равняться null.");
        }

        if (!user.getEmail().contains("@")) {
            log.debug("Поле email имеет неверный формат.");
            throw new ValidationException("Поле email НЕ должно быть пустой, и должно содержать символ - @");
        }

        if (user.getLogin().contains(" ")) {
            log.debug("Поле login имеет неверный формат.");
            throw new ValidationException("Поле login НЕ должно быть пустым, и НЕ должно содержать пробелы.");
        }

        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.debug("Поле nameUser было пустым, для поля установлено значение из login.");
        }

        if (user.getBirthday().isAfter(LocalDate.now().minusYears(10))) {
            log.debug("Поле birthday задано неверно.");
            throw new ValidationException("Пользователь не может быть младше 10 лет.");
        }

    }

}
