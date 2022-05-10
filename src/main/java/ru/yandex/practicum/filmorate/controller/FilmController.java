package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
@Slf4j
public class FilmController {

    private final FilmService filmService;

    @GetMapping
    public List<Film> findAll() {
        log.debug("Получен запрос GET на все записи с фильмами");
        return filmService.findAllFilms();
    }

    @GetMapping("{id}")
    public Film findById(@PathVariable("id") Long id) throws FilmNotFoundException {
        log.debug("Получен запрос GET на пользователя id - {}", id);
        return filmService.findById(id);
    }

    @PostMapping
    public Film create(@RequestBody Film film) throws ValidationException {
        log.debug("Получен запрос POST");
        return filmService.create(film);
    }

    @PutMapping
    public Film put(@RequestBody Film film) throws ValidationException {
        log.debug("Получен запрос PUT");
        return filmService.update(film);
    }

}
