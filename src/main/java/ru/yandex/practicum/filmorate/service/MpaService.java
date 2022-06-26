package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class MpaService {

    private final MpaDao mpaDao;

    public Collection<Mpa> findAllMpa() {
        return mpaDao.findAllMpa();
    }

    public Mpa findMpaById(Integer mpaId) throws ObjectNotFoundException {
        return mpaDao.findMpaById(mpaId);
    }

}
