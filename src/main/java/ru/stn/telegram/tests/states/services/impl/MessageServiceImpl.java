package ru.stn.telegram.tests.states.services.impl;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.stn.telegram.tests.states.services.MessageService;

@Service
public class MessageServiceImpl implements MessageService {
    private static final String CHANNEL_CHAT_TYPE_VALUE = "channel";

    private boolean isChannel(Chat chat) {
        return chat.getType().equals(CHANNEL_CHAT_TYPE_VALUE);
    }

    @Override
    public Post getForwardedChannelPost(Message message) {
        if (message.getForwardFromChat() == null || message.getForwardFromMessageId() == null || !isChannel(message.getForwardFromChat())) {
            return null;
        }
        return new Post(message.getForwardFromChat().getId(), message.getForwardFromMessageId());
    }
}
