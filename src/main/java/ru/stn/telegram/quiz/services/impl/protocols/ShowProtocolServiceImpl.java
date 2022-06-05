package ru.stn.telegram.quiz.services.impl.protocols;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.stn.telegram.quiz.entities.Question;
import ru.stn.telegram.quiz.entities.Session;
import ru.stn.telegram.quiz.services.ActionService;
import ru.stn.telegram.quiz.services.LocalizationService;
import ru.stn.telegram.quiz.services.QuestionService;
import ru.stn.telegram.quiz.services.SessionService;
import ru.stn.telegram.quiz.telegram.Config;

import java.util.Arrays;
import java.util.ResourceBundle;

@Service("ShowProtocol")
public class ShowProtocolServiceImpl extends CommonProtocolServiceImpl {
    private final QuestionService questionService;

    public ShowProtocolServiceImpl(Config config, ActionService actionService, SessionService sessionService, LocalizationService localizationService, QuestionService questionService) {
        super(Session.State.FORWARD, config, actionService, sessionService, localizationService);
        this.questionService = questionService;
        this.transitions.put(Session.State.FORWARD, Session.State.DEFAULT);
    }

    @Override
    protected BotApiMethod<?> commit(Session session, ResourceBundle resourceBundle) {
        Question question = questionService.find(session.getChatId(), session.getPostId());
        if (question == null) {
            return actionService.sendPrivateMessage(session.getUserId(), localizationService.getNoQuestion(resourceBundle));
        } else {
            return actionService.sendPrivateMessage(
                    session.getUserId(),
                    String.format(
                            localizationService.getQuestionFormat(resourceBundle),
                            question.getKeyword(),
                            question.getMessage(),
                            question.getTimeout() / 60 / 60
                    )
            );
        }
    }
}
