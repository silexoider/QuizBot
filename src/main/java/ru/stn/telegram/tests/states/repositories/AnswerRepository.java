package ru.stn.telegram.tests.states.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.stn.telegram.tests.states.entities.Answer;

import java.util.Optional;

@Repository
public interface AnswerRepository extends CrudRepository<Answer, Long> {
    Optional<Answer> findByChatIdAndPostIdAndUserId(long chatId, int postId, long userId);
    @Query("select sum(balance) from Answer where chatId = :chatId and userId = :userId")
    Integer getChatBalance(long chatId, long userId);
}
