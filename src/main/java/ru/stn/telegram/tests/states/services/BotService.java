package ru.stn.telegram.tests.states.services;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;

public interface BotService {
    void sendMessage(long chatId, String text);
    ChatMember getChatMember(long chatId, long userId);
}
