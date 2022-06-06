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

@Service("BriefProtocol")
public class BriefProtocolServiceImpl extends CommonProtocolServiceImpl {
    private final QuestionService questionService;

    public BriefProtocolServiceImpl(Config config, ActionService actionService, SessionService sessionService, LocalizationService localizationService, QuestionService questionService) {
        super(Session.State.FORWARD, config, actionService, sessionService, localizationService);
        this.questionService = questionService;
        this.transitions.put(Session.State.TIMEOUT, Session.State.DEFAULT);
    }

    @Override
    protected BotApiMethod<?> commit(Session session, ResourceBundle resourceBundle) {
        questionService.setQuestionBrief(
                session.getChatId(),
                session.getPostId(),
                session.getKeyword(),
                session.getMessage(),
                session.getTimeout()
        );
        return actionService.sendPrivateMessage(session.getUserId(), localizationService.getSuccess(resourceBundle));
    }
}
