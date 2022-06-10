package ru.stn.telegram.tests.states.protocols.protocols.question;

import org.springframework.stereotype.Component;
import ru.stn.telegram.tests.states.entities.Question;
import ru.stn.telegram.tests.states.localization.Localizer;
import ru.stn.telegram.tests.states.protocols.states.forward.ForwardState;
import ru.stn.telegram.tests.states.protocols.states.keyword.KeywordState;
import ru.stn.telegram.tests.states.services.BotService;
import ru.stn.telegram.tests.states.services.QuestionService;
import ru.stn.telegram.tests.states.services.SessionService;

@Component("KeywordQuestionProtocol")
public class KeywordQuestionProtocol extends SingleValueQuestionProtocol {
    public KeywordQuestionProtocol(
            ForwardState forwardState,
            KeywordState keywordState,
            QuestionService questionService,
            BotService botService,
            Localizer localizer
    ) {
        super(forwardState, keywordState, questionService, botService, localizer);
    }

    @Override
    protected void commitInternal2(Question question, QuestionContext context) {
        questionService.setKeyword(question, context.getKeyword());
    }
}
