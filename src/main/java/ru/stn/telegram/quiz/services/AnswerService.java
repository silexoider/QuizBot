package ru.stn.telegram.quiz.services;

import ru.stn.telegram.quiz.entities.Answer;
import ru.stn.telegram.quiz.entities.Question;

public interface AnswerService {
    Answer find(long chatId, long userId, int postId);
    Answer get(long chatId, long userId, int postId);
    boolean processCorrect(Answer answer, Question question);
    boolean processAttempt(Answer answer, Question question);
    int getChatBalance(Answer answer);
}
