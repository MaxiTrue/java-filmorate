package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Component
@Primary
@RequiredArgsConstructor
@Slf4j
public class UserDbStorage implements UserStorage<User> {

    private final JdbcTemplate jdbcTemplate;
    private final FriendDao friendDao;
    private final LikeDao likeDao;

    private static final String SQL_GET_ALL_USER = "select * from user_filmorate";
    private static final String SQL_GET_USER_BY_ID = "select * from user_filmorate where id=?";
    private static final String SQL_GET_USER_ID = "SELECT id FROM user_filmorate WHERE" +
            " email=? AND login=? AND name=? AND birthday=?";
    private static final String SQL_ADD_USER = "INSERT INTO user_filmorate (email, login, name, birthday)" +
            " VALUES (?, ?, ?, ?)";
    private static final String SQL_UPDATE_USER = "UPDATE user_filmorate SET" +
            " email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";
    private static final String SQL_DELETE_USER = "DELETE FROM user_filmorate WHERE id = ?";


    /**
     * Методы CRUD над сущностью User
     */
    @Override
    public User create(User user) {
        jdbcTemplate.update(SQL_ADD_USER, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());
        log.debug("Запись с пользователем успешно добавлена, его id - {}", user.getId());

        SqlRowSet rs = jdbcTemplate.queryForRowSet(
                SQL_GET_USER_ID,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());
        if (rs.next()) {
            return User.builder().
                    id(rs.getLong("id")).
                    email(user.getEmail()).
                    login(user.getLogin()).
                    name(user.getName()).
                    friends(new HashSet<>()).
                    birthday(user.getBirthday()).build();
        }
        return user;
    }

    @Override
    public User update(User user) {
        jdbcTemplate.update(SQL_UPDATE_USER,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        log.debug("Запись с пользователем успешно обновлена, его id - {}", user.getId());
        return user;
    }

    @Override
    public Long delete(Long userId) {
        jdbcTemplate.update(SQL_DELETE_USER, userId);
        //в БД указано каскадное удаление, но удаляем записи дружбы и лайки от пользователя на всякий случай
        friendDao.deleteFriendsAndFromFriends(userId);
        likeDao.deleteAllLikesForUser(userId);
        log.debug("Запись с пользователем успешно удалена, его id - {}", userId);
        return userId;
    }

    /**
     * Методы получения данных из хранилища
     */
    @Override
    public List<User> findAll() {
        return jdbcTemplate.query(SQL_GET_ALL_USER, (rs, rowNum) -> makeUser(rs));
    }

    @Override
    public User findById(Long userId) throws ObjectNotFoundException {
        List<User> users = jdbcTemplate.query(SQL_GET_USER_BY_ID, (rs, rowNum) -> makeUser(rs), userId);

        if (users.size() == 0) {
            throw new ObjectNotFoundException("пользователь", userId);
        }
        return users.get(0);
    }

    @Override
    public Collection<User> findAllFriendsUserById(Long userId) {
        return friendDao.findAllFriendsUserById(userId);
    }

    @Override
    public Collection<User> findCommonFriends(Long id, Long otherId) {
        return friendDao.findCommonFriends(id, otherId);
    }

    /**
     * Методы добавления/удаления друзей
     */
    @Override
    public Long addInFriends(Long userId, Long friendId) {
        return friendDao.addInFriends(userId, friendId);
    }

    @Override
    public Long removeFromFriends(Long userId, Long friendId) {
        return friendDao.deleteFromFriends(userId, friendId);
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
        Set<Long> friend = new HashSet<>(friendDao.findAllIdFriendsUserById(id));

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
