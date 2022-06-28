package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@EqualsAndHashCode
@ToString
public class Mpa {
    private final int id;
    private final String name;

    @JsonCreator
    public Mpa(@JsonProperty("id") Integer id) {
        this.id = id;
        this.name = TypeOfMpa.values()[id - 1].getTitle();
    }

}
