package ru.stn.telegram.tests.states.protocols.protocols.question;

import net.bytebuddy.asm.Advice;
import org.springframework.stereotype.Component;
import ru.stn.telegram.tests.states.entities.Question;
import ru.stn.telegram.tests.states.localization.Localizer;
import ru.stn.telegram.tests.states.protocols.states.forward.ForwardState;
import ru.stn.telegram.tests.states.protocols.states.message.MessageState;
import ru.stn.telegram.tests.states.services.BotService;
import ru.stn.telegram.tests.states.services.QuestionService;
import ru.stn.telegram.tests.states.services.SessionService;

@Component("MessageQuestionProtocol")
public class MessageQuestionProtocol extends SingleValueQuestionProtocol {
    public MessageQuestionProtocol(
            ForwardState forwardState,
            MessageState messageState,
            QuestionService questionService,
            BotService botService,
            Localizer localizer
    ) {
        super(forwardState, messageState, questionService, botService, localizer);
    }

    @Override
    protected void commitInternal2(Question question, QuestionContext context) {
        questionService.setMessage(question, context.getMessage());
    }
}
