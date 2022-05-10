package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {

    private long id;
    private final String email;
    private final String login;
    private String name;
    private final LocalDate birthday;
    private final Set<Long> friends = new HashSet<>();

}
