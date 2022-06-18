package ru.stn.telegram.tests.states.protocols.protocols.question;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.stn.telegram.tests.states.entities.Question;
import ru.stn.telegram.tests.states.entities.sessions.Session;
import ru.stn.telegram.tests.states.localization.Entry;
import ru.stn.telegram.tests.states.localization.Localizer;
import ru.stn.telegram.tests.states.protocols.basics.BaseNavigator;
import ru.stn.telegram.tests.states.protocols.basics.BaseProtocol;
import ru.stn.telegram.tests.states.protocols.states.attempt.AttemptState;
import ru.stn.telegram.tests.states.protocols.states.correct.CorrectState;
import ru.stn.telegram.tests.states.protocols.states.forward.ForwardState;
import ru.stn.telegram.tests.states.protocols.states.forward.PrivateForwardState;
import ru.stn.telegram.tests.states.protocols.states.keyword.KeywordState;
import ru.stn.telegram.tests.states.protocols.states.maximum.MaximumState;
import ru.stn.telegram.tests.states.protocols.states.message.MessageState;
import ru.stn.telegram.tests.states.protocols.states.timeout.TimeoutState;
import ru.stn.telegram.tests.states.services.BotService;
import ru.stn.telegram.tests.states.services.QuestionService;
import ru.stn.telegram.tests.states.services.SessionService;

import java.util.ResourceBundle;

@Component("FullQuestionProtocol")
public class FullQuestionProtocol extends BaseQuestionProtocol {
    public FullQuestionProtocol(
            PrivateForwardState forwardState,
            KeywordState keywordState,
            MessageState messageState,
            TimeoutState timeoutState,
            CorrectState correctState,
            AttemptState attemptState,
            MaximumState maximumState,
            BotService botService,
            Localizer localizer
    ) {
        super(
                new BaseNavigator<>(forwardState, botService, localizer),
                new BaseNavigator<>(keywordState, botService, localizer),
                new BaseNavigator<>(messageState, botService, localizer),
                new BaseNavigator<>(timeoutState, botService, localizer),
                new BaseNavigator<>(correctState, botService, localizer),
                new BaseNavigator<>(attemptState, botService, localizer),
                new BaseNavigator<>(maximumState, botService, localizer)
        );
    }

    @Override
    public boolean commitInternal(Session session, Question question, QuestionContext context, ResourceBundle resourceBundle) {
        questionService.setFull(
                context.getChatId(),
                context.getPostId(),
                context.getKeyword(),
                context.getMessage(),
                context.getTimeout(),
                context.getCorrect(),
                context.getAttempt(),
                context.getMaximum()
        );
        return true;
    }
}
