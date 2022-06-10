package ru.stn.telegram.tests.states.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.stn.telegram.tests.states.entities.Channel;

@Repository
public interface ChannelRepository extends CrudRepository<Channel, Long> {
}
