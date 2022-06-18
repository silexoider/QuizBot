package ru.stn.telegram.tests.states.protocols.protocols.question;

import org.springframework.stereotype.Component;
import ru.stn.telegram.tests.states.entities.Question;
import ru.stn.telegram.tests.states.entities.sessions.Session;
import ru.stn.telegram.tests.states.localization.Entry;
import ru.stn.telegram.tests.states.localization.Localizer;
import ru.stn.telegram.tests.states.protocols.basics.BaseNavigator;
import ru.stn.telegram.tests.states.protocols.basics.BaseProtocol;
import ru.stn.telegram.tests.states.protocols.states.forward.ForwardState;
import ru.stn.telegram.tests.states.protocols.states.forward.PrivateForwardState;
import ru.stn.telegram.tests.states.protocols.states.keyword.KeywordState;
import ru.stn.telegram.tests.states.protocols.states.message.MessageState;
import ru.stn.telegram.tests.states.protocols.states.timeout.TimeoutState;
import ru.stn.telegram.tests.states.services.BotService;
import ru.stn.telegram.tests.states.services.SessionService;

import java.util.ResourceBundle;

@Component("BriefQuestionProtocol")
public class BriefQuestionProtocol extends BaseQuestionProtocol {
    public BriefQuestionProtocol(
            PrivateForwardState forwardState,
            KeywordState keywordState,
            MessageState messageState,
            TimeoutState timeoutState,
            BotService botService,
            Localizer localizer
    ) {
        super(
                new BaseNavigator<>(forwardState, botService, localizer),
                new BaseNavigator<>(keywordState, botService, localizer),
                new BaseNavigator<>(messageState, botService, localizer),
                new BaseNavigator<>(timeoutState, botService, localizer)
        );
    }

    @Override
    protected boolean commitInternal(Session session, Question question, QuestionContext context, ResourceBundle resourceBundle) {
        questionService.setBrief(
                context.getChatId(),
                context.getPostId(),
                context.getKeyword(),
                context.getMessage(),
                context.getTimeout()
        );
        return true;
    }
}
