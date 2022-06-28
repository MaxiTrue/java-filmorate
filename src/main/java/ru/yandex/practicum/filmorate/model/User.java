package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;
import java.util.Set;


@Data
@Builder
public class User {

    private long id;
    private final String email;
    private final String login;
    private String name;
    private final LocalDate birthday;
    private final Set<Long> friends;

}
