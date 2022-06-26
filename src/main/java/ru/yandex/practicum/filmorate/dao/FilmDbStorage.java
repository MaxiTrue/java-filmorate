package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Component
@Primary
@RequiredArgsConstructor
@Slf4j
public class FilmDbStorage implements FilmStorage<Film> {

    private final JdbcTemplate jdbcTemplate;
    private final GenreDao genreDao;
    private final LikeDao likeDao;

    private static final String SQL_GET_ALL_FILMS = "SELECT * FROM film";
    private static final String SQL_GET_FILM_ID = "SELECT * FROM film WHERE id = ?";
    private static final String SQL_GET_ID_FILM = "SELECT id FROM film WHERE name=? AND description=?";
    private static final String SQL_ADD_FILM = "INSERT INTO film (name, description, release_date, duration, mpa_id)" +
            " VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE_FILM = "UPDATE film SET name=?, description=?, release_date=?," +
            " duration=?, mpa_id=?";
    private static final String SQL_DELETE_FILM = "DELETE FROM film WHERE id = ?";
    private static final String SQL_GET_POPULAR_FILMS = "SELECT f.id, f.name, f.description, f.release_date," +
            " f.duration, f.mpa_id, COUNT(likes.id_user) " +
            " FROM film AS f " +
            " LEFT JOIN like_for_film AS likes ON f.id = likes.id_film " +
            " GROUP BY f.id, f.name, f.description, f.release_date, f.duration, f.mpa_id " +
            " ORDER BY COUNT(likes.id_user) DESC" +
            " LIMIT ?";


    @Override
    public Film create(Film film) {
        //добавление фильма в таблицу
        jdbcTemplate.update(SQL_ADD_FILM,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId());

        log.debug("Запись с фильмом успешно добавлена, его id - {}", film.getId());

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(SQL_GET_ID_FILM, film.getName(), film.getDescription());
        if (rowSet.next()) {
            long idNewFim = rowSet.getLong("id");

            //добавление строк(и) в таблицу хранения жанров фильма
            if (film.getGenres() != null) {
                for (Genre genre : film.getGenres()) {
                    genreDao.addGenreForFilm(idNewFim, genre.getId());
                }
            }

            return Film.builder().
                    id(idNewFim).
                    name(film.getName()).
                    description(film.getDescription()).
                    releaseDate(film.getReleaseDate()).
                    duration(film.getDuration()).
                    mpa(film.getMpa()).
                    likes(new HashSet<>()).
                    genres(film.getGenres()).build();
        }
        return film;
    }

    @Override
    public Film update(Film film) {
        //обновляем фильм
        jdbcTemplate.update(SQL_UPDATE_FILM,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId());

        //удаляем все записи из таблицы genre_film по id фильма
        genreDao.deleteGenreForFilm(film.getId());

        //добавление строк(и) в таблицу хранения жанров фильма
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                genreDao.addGenreForFilm(film.getId(), genre.getId());
            }
        }
        log.debug("Запись с фильмом успешно обновлена, его id - {}", film.getId());
        return film;
    }

    @Override
    public Long delete(Long filmId) {
        jdbcTemplate.update(SQL_DELETE_FILM, filmId);
        //в БД указано каскадное удаление, но удаляем жанры и лайки на всякий случай
        genreDao.deleteGenreForFilm(filmId);
        likeDao.deleteAllLikesForFilm(filmId);
        log.debug("Запись с фильмом успешно удалена, его id - {}", filmId);
        return filmId;
    }

    /**
     * Методы получения данных из хранилища
     */
    @Override
    public Collection<Film> findAll() {
        return jdbcTemplate.query(SQL_GET_ALL_FILMS, (rs, rowNum) -> makeFilm(rs));
    }

    @Override
    public Film findById(Long filmId) throws ObjectNotFoundException {
        List<Film> films = jdbcTemplate.query(SQL_GET_FILM_ID, (rs, rowNum) -> makeFilm(rs), filmId);
        if (films.size() == 0) {
            throw new ObjectNotFoundException("фильм", filmId);
        }
        return films.get(0);
    }

    @Override
    public Collection<Film> findPopularFilms(Integer maxSize) {
        return new LinkedHashSet<>(jdbcTemplate.query(SQL_GET_POPULAR_FILMS, (rs, rowNum) -> makeFilm(rs), maxSize));
    }

    public Collection<Long> findAllLikesFilm(Long filmId) {
        return likeDao.findAllLikesFilm(filmId);
    }

    /**
     * Методы добавления/удаления лайков
     */
    @Override
    public Long addLike(Long filmId, Long userId) {
        return likeDao.addLike(filmId, userId);
    }

    @Override
    public Long removeLike(Long filmId, Long userId) {
        return likeDao.deleteLike(filmId, userId);
    }

    /**
     * Внутренний метод мапинга объекта Film
     */
    private Film makeFilm(ResultSet rs) throws SQLException {
        long id = rs.getLong("id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
        long duration = rs.getLong("duration");
        Mpa mpa = new Mpa(rs.getInt("mpa_id"));
        Set<Genre> genre = (Set<Genre>) genreDao.findGenreByIdFilm(id);
        Set<Long> likes = (Set<Long>) likeDao.findAllLikesFilm(id);

        return Film.builder().
                id(id).
                name(name).
                description(description).
                releaseDate(releaseDate).
                duration(duration).
                mpa(mpa).
                likes(likes).
                genres(genre.isEmpty() ? null : genre).
                build();
    }

}
