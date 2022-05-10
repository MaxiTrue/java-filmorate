package ru.yandex.practicum.filmorate.exception;

public class FilmNotFoundException extends Exception {

    public FilmNotFoundException() {
        super();
    }

    public FilmNotFoundException(String message) {
        super(message);
    }

}
