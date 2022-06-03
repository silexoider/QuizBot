package ru.stn.telegram.quiz.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.stn.telegram.quiz.services.BotService;
import ru.stn.telegram.quiz.telegram.Bot;

@Service
public class BotServiceImpl implements BotService {
    private final Bot bot;

    public BotServiceImpl(@Lazy Bot bot) {
        this.bot = bot;
    }

    @Override
    public ChatMember getChatMember(long chatId, long userId) throws TelegramApiException {
        return bot.execute(new GetChatMember(Long.valueOf(chatId).toString(), userId));
    }
}
