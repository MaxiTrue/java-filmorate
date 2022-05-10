package ru.yandex.practicum.filmorate.storage;

import java.util.List;


public interface Storage<T> {

    List<T> findAll();

    T findById(Long id);

    T add(T object);

    T update(T object);

    void delete(Long id);

}
