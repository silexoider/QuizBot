package ru.stn.telegram.tests.states.protocols.states.forward;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import ru.stn.telegram.tests.states.entities.sessions.Session;
import ru.stn.telegram.tests.states.exceptions.InsufficientPrivilegesException;
import ru.stn.telegram.tests.states.exceptions.InvalidInputException;
import ru.stn.telegram.tests.states.localization.Entry;
import ru.stn.telegram.tests.states.protocols.basics.BaseState;
import ru.stn.telegram.tests.states.services.MessageService;
import ru.stn.telegram.tests.states.services.SessionService;

import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public abstract class ForwardState extends BaseState<ForwardContext> {
    @Autowired
    private SessionService sessionService;
    @Autowired
    private MessageService messageService;

    public ForwardState() {
        super(Entry.FORWARD_PROMPT);
    }

    protected abstract boolean checkChatMember(ChatMember chatMember);

    @Override
    public void process(Session session, ForwardContext context, Message message, ResourceBundle resourceBundle) {
        MessageService.Post post = messageService.getForwardedChannelPost(message);
        if (post == null) {
            throw new InvalidInputException();
        }
        ChatMember chatMember = botService.getChatMember(message.getForwardFromChat().getId(), session.getUserId());
        if (chatMember == null || !checkChatMember(chatMember)) {
            throw new InsufficientPrivilegesException();
        }
        context.setChatId(message.getForwardFromChat().getId());
        context.setPostId(message.getForwardFromMessageId());
        sessionService.save(session);
    }
}
