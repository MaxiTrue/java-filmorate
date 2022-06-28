package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface Storage<T> {

    T create(T object);

    T update(T object);

    Long delete(Long objectId);

    Collection<T> findAll();

    Optional<T> findById(Long objectId);

}
