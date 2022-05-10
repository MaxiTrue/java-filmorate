package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.UserNotFoundException;

public interface UserStorage<T> extends Storage<T> {

    @Override
    T findById(Long id) throws UserNotFoundException;

    Integer getSize();

    Boolean containsUser(Long id);

}
