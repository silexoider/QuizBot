package ru.stn.telegram.tests.states.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.stn.telegram.tests.states.entities.Answer;
import ru.stn.telegram.tests.states.entities.Question;
import ru.stn.telegram.tests.states.repositories.AnswerRepository;
import ru.stn.telegram.tests.states.services.AnswerService;

@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {
    private final AnswerRepository repository;

    private boolean isAllowedToAnswer(Answer answer, Question question) {
        return !answer.isCorrect() && answer.getAttempts() < question.getMaximum();
    }

    @Override
    public Answer find(long chatId, int postId, long userId) {
        return repository.findByChatIdAndPostIdAndUserId(chatId, postId, userId).orElse(null);
    }

    @Override
    public Answer get(long chatId, int postId, long userId) {
        Answer answer = find(chatId, postId, userId);
        if (answer == null) {
            answer = new Answer(chatId, postId, userId);
            repository.save(answer);
        }
        return answer;
    }

    @Override
    public boolean processCorrect(Answer answer, Question question) {
        if (!answer.isCorrect()) {
            answer.setCorrect(true);
            answer.setBalance(answer.getBalance() + question.getCorrect());
            repository.save(answer);
            return true;
        }
        return false;
    }

    @Override
    public boolean processAttempt(Answer answer, Question question) {
        if (isAllowedToAnswer(answer, question)) {
            answer.setAttempts(answer.getAttempts() + 1);
            answer.setBalance(answer.getBalance() + question.getAttempt());
            repository.save(answer);
            return true;
        }
        return false;
    }

    @Override
    public void pay(Answer answer, int amount) {
        answer.setBalance(answer.getBalance() + amount);
        repository.save(answer);
    }

    @Override
    public int getChatBalance(Answer answer) {
        return getChatBalance(answer.getChatId(), answer.getUserId());
    }

    @Override
    public int getChatBalance(long chatId, long userId) {
        Integer value = repository.getChatBalance(chatId, userId);
        if (value == null) {
            return 0;
        } else {
            return value;
        }
    }
}
