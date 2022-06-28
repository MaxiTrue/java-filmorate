package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
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
public class UserDbStorageTest {

    private final UserDbStorage userDbStorage;
    private User user;

    @BeforeEach
    public void beforeEach() {
        user = User.builder().id(0).email("max@.mail").login("MaxiTrue").name("Максим").
                birthday(LocalDate.of(1996, Month.FEBRUARY, 28)).friends(new HashSet<>()).build();
    }

    /**
     * Тесты на добавление объекта в БД
     */
    @Test
    public void shouldAddUser() {
        assertThat(userDbStorage.findAll()).hasSize(1); //до добавления объекта, один объект создаётся в файле
        User userReturn = userDbStorage.create(user);  //добавление в БД объект
        assertThat(userDbStorage.findAll()).hasSize(2); //после добавления объекта
    }

    /**
     * Тесты на обновление объекта в БД
     */
    @Test
    public void shouldUpdateUser() {
        //обновляем день рождения
        User returnUSer = userDbStorage.update(User.builder().id(2).email("max@.mail").login("MaxiTrue").name("Максим").
                birthday(LocalDate.of(1996, Month.FEBRUARY, 29)).friends(new HashSet<>()).build());

        assertThat(returnUSer.getId()).isEqualTo(2L);
        assertThat(returnUSer.getEmail()).isEqualTo("max@.mail");
        assertThat(returnUSer.getLogin()).isEqualTo("MaxiTrue");
        assertThat(returnUSer.getName()).isEqualTo("Максим");
        assertThat(returnUSer.getBirthday()).isEqualTo("1996-02-29");
    }

    /**
     * Тесты на удаление объекта из БД
     */
    @Test
    public void shouldDeleteUser() {
        assertThat(userDbStorage.findAll()).hasSize(1); //до удаления
        userDbStorage.delete(1L);
        assertThat(userDbStorage.findAll()).hasSize(0); //после удаления
    }

    /**
     * Тесты на получение объектов из БД
     */
    @Test
    public void shouldReturnCollectionSize_1() {
        List<User> users = userDbStorage.findAll();
        assertThat(users).hasSize(1);
    }

    @Test
    public void shouldReturnUser_Id_1() {
        User foundUser = userDbStorage.findById(1L).get();
        assertThat(foundUser.getId()).isEqualTo(1L);
        assertThat(foundUser.getEmail()).isEqualTo("mail");
        assertThat(foundUser.getLogin()).isEqualTo("login");
        assertThat(foundUser.getName()).isEqualTo("name");
        assertThat(foundUser.getBirthday()).isEqualTo("1990-03-08");
    }

}
