package ru.stn.telegram.tests.states.protocols.states.comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import ru.stn.telegram.tests.states.entities.sessions.Session;
import ru.stn.telegram.tests.states.exceptions.InsufficientPrivilegesException;
import ru.stn.telegram.tests.states.exceptions.InvalidInputException;
import ru.stn.telegram.tests.states.localization.Entry;
import ru.stn.telegram.tests.states.protocols.basics.BaseState;
import ru.stn.telegram.tests.states.protocols.states.forward.ForwardContext;
import ru.stn.telegram.tests.states.services.MessageService;

import java.util.ResourceBundle;

@Component
public class CommentState extends BaseState<CommentContext> {
    @Autowired
    private MessageService messageService;

    public CommentState() {
        super(Entry.COMMENT_PROMPT);
    }

    @Override
    public void process(Session session, CommentContext context, Message message, ResourceBundle resourceBundle) {
        if (message.getForwardFrom() == null) {
            throw new InvalidInputException();
        }
        context.setUserId(message.getForwardFrom().getId());
        sessionService.save(session);
    }
}
