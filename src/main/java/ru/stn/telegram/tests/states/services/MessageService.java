package ru.stn.telegram.tests.states.services;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface MessageService {
    @Getter
    @RequiredArgsConstructor
    class Post {
        private final long chatId;
        private final int postId;
    }

    Post getForwardedChannelPost(Message message);
}
