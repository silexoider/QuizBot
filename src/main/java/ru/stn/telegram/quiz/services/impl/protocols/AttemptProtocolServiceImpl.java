package ru.stn.telegram.quiz.services.impl.protocols;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import ru.stn.telegram.quiz.entities.Session;
import ru.stn.telegram.quiz.services.ActionService;
import ru.stn.telegram.quiz.services.LocalizationService;
import ru.stn.telegram.quiz.services.QuestionService;
import ru.stn.telegram.quiz.services.SessionService;
import ru.stn.telegram.quiz.telegram.Config;

import java.util.ResourceBundle;

@Service("AttemptProtocol")
public class AttemptProtocolServiceImpl extends CommonPartProtocolServiceImpl {
    public AttemptProtocolServiceImpl(Config config, ActionService actionService, SessionService sessionService, LocalizationService localizationService, QuestionService questionService) {
        super(Session.State.FORWARD, config, actionService, sessionService, localizationService, questionService);
        this.transitions.put(Session.State.FORWARD, Session.State.ATTEMPT);
        this.transitions.put(Session.State.ATTEMPT, Session.State.DEFAULT);
    }

    @Override
    protected BotApiMethod<?> commit(Session session, ResourceBundle resourceBundle) {
        questionService.setAttempt(session.getChatId(), session.getPostId(), session.getAttempt());
        return actionService.sendPrivateMessage(session.getUserId(), localizationService.getSuccess(resourceBundle));
    }

}
