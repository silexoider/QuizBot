package ru.stn.telegram.quiz.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.stn.telegram.quiz.entities.Question;

import java.util.Optional;

@Repository
public interface QuestionRepository extends CrudRepository<Question, Long> {
    Optional<Question> findByChatIdAndPostId(long chatId, int postId);
}
