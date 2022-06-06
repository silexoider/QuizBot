package ru.stn.telegram.quiz.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.stn.telegram.quiz.entities.Answer;

import java.util.Optional;

@Repository
public interface AnswerRepository extends CrudRepository<Answer, Long> {
    Optional<Answer> findByChatIdAndUserIdAndPostId(long chatId, long userId, int postId);
    @Query("select Sum(balance) from Answer where chatId = :chatId and userId = :userId")
    int getChatBalance(long chatId, long userId);
}
