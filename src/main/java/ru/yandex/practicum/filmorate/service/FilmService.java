package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {

    private final FilmStorage<Film> storageFilms;
    private final UserStorage<User> storageUsers;

    /**
     * Методы CRUD над сущностью Film
     */
    public Film createFilm(Film film) throws ValidationException {
        checkObjectFilm(film);
        return storageFilms.create(film);
    }

    public Film updateFilm(Film film) throws ValidationException, ObjectNotFoundException {
        Film checkFilm = storageFilms.findById(film.getId());
        checkObjectFilm(film);
        return storageFilms.update(film);
    }

    public Long deleteFilmById(Long filmId) {
        return storageFilms.delete(filmId);
    }

    /**
     * Методы получения данных из хранилища
     */
    public Collection<Film> findAllFilms() {
        return storageFilms.findAll();
    }

    public Film findFilmById(Long filmId) throws ObjectNotFoundException {
        return storageFilms.findById(filmId);
    }

    public Collection<Film> findPopularFilms(Integer maxSize) {
        return storageFilms.findPopularFilms(maxSize);
    }

    /**
     * Методы добавления/удаления лайков
     */
    public Long addLikeFilm(Long filmId, Long userId) throws ObjectNotFoundException {
        //проверим существует ли фильм которому ставим лайк, и пользователь который ставит лайк
        Film film = storageFilms.findById(filmId);
        User user = storageUsers.findById(userId);

        return storageFilms.addLike(filmId, userId);
    }

    public Long removeLikeFilm(Long filmId, Long userId) throws ObjectNotFoundException {
        Film film = storageFilms.findById(filmId);

        if (!storageFilms.findAllLikesFilm(filmId).contains(userId)) {
            throw new ObjectNotFoundException("фильм", userId);
        }

        return storageFilms.removeLike(filmId, userId);
    }

    /**
     * Метод валидации объекта Film
     */
    private void checkObjectFilm(Film film) throws ValidationException {

        if (film.getName().isBlank() ||
                film.getDescription().isBlank() ||
                film.getReleaseDate() == null ||
                film.getDuration() == null) {
            log.debug("Одно из полей объекта film равно null");
            throw new ValidationException("Поля не должны быть null.");
        }

        if (film.getDescription().length() > 200) {
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
