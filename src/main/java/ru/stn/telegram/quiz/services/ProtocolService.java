package ru.stn.telegram.quiz.services;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.stn.telegram.quiz.entities.Session;

import java.util.ResourceBundle;

public interface ProtocolService {
    Session.State getInitialState();
    BotApiMethod<?> processForward(Session session, Message message, ResourceBundle resourceBundle);
    BotApiMethod<?> processKeyword(Session session, Message message, ResourceBundle resourceBundle);
    BotApiMethod<?> processMessage(Session session, Message message, ResourceBundle resourceBundle);
    BotApiMethod<?> processTimeout(Session session, Message message, ResourceBundle resourceBundle);
    BotApiMethod<?> processCorrect(Session session, Message message, ResourceBundle resourceBundle);
    BotApiMethod<?> processAttempt(Session session, Message message, ResourceBundle resourceBundle);
    BotApiMethod<?> processMaximum(Session session, Message message, ResourceBundle resourceBundle);
    BotApiMethod<?> processCurrencySingular(Session session, Message message, ResourceBundle resourceBundle);
    BotApiMethod<?> processCurrencyDual(Session session, Message message, ResourceBundle resourceBundle);
    BotApiMethod<?> processCurrencyPlural(Session session, Message message, ResourceBundle resourceBundle);
}
