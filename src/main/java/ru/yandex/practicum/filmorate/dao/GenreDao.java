package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.HashSet;

@Component
@RequiredArgsConstructor
public class GenreDao {

    private final JdbcTemplate jdbcTemplate;

    private final static String SQL_GET_ALL_GENRE = "SELECT * FROM genre";
    private final static String SQL_GET_GENRE_ID = "SELECT * FROM genre WHERE id = ?";
    private final static String SQL_GET_GENRE_ID_FILM = "SELECT id_genre FROM genre_film WHERE id_film = ?";
    private final static String SQL_ADD_GENRE_FOR_FILM = "INSERT INTO genre_film (id_film, id_genre) VALUES (?, ?)";
    private static final String SQL_DELETE_GENRE_FOR_FILM = "DELETE FROM genre_film WHERE id_film = ?";

    /**
     * Получение всех жанров
     */
    public Collection<Genre> findAllGenre() {
        return jdbcTemplate.query(SQL_GET_ALL_GENRE, (rs, rowNum) -> new Genre(rs.getInt("id")));
    }

    /**
     * Получение жанра по id
     */
    public Genre findGenreById(Integer genreId) throws ObjectNotFoundException {
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(SQL_GET_GENRE_ID, genreId);
        if (rowSet.next()) {
            return new Genre(rowSet.getInt("id"));
        }
        throw new ObjectNotFoundException("жанр", genreId);
    }

    /**
     * Получение всех жанров по id фильма
     */
    public Collection<Genre> findGenreByIdFilm(Long filmId) {
        return new HashSet<>(jdbcTemplate.query(
                SQL_GET_GENRE_ID_FILM,
                (rs, rowNum) -> new Genre(rs.getInt("id_genre")),
                filmId));
    }

    /**
     * Добавлени записи отнощения жанра к фильму
     */
    protected void addGenreForFilm(Long filmId, Integer genreId) {
        jdbcTemplate.update(SQL_ADD_GENRE_FOR_FILM, filmId, genreId);
    }

    /**
     * Удаление записи отнощения жанра к фильму
     */
    protected void deleteGenreForFilm(Long filmId) {
        jdbcTemplate.update(SQL_DELETE_GENRE_FOR_FILM, filmId);
    }

}
