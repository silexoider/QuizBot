package ru.stn.telegram.tests.states.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.stn.telegram.tests.states.services.BotService;
import ru.stn.telegram.tests.states.telegram.Bot;

@Service
@RequiredArgsConstructor
public class BotServiceImpl implements BotService {
    @Autowired
    private Bot bot;

    @Override
    public void sendMessage(long chatId, String text) {
        SendMessage method = new SendMessage(Long.valueOf(chatId).toString(), text);
        try {
            bot.execute(method);
        }
        catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ChatMember getChatMember(long chatId, long userId) {
        try {
            return bot.execute(new GetChatMember(Long.valueOf(chatId).toString(), userId));
        } catch (TelegramApiException e) {
            e.printStackTrace();
            return null;
        }
    }
}
