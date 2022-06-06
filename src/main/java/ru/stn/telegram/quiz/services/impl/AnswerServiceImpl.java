package ru.stn.telegram.quiz.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.stn.telegram.quiz.entities.Answer;
import ru.stn.telegram.quiz.entities.Question;
import ru.stn.telegram.quiz.repositories.AnswerRepository;
import ru.stn.telegram.quiz.services.AnswerService;

@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {
    private final AnswerRepository answerRepository;

    @Override
    public Answer find(long chatId, long userId, int postId) {
        return answerRepository.findByChatIdAndUserIdAndPostId(chatId, userId, postId).orElse(null);
    }

    @Override
    public Answer get(long chatId, long userId, int postId) {
        Answer answer = find(chatId, userId, postId);
        if (answer == null) {
            answer = new Answer(chatId, userId, postId);
            answerRepository.save(answer);
        }
        return answer;
    }

    private boolean isAllowedToAnswer(Answer answer, Question question) {
        return !answer.isCorrect() && answer.getAttempts() < question.getMaximum();
    }

    @Override
    public boolean processCorrect(Answer answer, Question question) {
        if (!answer.isCorrect()) {
            answer.setCorrect(true);
            answer.setBalance(answer.getBalance() + question.getCorrect());
            answerRepository.save(answer);
            return true;
        }
        return false;
    }

    @Override
    public boolean processAttempt(Answer answer, Question question) {
        if (isAllowedToAnswer(answer, question)) {
            answer.setAttempts(answer.getAttempts() + 1);
            answer.setBalance(answer.getBalance() + question.getAttempt());
            answerRepository.save(answer);
            return true;
        }
        return false;
    }

    @Override
    public void pay(Answer answer, int amount) {
        answer.setBalance(answer.getBalance() + amount);
        answerRepository.save(answer);
    }

    @Override
    public int getChatBalance(Answer answer) {
        return answerRepository.getChatBalance(answer.getChatId(), answer.getUserId());
    }
}
