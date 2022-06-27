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

    public Collection<Genre> findGenre() {
        return genreDao.findAll();
    }

    public Genre findById(Integer genreId) throws ObjectNotFoundException {
        return genreDao.findById(genreId);
    }

}
