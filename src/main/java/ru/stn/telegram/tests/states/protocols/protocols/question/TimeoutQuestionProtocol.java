package ru.stn.telegram.tests.states.protocols.protocols.question;

import org.springframework.stereotype.Component;
import ru.stn.telegram.tests.states.entities.Question;
import ru.stn.telegram.tests.states.localization.Localizer;
import ru.stn.telegram.tests.states.protocols.states.forward.ForwardState;
import ru.stn.telegram.tests.states.protocols.states.forward.PrivateForwardState;
import ru.stn.telegram.tests.states.protocols.states.timeout.TimeoutState;
import ru.stn.telegram.tests.states.services.BotService;
import ru.stn.telegram.tests.states.services.QuestionService;
import ru.stn.telegram.tests.states.services.SessionService;

@Component("TimeoutQuestionProtocol")
public class TimeoutQuestionProtocol extends SingleValueQuestionProtocol {
    public TimeoutQuestionProtocol(
            PrivateForwardState forwardState,
            TimeoutState timeoutState,
            QuestionService questionService,
            BotService botService,
            Localizer localizer
    ) {
        super(forwardState, timeoutState, questionService, botService, localizer);
    }

    @Override
    protected void commitInternal2(Question question, QuestionContext context) {
        questionService.setTimeout(question, context.getTimeout());
    }
}
