package ru.stn.telegram.tests.states.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.CrudRepository;
import ru.stn.telegram.tests.states.entities.Question;

import java.util.function.BiConsumer;

@RequiredArgsConstructor
public abstract class BaseEntityServiceImpl<S, ID, R extends CrudRepository<S, ID>> {
    protected final R repository;

    protected <V> void setAttribute(S entity, BiConsumer<S, V> setter, V value) {
        setter.accept(entity, value);
        repository.save(entity);
    }
}
