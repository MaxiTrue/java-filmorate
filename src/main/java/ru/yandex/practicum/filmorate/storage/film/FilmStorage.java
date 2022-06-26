package ru.yandex.practicum.filmorate.storage.film;


import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.Collection;

public interface FilmStorage<T> extends Storage<T> {

    Long addLike(Long filmId, Long userId);

    Long removeLike(Long filmId, Long userId);

    Collection<T> findPopularFilms(Integer maxSize);

    Collection<Long> findAllLikesFilm(Long filmId);

}
