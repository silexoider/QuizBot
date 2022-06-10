package ru.stn.telegram.tests.states.services;

import ru.stn.telegram.tests.states.entities.Answer;
import ru.stn.telegram.tests.states.entities.Question;

public interface AnswerService {
    Answer find(long chatId, int postId, long userId);
    Answer get(long chatId, int postId, long userId);
    boolean processCorrect(Answer answer, Question question);
    boolean processAttempt(Answer answer, Question question);
    void pay(Answer answer, int amount);
    int getChatBalance(Answer answer);
}
