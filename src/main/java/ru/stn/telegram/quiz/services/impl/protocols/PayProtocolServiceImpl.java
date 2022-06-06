package ru.stn.telegram.quiz.services.impl.protocols;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.stn.telegram.quiz.entities.Answer;
import ru.stn.telegram.quiz.entities.Session;
import ru.stn.telegram.quiz.exceptions.InsufficientPrivilegeException;
import ru.stn.telegram.quiz.exceptions.InvalidInputException;
import ru.stn.telegram.quiz.exceptions.PartFailureException;
import ru.stn.telegram.quiz.services.*;
import ru.stn.telegram.quiz.telegram.Config;

import java.util.ResourceBundle;

@Service("PayProtocol")
public class PayProtocolServiceImpl extends CommonProtocolServiceImpl {
    private final AnswerService answerService;

    public PayProtocolServiceImpl(Config config, ActionService actionService, SessionService sessionService, LocalizationService localizationService, AnswerService answerService) {
        super(Session.State.FORWARD, config, actionService, sessionService, localizationService);
        this.answerService = answerService;
        this.transitions.put(Session.State.FORWARD, Session.State.COMMENT);
        this.transitions.put(Session.State.COMMENT, Session.State.PAY);
        this.transitions.put(Session.State.PAY, Session.State.DEFAULT);
    }

    @Override
    protected BotApiMethod<?> commit(Session session, ResourceBundle resourceBundle) {
        Answer answer = answerService.get(session.getChatId(), session.getCommentUserId(), session.getPostId());
        answerService.pay(answer, session.getAmount());
        return actionService.sendPrivateMessage(session.getUserId(), localizationService.getSuccess(resourceBundle));
    }
}
