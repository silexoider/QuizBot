package ru.stn.telegram.quiz.services.impl.protocols;

import org.telegram.telegrambots.meta.api.objects.Message;
import ru.stn.telegram.quiz.entities.Session;
import ru.stn.telegram.quiz.exceptions.PartFailureException;
import ru.stn.telegram.quiz.services.ActionService;
import ru.stn.telegram.quiz.services.LocalizationService;
import ru.stn.telegram.quiz.services.QuestionService;
import ru.stn.telegram.quiz.services.SessionService;
import ru.stn.telegram.quiz.telegram.Config;

import java.util.ResourceBundle;

public abstract class CommonPartProtocolServiceImpl extends CommonProtocolServiceImpl {
    protected final QuestionService questionService;

    public CommonPartProtocolServiceImpl(Session.State initialState, Config config, ActionService actionService, SessionService sessionService, LocalizationService localizationService, QuestionService questionService) {
        super(initialState, config, actionService, sessionService, localizationService);
        this.questionService = questionService;
    }

    @Override
    protected ActionService.Post checkForward(Session session, Message message, ResourceBundle resourceBundle) {
        ActionService.Post post = super.checkForward(session, message, resourceBundle);
        if (questionService.find(post.getChatId(), post.getPostId()) == null) {
            throw new PartFailureException();
        }
        return post;
    }
}
