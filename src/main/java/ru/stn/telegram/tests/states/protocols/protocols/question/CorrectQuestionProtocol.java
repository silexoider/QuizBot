package ru.stn.telegram.tests.states.protocols.protocols.question;

import org.springframework.stereotype.Component;
import ru.stn.telegram.tests.states.entities.Question;
import ru.stn.telegram.tests.states.localization.Localizer;
import ru.stn.telegram.tests.states.protocols.states.correct.CorrectState;
import ru.stn.telegram.tests.states.protocols.states.forward.ForwardState;
import ru.stn.telegram.tests.states.protocols.states.forward.PrivateForwardState;
import ru.stn.telegram.tests.states.services.BotService;
import ru.stn.telegram.tests.states.services.QuestionService;
import ru.stn.telegram.tests.states.services.SessionService;

@Component("CorrectQuestionProtocol")
public class CorrectQuestionProtocol extends SingleValueQuestionProtocol {
    public CorrectQuestionProtocol(
            PrivateForwardState forwardState,
            CorrectState correctState,
            QuestionService questionService,
            BotService botService,
            Localizer localizer
    ) {
        super(forwardState, correctState, questionService, botService, localizer);
    }

    @Override
    protected void commitInternal2(Question question, QuestionContext context) {
        questionService.setCorrect(question, context.getCorrect());
    }
}
