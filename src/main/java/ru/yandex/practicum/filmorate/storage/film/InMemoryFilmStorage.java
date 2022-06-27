package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage<Film> {

    private final Map<Long, Film> films = new ConcurrentHashMap<>();
    private static long globalId = 0;

    /**
     * Внутренний метод генерации уникального id
     */
    private long generateNewId() {
        return ++globalId;
    }

    /**
     * Методы CRUD над сущностью Film
     */
    @Override
    public Film create(Film film) {
        film.setId(generateNewId());
        films.put(film.getId(), film);
        log.debug("Запись с фильмом успешно добавлена, его id - {}", film.getId());
        return film;
    }

    @Override
    public Film update(Film film) {
        films.put(film.getId(), film);
        log.debug("Запись с фильмом успешно обновлена, его id - {}", film.getId());
        return film;
    }

    @Override
    public Long delete(Long filmId) {
        films.remove(filmId);
        log.debug("Запись с фильмом успешно удалена, его id - {}", filmId);
        return filmId;
    }

    /**
     * Методы получения данных из хранилища
     */
    public Collection<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Optional<Film> findById(Long filmId) {
        /**if (!films.containsKey(filmId)) {
            throw new ObjectNotFoundException("фильм", filmId);
        }
        return films.get(filmId);*/
        return Optional.ofNullable(films.get(filmId));
    }

    @Override
    public Collection<Film> findPopularFilms(Integer maxSize) {
        return films.values().stream().
                sorted(Comparator.comparingInt(o -> o.getLikes().size())).
                limit(maxSize).collect(Collectors.toSet());
    }

    @Override
    public Collection<Long> findAllLikesFilm(Long filmId) {
        return films.get(filmId).getLikes();
    }

    /**
     * Методы добавления/удаления лайков
     */
    @Override
    public Long addLike(Long filmId, Long userId) {
        films.get(filmId).getLikes().add(userId);
        return userId;
    }

    @Override
    public Long removeLike(Long filmId, Long userId) {
        films.get(filmId).getLikes().remove(userId);
        return userId;
    }

}

