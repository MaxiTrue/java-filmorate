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
public class Genre {

    private final Integer id;
    private final String name;

    @JsonCreator
    public Genre(@JsonProperty("id") Integer id) {
        this.id = id;
        this.name = TypeOfGenre.values()[id - 1].getTitle();
    }
}
