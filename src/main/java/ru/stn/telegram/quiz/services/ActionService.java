package ru.stn.telegram.quiz.services;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import ru.stn.telegram.quiz.entities.Question;

import java.util.ResourceBundle;

public interface ActionService {
    BotApiMethod<?> sendPrivateMessage(long userId, String message);
    Question getQuestion(long chatId, int postId, long userId);
    boolean setQuestion(long chatId, int postId, long userId, String text, ResourceBundle resourceBundle);
}
