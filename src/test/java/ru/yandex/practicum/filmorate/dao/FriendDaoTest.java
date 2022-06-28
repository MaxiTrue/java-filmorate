package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FriendDaoTest {

    private final FriendDao friendDao;
    private final UserDbStorage userDbStorage;
    private User user;

    @BeforeEach
    public void beforeEach() {
        user = User.builder().id(0).email("max@.mail").login("MaxiTrue").name("Максим").
                birthday(LocalDate.of(1996, Month.FEBRUARY, 28)).friends(new HashSet<>()).build();
    }

    /**
     * Тесты добавления в друзья
     */
    @Test
    public void shouldAddToFriends() {
        User returnUser = userDbStorage.create(user);
        friendDao.addInFriends(1L, returnUser.getId());
        assertThat(userDbStorage.findAllFriendsUserById(1L)).isEqualTo(List.of(returnUser));
    }

    /**
     * Тесты получения данных из БД
     */
    @Test
    public void shouldReturnCollectionAllIdFriendByIdUser() {
        User returnUser = userDbStorage.create(user);
        friendDao.addInFriends(1L, returnUser.getId());
        assertThat(friendDao.findAllIdFriendsUserById(1L)).isEqualTo(List.of(2L));
    }

    @Test
    public void shouldReturnCollectionAllFriendByIdUser() {
        User returnUser = userDbStorage.create(user);
        friendDao.addInFriends(1L, returnUser.getId());
        assertThat(friendDao.findAllFriendsUserById(1L)).isEqualTo(List.of(
                User.builder().id(2).email("max@.mail").login("MaxiTrue").name("Максим").
                        birthday(LocalDate.of(1996, Month.FEBRUARY, 28)).
                        friends(new HashSet<>()).build()));
    }

    @Test
    public void shouldReturnCollectionCommonFriendForThoUsers() {
        User returnUser = userDbStorage.create(user);
        User common = userDbStorage.create(User.builder().id(0).email("common@mail.ru").login("common").name("common").
                birthday(LocalDate.of(1996, Month.MARCH, 11)).friends(new HashSet<>()).build());
        friendDao.addInFriends(1L, common.getId());
        friendDao.addInFriends(returnUser.getId(), common.getId());
        assertThat(userDbStorage.findCommonFriends(1L, returnUser.getId())).isEqualTo(List.of(common));
    }

    /**
     * Тесты на удаление из друзей
     */
    @Test
    public void shouldDeleteFriend() {
        User returnUser = userDbStorage.create(user);
        friendDao.addInFriends(1L, returnUser.getId());
        assertThat(friendDao.findAllIdFriendsUserById(1L)).isEqualTo(List.of(2L));
        friendDao.deleteFromFriends(1L, returnUser.getId());
        assertThat(friendDao.findAllIdFriendsUserById(1L)).isEmpty();
    }


}
