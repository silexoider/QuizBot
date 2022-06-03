package ru.stn.telegram.quiz.services;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface BotService {
    ChatMember getChatMember(long chatId, long userId) throws TelegramApiException;
}
