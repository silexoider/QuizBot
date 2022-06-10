package ru.stn.telegram.tests.states.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.stn.telegram.tests.states.entities.sessions.Session;

@Repository
public interface SessionRepository extends CrudRepository<Session, Long> {
}
