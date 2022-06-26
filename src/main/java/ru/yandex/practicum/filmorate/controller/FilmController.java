package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
@Slf4j
public class FilmController {

    private final FilmService filmService;

    @PostMapping
    public Film create(@RequestBody Film film) throws ValidationException {
        log.debug("Получен запрос POST на фильм name - {}", film.getName());
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film update(@RequestBody Film film) throws ValidationException, ObjectNotFoundException {
        log.debug("Получен запрос PUT на фильм id - {}", film.getId());
        return filmService.updateFilm(film);
    }

    @DeleteMapping({"id"})
    public Long delete(@PathVariable("id") Long id) {
        log.debug("Получен запрос DELETE на фильм id - {}", id);
        return filmService.deleteFilmById(id);
    }

    @GetMapping
    public Collection<Film> findAll() {
        log.debug("Получен запрос GET на все записи с фильмами");
        return filmService.findAllFilms();
    }

    @GetMapping("{id}")
    public Film findById(@PathVariable("id") Long id) throws ObjectNotFoundException {
        log.debug("Получен запрос GET на фильм id - {}", id);
        return filmService.findFilmById(id);
    }

}
