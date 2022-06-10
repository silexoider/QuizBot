package ru.stn.telegram.tests.states.protocols.protocols.question;

import org.springframework.stereotype.Component;
import ru.stn.telegram.tests.states.entities.Question;
import ru.stn.telegram.tests.states.localization.Localizer;
import ru.stn.telegram.tests.states.protocols.states.forward.ForwardState;
import ru.stn.telegram.tests.states.protocols.states.maximum.MaximumState;
import ru.stn.telegram.tests.states.services.BotService;
import ru.stn.telegram.tests.states.services.QuestionService;
import ru.stn.telegram.tests.states.services.SessionService;

@Component("MaximumQuestionProtocol")
public class MaximumQuestionProtocol extends SingleValueQuestionProtocol {
    public MaximumQuestionProtocol(
            ForwardState forwardState,
            MaximumState maximumState,
            QuestionService questionService,
            BotService botService,
            Localizer localizer
    ) {
        super(forwardState, maximumState, questionService, botService, localizer);
    }

    @Override
    protected void commitInternal2(Question question, QuestionContext context) {
        questionService.setMaximum(question, context.getMaximum());
    }
}
