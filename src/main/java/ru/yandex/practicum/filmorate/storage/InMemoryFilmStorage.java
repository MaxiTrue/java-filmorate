package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage<Film> {

    private final Map<Long, Film> films = new ConcurrentHashMap<>();

    private static long globalId = 0;

    private long generateNewId() {
        return ++globalId;
    }

    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film findById(Long id) throws FilmNotFoundException {

        if (!films.containsKey(id)) {
            throw new FilmNotFoundException("Фильм с ID - " + id + " не найден");
        }

        return films.get(id);
    }

    @Override
    public Film add(Film film) {
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
    public void delete(Long id) {
        if (id == null) {
            //TODO Сделать бросок исключения
        }

        if (!films.containsKey(id)) {
            //TODO Сделать бросок исключения
        }

        films.remove(id);
    }

    @Override
    public Integer getSize() {
        return films.size();
    }

    @Override
    public Boolean containsFilm(Long id) {
        return films.containsKey(id);
    }

}

