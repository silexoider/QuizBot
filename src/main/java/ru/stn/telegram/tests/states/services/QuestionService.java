package ru.stn.telegram.tests.states.services;

import org.telegram.telegrambots.meta.api.objects.Message;
import ru.stn.telegram.tests.states.entities.Question;

import java.time.Duration;
import java.time.Instant;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface QuestionService {
    Question find(long chatId, int postId);
    void setFull(long chatId, int postId, String keyword, String message, int timeout, int correct, int attempt, int maximum);
    void setBrief(long chatId, int postId, String keyword, String message, int timeout);
    void setPost(Question question, long chatId, int postId);
    void setKeyword(Question question, String keyword);
    void setMessage(Question question, String message);
    void setTimeout(Question question, int timeout);
    void setCorrect(Question question, int correct);
    void setAttempt(Question question, int attempt);
    void setMaximum(Question question, int maximum);

    boolean checkKeyword(Question question, Message message);
    boolean checkTimeout(Question question, Message message);
}
