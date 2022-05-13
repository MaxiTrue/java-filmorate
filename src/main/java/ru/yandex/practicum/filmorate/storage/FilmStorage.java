package ru.yandex.practicum.filmorate.storage;


import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;

public interface FilmStorage<T> extends Storage<T> {

    @Override
    T findById(Long id) throws FilmNotFoundException;

    Integer getSize();

    Boolean containsFilm(Long id);

}
