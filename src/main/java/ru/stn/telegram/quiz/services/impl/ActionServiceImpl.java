package ru.stn.telegram.quiz.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
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

    private boolean isSuperUser(long chatId, long userId) {
        try {
            ChatMember chatMember = botService.getChatMember(chatId, userId);
            return superUserChatMemberTypes.contains(chatMember.getStatus());
        } catch (TelegramApiException e) {
            return false;
        }
    }

    @Override
    public BotApiMethod<?> sendPrivateMessage(long userId, String message) {
        return new SendMessage(Long.valueOf(userId).toString(), message);
    }

    @Override
    public Question getQuestion(long chatId, int postId, long userId) {
        return questionService.find(chatId, postId);
    }

    @Override
    public boolean setQuestion(long chatId, int postId, long userId, String text, ResourceBundle resourceBundle) {
        if (isSuperUser(chatId, userId)) {
            questionService.setQuestion(chatId, postId, text);
            return true;
        } else {
            return false;
        }
    }
}
