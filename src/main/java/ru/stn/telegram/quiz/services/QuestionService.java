package ru.stn.telegram.quiz.services;

import org.telegram.telegrambots.meta.api.objects.Message;
import ru.stn.telegram.quiz.entities.Question;

public interface QuestionService {
    Question find(long chatId, int postId);
    Question setQuestionBrief(long chatId, int postId, String keyword, String message, int timeout);
    Question setQuestionFull(long chatId, int postId, String keyword, String message, int timeout, int correct, int attempt, int maximum);
    boolean setKeyword(long chatId, int postId, String keyword);
    boolean setMessage(long chatId, int postId, String message);
    boolean setTimeout(long chatId, int postId, int timeout);
    boolean checkKeyword(Question question, Message message);
    boolean checkTimeout(Question question, Message message);
}
