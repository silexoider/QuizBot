package ru.stn.telegram.quiz.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.stn.telegram.quiz.entities.Question;
import ru.stn.telegram.quiz.services.*;

import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

@Service
@RequiredArgsConstructor
public class ActionServiceImpl implements ActionService {
    private static List<String> superUserChatMemberTypes = Arrays.asList("creator", "administrator");

    private final BotService botService;
    private final QuestionService questionService;
    private final LocalizationService localizationService;

    @Override
    public boolean isSuperUser(long chatId, long userId) {
        try {
            ChatMember chatMember = botService.getChatMember(chatId, userId);
            return superUserChatMemberTypes.contains(chatMember.getStatus());
        } catch (TelegramApiException e) {
            return false;
        }
    }

    @Override
    public Post getPublicPost(Message message) {
        if (message.getReplyToMessage() == null || message.getReplyToMessage().getForwardFromChat() == null || !message.getReplyToMessage().getForwardFromChat().getType().equals("channel")) {
            return null;
        }
        Long chatId = message.getReplyToMessage().getForwardFromChat().getId();
        Integer postId = message.getReplyToMessage().getForwardFromMessageId();
        if (chatId == null || postId == null) {
            return null;
        }
        return new Post(chatId, postId);
    }
    @Override
    public Post getPublicPostForward(Message message) {
        return null;
    }
    @Override
    public Post getPrivatePost(Message message) {
        if (message == null || message.getForwardFromChat() == null || !message.getForwardFromChat().getType().equals("channel")) {
            return null;
        }
        Long chatId = message.getForwardFromChat().getId();
        Integer postId = message.getForwardFromMessageId();
        if (chatId == null || postId == null) {
            return null;
        }
        return new Post(chatId, postId);
    }
    @Override
    public boolean checkPrivatePost(Message message) {
        return message.getChat().isUserChat();
    }
    @Override
    public boolean checkPublicPost(Message message) {
        return !message.getChat().isUserChat() && message.getReplyToMessage() != null;
    }

    @Override
    public BotApiMethod<?> sendPrivateMessage(long userId, String message) {
        return new SendMessage(Long.valueOf(userId).toString(), message);
    }
}
