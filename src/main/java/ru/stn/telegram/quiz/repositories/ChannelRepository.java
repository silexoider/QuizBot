package ru.stn.telegram.quiz.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.stn.telegram.quiz.entities.Channel;

@Repository
public interface ChannelRepository extends CrudRepository<Channel, Long> {
}
