package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;

@Component
@RequiredArgsConstructor
public class LikeDao {

    private final JdbcTemplate jdbcTemplate;

    private static final String SQL_GET_ALL_LIKES_FILM = "SELECT id_user FROM like_for_film WHERE id_film = ?";
    private static final String SQL_ADD_LIKE_FILM = "INSERT INTO like_for_film (id_film, id_user) VALUES (?, ?)";
    private static final String SQL_DELETE_LIKE_FILM = "DELETE FROM like_for_film WHERE id_film = ? AND id_user = ?";
    private static final String SQL_DELETE_ALL_LIKE_FILM = "DELETE FROM like_for_film WHERE id_film = ?";
    private static final String SQL_DELETE_ALL_LIKE_USER = "DELETE FROM like_for_film WHERE id_user = ?";

    /**
     * Добавление фильма лайка от пользователя
     */
    public Long addLike(Long filmId, Long userId) {
        jdbcTemplate.update(SQL_ADD_LIKE_FILM, filmId, userId);
        return filmId;
    }

    /**
     * Получение всех лайков для фильма
     */
    public Collection<Long> findAllLikesFilm(Long filmId) {
        return new HashSet<>(jdbcTemplate.query(
                SQL_GET_ALL_LIKES_FILM,
                (rs, rowNum) -> rs.getLong("id_user"),
                filmId));
    }

    /**
     * Удаление лайка от пользовотяля у фильма
     */
    public Long deleteLike(Long filmId, Long userId) {
        jdbcTemplate.update(SQL_DELETE_LIKE_FILM, filmId, userId);
        return filmId;
    }

    /**
     * Удаление всех лайков для фильма
     */
    public Long deleteAllLikesForFilm(Long filmId) {
        jdbcTemplate.update(SQL_DELETE_ALL_LIKE_FILM, filmId);
        return filmId;
    }

    /**
     * Удаление всех лайков от пользователя
     */
    public Long deleteAllLikesForUser(Long userId) {
        jdbcTemplate.update(SQL_DELETE_ALL_LIKE_USER, userId);
        return userId;
    }

}
