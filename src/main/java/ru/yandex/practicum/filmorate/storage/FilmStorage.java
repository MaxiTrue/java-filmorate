package ru.yandex.practicum.filmorate.storage;

import java.util.List;

public interface FilmStorage<T> extends Storage<T> {

    Integer getSize();

    Boolean containsFilm(Long id);

}
