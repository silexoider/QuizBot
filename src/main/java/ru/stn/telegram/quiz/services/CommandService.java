package ru.stn.telegram.quiz.services;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.stn.telegram.quiz.entities.Session;

import java.util.ResourceBundle;

public interface CommandService {
    BotApiMethod<?> process(Session session, Message message, ResourceBundle resourceBundle);
}
