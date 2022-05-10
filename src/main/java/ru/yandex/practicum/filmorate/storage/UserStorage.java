package ru.yandex.practicum.filmorate.storage;

public interface UserStorage<T> extends Storage<T> {

    Integer getSize();

    Boolean containsUser(Long id);
}
