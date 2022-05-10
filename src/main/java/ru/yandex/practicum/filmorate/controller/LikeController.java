package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/films")
public class LikeController {

    FilmService filmService;

    @Autowired
    public LikeController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/popular")
    public Set<Film> findPopularFilms(
            @RequestParam(value = "count", defaultValue = "10", required = false) Integer count
    ) {

        return filmService.findPopularFilms(count);
    }


    @PutMapping("/{id}/like/{userId}")
    public List<Long> addLikeFilm(
            @PathVariable("id") Long filmId,
            @PathVariable("userId") Long userId
    ) {

        return filmService.addLikeFilm(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public List<Long> removeLikeFilm(
            @PathVariable("id") Long filmId,
            @PathVariable("userId") Long userId
    ) {

        return filmService.removeLikeFilm(filmId, userId);
    }


}
