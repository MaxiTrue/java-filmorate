package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;

import java.util.Collection;

public interface Storage<T> {

    T create(T object);

    T update(T object);

    Long delete(Long objectId);

    Collection<T> findAll();

    T findById(Long objectId) throws ObjectNotFoundException;

}
