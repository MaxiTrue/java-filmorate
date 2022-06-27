package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;

@Component
@RequiredArgsConstructor
public class MpaDao {

    private final JdbcTemplate jdbcTemplate;

    private static final String SQL_GET_ALL_MPA = "SELECT * FROM mpa";
    private static final String SQL_GET_MPA_ID = "SELECT * FROM mpa WHERE id = ?";

    /**
     * Получение всех категорий
     */
    public Collection<Mpa> findAll() {
        return jdbcTemplate.query(SQL_GET_ALL_MPA, (rs, rowNum) -> new Mpa(rs.getInt("id")));
    }

    /**
     * Получение категории по id
     */
    public Mpa findById(Integer mpaId) throws ObjectNotFoundException {
        SqlRowSet rs = jdbcTemplate.queryForRowSet(SQL_GET_MPA_ID, mpaId);

        if (rs.next()) {
            return new Mpa(rs.getInt("id"));
        }
        throw new ObjectNotFoundException("mpa", mpaId);
    }


}
