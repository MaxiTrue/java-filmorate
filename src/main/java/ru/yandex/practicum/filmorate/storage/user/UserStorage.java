package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.Collection;

public interface UserStorage<T> extends Storage<T> {

    Long addInFriends(Long id, Long friendId);

    Long removeFromFriends(Long id, Long friendId);

    Collection<User> findAllFriendsUserById(Long id);

    Collection<User> findCommonFriends(Long id, Long otherId);

}
