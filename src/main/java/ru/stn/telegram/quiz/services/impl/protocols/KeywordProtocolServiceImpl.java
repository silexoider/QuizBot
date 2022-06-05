package ru.stn.telegram.quiz.services.impl.protocols;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.stn.telegram.quiz.entities.Session;
import ru.stn.telegram.quiz.services.ActionService;
import ru.stn.telegram.quiz.services.LocalizationService;
import ru.stn.telegram.quiz.services.QuestionService;
import ru.stn.telegram.quiz.services.SessionService;
import ru.stn.telegram.quiz.telegram.Config;

import java.util.Arrays;
import java.util.ResourceBundle;

@Service("KeywordProtocol")
public class KeywordProtocolServiceImpl extends CommonProtocolServiceImpl {
    private final QuestionService questionService;

    public KeywordProtocolServiceImpl(Config config, ActionService actionService, SessionService sessionService, LocalizationService localizationService, QuestionService questionService) {
        super(Session.State.FORWARD, config, actionService, sessionService, localizationService);
        this.questionService = questionService;
        this.transitions.put(Session.State.KEYWORD, Session.State.DEFAULT);
    }

    @Override
    protected BotApiMethod<?> commit(Session session, ResourceBundle resourceBundle) {
        questionService.setKeyword(session.getChatId(), session.getPostId(), session.getKeyword());
        return actionService.sendPrivateMessage(session.getUserId(), localizationService.getSuccess(resourceBundle));
    }
}
