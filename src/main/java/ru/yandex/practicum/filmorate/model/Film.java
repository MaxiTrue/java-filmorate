package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class Film {

    private long id;
    private final String name;
    private final String description;
    private final LocalDate releaseDate;
    private final Long duration;
    private final Mpa mpa;
    private final Set<Long> likes;
    private final Set<Genre> genres;

}
