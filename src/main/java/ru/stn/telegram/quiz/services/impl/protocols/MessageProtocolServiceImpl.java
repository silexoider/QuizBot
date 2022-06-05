package ru.stn.telegram.quiz.services.impl.protocols;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import ru.stn.telegram.quiz.entities.Session;
import ru.stn.telegram.quiz.services.ActionService;
import ru.stn.telegram.quiz.services.LocalizationService;
import ru.stn.telegram.quiz.services.QuestionService;
import ru.stn.telegram.quiz.services.SessionService;
import ru.stn.telegram.quiz.telegram.Config;

import java.util.Arrays;
import java.util.ResourceBundle;

@Service("MessageProtocol")
public class MessageProtocolServiceImpl extends CommonProtocolServiceImpl {
    private final QuestionService questionService;

    public MessageProtocolServiceImpl(Config config, ActionService actionService, SessionService sessionService, LocalizationService localizationService, QuestionService questionService) {
        super(Session.State.FORWARD, config, actionService, sessionService, localizationService);
        this.questionService = questionService;
        this.transitions.put(Session.State.FORWARD, Session.State.MESSAGE);
        this.transitions.put(Session.State.MESSAGE, Session.State.DEFAULT);
    }

    @Override
    protected BotApiMethod<?> commit(Session session, ResourceBundle resourceBundle) {
        questionService.setMessage(session.getChatId(), session.getPostId(), session.getMessage());
        return actionService.sendPrivateMessage(session.getUserId(), localizationService.getSuccess(resourceBundle));
    }
}
