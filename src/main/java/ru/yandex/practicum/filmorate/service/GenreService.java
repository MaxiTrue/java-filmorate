package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenreService {

    private final GenreDao genreDao;

    public Collection<Genre> findAllGenre() {
        return genreDao.findAllGenre();
    }

    public Genre findGenreById(Integer genreId) throws ObjectNotFoundException {
        return genreDao.findGenreById(genreId);
    }

}
