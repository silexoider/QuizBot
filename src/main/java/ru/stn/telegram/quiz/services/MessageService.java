package ru.stn.telegram.quiz.services;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface MessageService {
    BotApiMethod<?> process(Message message);
}
