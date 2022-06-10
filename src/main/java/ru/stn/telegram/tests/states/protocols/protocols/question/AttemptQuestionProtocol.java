package ru.stn.telegram.tests.states.protocols.protocols.question;

import org.springframework.stereotype.Component;
import ru.stn.telegram.tests.states.entities.Question;
import ru.stn.telegram.tests.states.localization.Localizer;
import ru.stn.telegram.tests.states.protocols.states.attempt.AttemptState;
import ru.stn.telegram.tests.states.protocols.states.forward.ForwardState;
import ru.stn.telegram.tests.states.services.BotService;
import ru.stn.telegram.tests.states.services.QuestionService;
import ru.stn.telegram.tests.states.services.SessionService;

@Component("AttemptQuestionProtocol")
public class AttemptQuestionProtocol extends SingleValueQuestionProtocol {
    public AttemptQuestionProtocol(
            ForwardState forwardState,
            AttemptState attemptState,
            QuestionService questionService,
            BotService botService,
            Localizer localizer
    ) {
        super(forwardState, attemptState, questionService, botService, localizer);
    }

    @Override
    protected void commitInternal2(Question question, QuestionContext context) {
        questionService.setAttempt(question, context.getAttempt());
    }
}
