package ru.stn.telegram.quiz.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.stn.telegram.quiz.entities.Session;

@Repository
public interface SessionRepository extends CrudRepository<Session, Long> {
}
