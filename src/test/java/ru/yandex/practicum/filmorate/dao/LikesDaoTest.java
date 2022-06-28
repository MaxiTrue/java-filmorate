package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class LikesDaoTest {

    private final LikeDao likeDao;
    private final FilmDbStorage filmDbStorage;
    private final JdbcTemplate jdbcTemplate;
    private Film film;
    private Film film2;

    @BeforeEach
    public void beforeEach() {
        film = Film.builder().id(0).name("Аватар").
                description("Бывший морпех Джейк Салли, получает задание совершить путешествие в несколько световых" +
                        " лет к базе землян на планете Пандора").
                releaseDate(LocalDate.of(2009, Month.DECEMBER, 17)).
                duration(160L).
                mpa(new Mpa(1)).
                likes(new HashSet<>(new ArrayList<>())).
                genres(Collections.singleton(new Genre(2))).build();
        film2 = Film.builder().id(0).name("Аватар 2").description("Скоро выйдет...").
                releaseDate(LocalDate.of(2022, Month.DECEMBER, 16)).mpa(new Mpa(2)).
                duration(320L).build();

    }

    /**
     * Тесты добавления лайка фильму
     */
    @Test
    public void shouldAddLike() {
        Film returnFilm = filmDbStorage.create(film); //добавляем в БД Объект
        assertThat(likeDao.findAllLikesFilm(returnFilm.getId())).hasSize(0); //проверка до добавления лайка
        likeDao.addLike(returnFilm.getId(), 1L); //добавление лайка
        assertThat(likeDao.findAllLikesFilm(returnFilm.getId())).hasSize(1); //проверка после добавления лайка
    }

    /**
     * Тесты удаления лайка фильму
     */
    @Test
    public void shouldDeleteLike() {
        Film returnFilm = filmDbStorage.create(film); //добавляем в БД Объект
        likeDao.addLike(returnFilm.getId(), 1L); //добавление лайка
        assertThat(likeDao.findAllLikesFilm(returnFilm.getId())).hasSize(1); //проверка после добавления лайка
        likeDao.deleteLike(returnFilm.getId(), 1L); // удаление лайка
        assertThat(likeDao.findAllLikesFilm(returnFilm.getId())).hasSize(0); //проверка после добавления лайка
    }

    @Test
    public void shouldReturnPopularFilms() {
        Film returnFilm = filmDbStorage.create(film); // добавляем в БД Объект
        Film returnFilm2 = filmDbStorage.create(film2);
        likeDao.addLike(returnFilm.getId(), 1L); // добавляем лайк фильму c id - 1
        assertThat(
                filmDbStorage.findPopularFilms(10)).isEqualTo(Set.of(
                Film.builder().id(1).name("Аватар").
                        description("Бывший морпех Джейк Салли, получает задание совершить путешествие в несколько световых" +
                                " лет к базе землян на планете Пандора").
                        releaseDate(LocalDate.of(2009, Month.DECEMBER, 17)).
                        duration(160L).
                        mpa(new Mpa(1)).
                        likes(new HashSet<>(List.of(1L))).
                        genres(Collections.singleton(new Genre(2))).build(),
                Film.builder().id(2).name("Аватар 2").description("Скоро выйдет...").
                        releaseDate(LocalDate.of(2022, Month.DECEMBER, 16)).mpa(new Mpa(2)).
                        likes(new HashSet<>()).
                        duration(320L).build()));
    }


}
