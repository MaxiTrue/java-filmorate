package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FilmDbStorageTest {

    private final FilmDbStorage filmDbStorage;
    private final JdbcTemplate jdbcTemplate;
    private Film film;

    @BeforeEach
    public void beforeEach() {
        film = Film.builder().id(1).name("Аватар").
                description("Бывший морпех Джейк Салли, получает задание совершить путешествие в несколько световых" +
                        " лет к базе землян на планете Пандора").
                releaseDate(LocalDate.of(2009, Month.DECEMBER, 17)).
                duration(160L).
                mpa(new Mpa(1)).
                likes(new HashSet<>()).
                genres(Collections.singleton(new Genre(2))).build();
    }

    /**
     * Тесты на добавление объекта в БД
     */
    @Test
    public void shouldAddFilm() throws ObjectNotFoundException {
        assertThat(filmDbStorage.findAll()).hasSize(0); //до добавления объекта
        Film filmReturn = filmDbStorage.create(film);  //добавление в БД объект
        assertThat(filmDbStorage.findAll()).hasSize(1); //после добавления объекта
    }

    /**
     * Тесты на обновление объекта в БД
     */
    @Test
    public void shouldUpdateFilm() {
        Film filmReturn = filmDbStorage.create(film); //добавление фильма в БД
        //обновляем описание
        Film filmUpdate = filmDbStorage.update(Film.builder().id(filmReturn.getId()).name("Аватар").
                description("Description_Update").
                releaseDate(LocalDate.of(2009, Month.DECEMBER, 17)).
                duration(160L).
                mpa(new Mpa(1)).
                likes(new HashSet<>()).
                genres(Collections.singleton(new Genre(2))).build());

        //проверка полей
        assertThat(filmUpdate.getName()).isEqualTo("Аватар");
        assertThat(filmUpdate.getDescription()).isEqualTo("Description_Update");
        assertThat(filmUpdate.getReleaseDate()).isEqualTo("2009-12-17");
        assertThat(filmUpdate.getDuration()).isEqualTo(160L);
        assertThat(filmUpdate.getMpa().getId()).isEqualTo(1);
    }

    /**
     * Тесты на удаление объекта из БД
     */
    @Test
    public void shouldDeleteFilm() {
        Film filmReturn = filmDbStorage.create(film); //добавление фильма в БД
        //проверка наличия фильма в бд
        assertThat(filmDbStorage.findAll()).hasSize(1); //проверка количества перед удаление
        filmDbStorage.delete(filmReturn.getId()); //удаление объекта из БД
        assertThat(filmDbStorage.findAll()).hasSize(0); //проверка количества после удаления
    }

    /**
     * Тесты на получение объектов из БД
     */
    @Test
    public void shouldReturnCollectionSize_0() {
        //в базу ничего не добавляли, должен вернуть пустую коллекцию
        List<Film> films = (List<Film>) filmDbStorage.findAll();
        assertThat(films).hasSize(0);
    }

    @Test
    public void shouldReturnCollectionSize_1() {
        filmDbStorage.create(film); //добавление объекта в БД
        List<Film> films = (List<Film>) filmDbStorage.findAll();
        assertThat(films).hasSize(1);
    }

    @Test
    public void shouldReturnFilm_Id_1() throws ObjectNotFoundException {
        Film filmReturn = filmDbStorage.create(film);  //добавление в БД объект
        Film foundFilm = filmDbStorage.findById(filmReturn.getId()); // получение из БД объекта по вернувшемуся id
        assertThat(filmReturn.getId()).isEqualTo(foundFilm.getId());
        assertThat(filmReturn.getName()).isEqualTo(foundFilm.getName());
        assertThat(filmReturn.getDescription()).isEqualTo(foundFilm.getDescription());
        assertThat(filmReturn.getReleaseDate()).isEqualTo("2009-12-17");
        assertThat(filmReturn.getDuration()).isEqualTo(160L);
        assertThat(filmReturn.getMpa().getId()).isEqualTo(1);
    }

}
