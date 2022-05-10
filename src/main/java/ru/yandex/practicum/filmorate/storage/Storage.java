package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;

import java.util.List;


public interface Storage<T> {

    List<T> findAll();

    T findById(Long id) throws Exception;

    T add(T object);

    T update(T object);

    void delete(Long id);

}
