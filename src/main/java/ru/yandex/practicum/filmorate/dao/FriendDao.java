package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class FriendDao {

    private final JdbcTemplate jdbcTemplate;

    private static final String SQL_ADD_FRIEND = "INSERT INTO friend (id_first_friend, id_last_friend) VALUES (?, ?)";
    private static final String SQL_DELETE_FROM_FRIEND = "DELETE FROM friend WHERE" +
            " id_first_friend = ? AND id_last_friend = ?";
    private static final String SQL_DELETE_FRIENDS_AND_FROM_FRIENDS = "DELETE FROM friend WHERE" +
            " id_first_friend=? OR id_last_friend=?";
    private static final String SQL_GET_ALL_ID_FRIEND_BY_ID = "SELECT id_last_friend FROM friend WHERE" +
            " id_first_friend = ?";
    private static final String SQL_GET_ALL_FRIEND_BY_ID = "SELECT * FROM user_filmorate WHERE id IN " +
            "(SELECT id_last_friend FROM friend WHERE id_first_friend = ?)";
    private static final String SQL_GET_COMMON_FRIENDS = "SELECT * FROM user_filmorate WHERE id IN " +
            "(SELECT id_last_friend FROM friend WHERE id_first_friend = ? AND id_last_friend IN " +
            "(Select id_last_friend FROM friend WHERE id_first_friend = ?))";


    /**
     * Добавление в друзья
     */
    public Long addInFriends(Long userId, Long friendId) {
        jdbcTemplate.update(SQL_ADD_FRIEND, userId, friendId);
        log.debug("Пользователь - {}, добавил в друзья пользователя - {}", userId, friendId);
        return userId;
    }

    /**
     * Удаление из друзей
     */
    public Long deleteFromFriends(Long userId, Long friendId) {
        jdbcTemplate.update(SQL_DELETE_FROM_FRIEND, userId, friendId);
        log.debug("Пользователь - {}, удалил из друзей пользователя - {}", userId, friendId);
        return userId;
    }

    /**
     * Удаление друзей и удаление из друзей по id пользователя
     */
    public Long deleteFriendsAndFromFriends(Long userId) {
        jdbcTemplate.update(SQL_DELETE_FRIENDS_AND_FROM_FRIENDS, userId, userId);
        return userId;
    }

    /**
     * Получение всех ID друзей по id пользователя
     */
    public Collection<Long> findAllIdFriendsUserById(Long userId) {
        return jdbcTemplate.query(
                SQL_GET_ALL_ID_FRIEND_BY_ID,
                (rs, rowNum) -> rs.getLong("id_last_friend"),
                userId);
    }

    /**
     * Получение всех объектов друзей по id пользователя
     */
    public Collection<User> findAllFriendsUserById(Long userId) {
        return jdbcTemplate.query(SQL_GET_ALL_FRIEND_BY_ID, (rs, rowNum) -> makeUser(rs), userId);
    }

    /**
     * Получение общих друзей для двух пользователей
     */
    public Collection<User> findCommonFriends(Long userId, Long otherId) {
        return jdbcTemplate.query(
                SQL_GET_COMMON_FRIENDS,
                (rs, rowNum) -> makeUser(rs),
                userId, otherId);
    }

    /**
     * Внутренний метод мапинга объекта User
     */
    private User makeUser(ResultSet rs) throws SQLException {
        long id = rs.getLong("id");
        String email = rs.getString("email");
        String login = rs.getString("login");
        String name = rs.getString("name");
        LocalDate birthday = rs.getDate("birthday").toLocalDate();
        Set<Long> friend = new HashSet<>(findAllIdFriendsUserById(id));

        return User.builder().
                id(id).
                email(email).
                login(login).
                name(name).
                birthday(birthday).
                friends(friend)
                .build();
    }
}
