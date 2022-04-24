package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final Map<Long, Film> films = new HashMap<>();

    private long newId = 0;

    @GetMapping
    public List<Film> findAll() {
        log.debug("Текущее количество записей с фильмами: {}", films.size());
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        log.debug("Получен запрос POST");
        film.setId(generateNewId());
        checkObject(film, "POST");
        films.put(film.getId(), film);
        log.debug("Запись с фильмом успешно добавлена, его id - {}", film.getId());
        return film;
    }

    @PutMapping
    public Film put(@RequestBody Film film) {
        log.debug("Получен запрос PUT");
        checkObject(film, "PUT");
        films.put(film.getId(), film);
        log.debug("Запись с фильмом успешно обновлена, его id - {}", film.getId());
        return film;
    }

    private long generateNewId() {
        return ++newId;
    }

   private void checkObject(Film film, String method) {

        if (film.getId() == 0 ||
                film.getName() == null ||
                film.getDescription() == null ||
                film.getReleaseDate() == null ||
                film.getDuration() == null){
            log.debug("Одно из полей объекта film равно null");
            throw new ValidationException("Поля не должны быть null.");
        }

       if (method.equals("PUT")) {
           if (!films.containsKey(film.getId())) {
               log.debug("Объект film с таким id - {} НЕ существует.", film.getId());
               throw new ValidationException("Запись с таким значение поля id НЕ существует.");
           }
       }

        if (film.getName().isBlank()) {
            log.debug("Поле name имеет неверный формат.");
            throw new ValidationException("Поле name не может быть пустым.");
        }

        if (film.getDescription().length() > 200) {
            log.debug("Поле description имеет неверный формат.");
            throw new ValidationException("Поле description не может быть больше 200 знаков.");
        }

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28))) {
            log.debug("Поле date - {} раньше граничной даты 1895.12.28 .", film.getReleaseDate());
            throw new ValidationException("Поле date не может быть раньше 1895.12.28 .");
        }

        if (film.getDuration().getSeconds() < 0) {
            log.debug("Поле duration - {} имеет недопустипое значение .", film.getDuration().getSeconds());
            throw new ValidationException("Поле duration не может быть отрицательным.");
        }
    }

}
