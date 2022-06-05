package ru.stn.telegram.quiz.services;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.stn.telegram.quiz.entities.Question;

import java.util.ResourceBundle;

public interface ActionService {
    @Getter
    @RequiredArgsConstructor
    class Post {
        private final long chatId;
        private final int postId;
    }

    boolean isSuperUser(long chatId, long userId);
    Post getPublicPost(Message message);
    Post getPrivatePost(Message message);
    boolean checkPrivatePost(Message message);
    boolean checkPublicPost(Message message);

    BotApiMethod<?> sendPrivateMessage(long userId, String message);
}
