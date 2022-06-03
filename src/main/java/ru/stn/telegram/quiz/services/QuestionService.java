package ru.stn.telegram.quiz.services;

import ru.stn.telegram.quiz.entities.Question;

public interface QuestionService {
    Question find(long chatId, int postId);
    Question setQuestion(long chatId, int postId, String text);
}
