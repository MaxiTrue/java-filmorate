package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {

    FilmStorage<Film> storageFilms;
    UserStorage<User> storageUsers;

    @Autowired
    public FilmService(InMemoryFilmStorage storageFilms, InMemoryUserStorage storageUsers) {
        this.storageFilms = storageFilms;
        this.storageUsers = storageUsers;
    }

    public Film create(Film film) throws ValidationException {
        checkObject(film, "POST");
        return storageFilms.add(film);
    }

    public Film update(Film film) throws ValidationException {
        checkObject(film, "PUT");
        return storageFilms.update(film);
    }

    public List<Film> findAllFilms() {
        log.debug("Текущее количество фильмов: {}", storageFilms.getSize());
        return storageFilms.findAll();
    }

    public Film findById(Long id) {
        return storageFilms.findById(id);
    }

    public Set<Film> findPopularFilms(Integer count) {

        return storageFilms.findAll().stream().
                sorted((o1, o2) -> o2.getLikes().size() - o1.getLikes().size()).
                limit(count).collect(Collectors.toSet());
    }

    public List<Long> addLikeFilm(Long filmId, Long userId) {

        Film film = storageFilms.findById(filmId);

        film.getLikes().add(storageUsers.findById(userId).getId());

        return new ArrayList<>(film.getLikes());
    }

    public List<Long> removeLikeFilm(Long filmId, Long userId) {

        Film film = storageFilms.findById(filmId);

        if (!film.getLikes().contains(userId)) {
            throw new UserNotFoundException("Лайк от пользователя с id - " + userId + " отсутствует.");
        }

        film.getLikes().remove(userId);

        return new ArrayList<>(film.getLikes());
    }

    private void checkObject(Film film, String method) throws ValidationException {

        if (film.getName() == null ||
                film.getDescription() == null ||
                film.getReleaseDate() == null ||
                film.getDuration() == null) {
            log.debug("Одно из полей объекта film равно null");
            throw new ValidationException("Поля не должны быть null.");
        }

        if (method.equals("PUT")) {
            if (!storageFilms.containsFilm(film.getId())) {
                log.debug("Объект film с таким id - {} НЕ существует.", film.getId());
                throw new ValidationException("Запись с таким значение поля id НЕ существует.");
            }
        }

        if (film.getName().isBlank()) {
            log.debug("Поле name имеет неверный формат.");
            throw new ValidationException("Поле name не может быть пустым.");
        }

        if (film.getDescription().isBlank() || film.getDescription().length() > 200) {
            log.debug("Поле description имеет неверный формат.");
            throw new ValidationException("Поле description не может быть больше 200 знаков.");
        }

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28))) {
            log.debug("Поле date - {} раньше граничной даты 1895.12.28 .", film.getReleaseDate());
            throw new ValidationException("Поле date не может быть раньше 1895.12.28 .");
        }

        if (film.getDuration() < 0) {
            log.debug("Поле duration - {} имеет недопустипое значение .", film.getDuration());
            throw new ValidationException("Поле duration не может быть отрицательным.");
        }
    }


}
