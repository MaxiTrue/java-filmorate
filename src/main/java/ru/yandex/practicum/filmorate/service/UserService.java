package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserStorage<User> storageUsers;

    public User create(User user) throws ValidationException {
        checkObject(user, "POST");
        return storageUsers.add(user);
    }

    public User update(User user) throws ValidationException {
        checkObject(user, "PUT");
        return storageUsers.update(user);
    }

    public User findById(Long id) throws UserNotFoundException {
        return storageUsers.findById(id);
    }

    public List<User> findAllUser() {
        log.debug("Текущее количество пользователей: {}", storageUsers.getSize());
        return storageUsers.findAll();
    }

    public void addInFriends(Long id, Long friendId) throws UserNotFoundException {

        User user = storageUsers.findById(id);
        User friend = storageUsers.findById(friendId);

        user.getFriends().add(friendId);
        friend.getFriends().add(id);
        log.debug("Пользователи добавлены друг другу в друзья id - {} и id - {}", id, friendId);
    }

    public void removeFromFriends(Long id, Long friendId) throws UserNotFoundException {

        User user = storageUsers.findById(id);
        User friend = storageUsers.findById(friendId);

        if (!user.getFriends().contains(friendId) ||
                !friend.getFriends().contains(id)) {
            throw new UserNotFoundException("Пользователи с id - " + id + ", " + friendId + " не являются друзьями.");

        }

        user.getFriends().remove(friendId);
        friend.getFriends().remove(id);
        log.debug("Пользователи удалены друг у друга из друзей id - {} и id - {}", id, friendId);
    }

    public List<User> findAllFriendsUserById(Long id) throws UserNotFoundException {

        User user = storageUsers.findById(id);

        return findListFriendsFromUSer(new ArrayList<>(user.getFriends()));
    }

    public List<User> findCommonFriends(Long id, Long otherId) throws UserNotFoundException {

        List<User> commonFriends = new ArrayList<>();

        User firstUser = storageUsers.findById(id);
        User secondUser = storageUsers.findById(otherId);

        List<User> friendsFirstUser = findListFriendsFromUSer(new ArrayList<>(firstUser.getFriends()));
        List<User> friendsSecondUser = findListFriendsFromUSer(new ArrayList<>(secondUser.getFriends()));

        for (User i : friendsFirstUser) {
            Iterator<User> iterator = friendsSecondUser.listIterator();
            while (iterator.hasNext()) {
                if (i.equals(iterator.next())) {
                    commonFriends.add(i);
                    iterator.remove();
                    break;
                }
            }
        }

        return commonFriends;
    }


    private void checkObject(User user, String method) throws ValidationException {

        if (user.getEmail().isBlank() ||
                user.getLogin().isBlank() ||
                user.getBirthday() == null) {
            log.debug("Одно из полей объекта user пустое или равно null");
            throw new ValidationException("Поля не должны быть пустыми или равняться null.");
        }

        if (method.equals("PUT")) {
            if (!storageUsers.containsUser(user.getId())) {
                log.debug("Объект user с таким id - {} НЕ существует.", user.getId());
                throw new ValidationException("Пользователь с таким значение поля id НЕ существует.");
            }
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

    private List<User> findListFriendsFromUSer(List<Long> idFriends) throws UserNotFoundException {
        List<User> emailFriend = new ArrayList<>();

        for (Long id : idFriends) {
            emailFriend.add(storageUsers.findById(id));
        }

        return emailFriend;
    }


}
