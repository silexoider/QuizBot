package ru.stn.telegram.tests.states.protocols.protocols.question;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.stn.telegram.tests.states.entities.Question;
import ru.stn.telegram.tests.states.entities.sessions.Session;
import ru.stn.telegram.tests.states.localization.Entry;
import ru.stn.telegram.tests.states.localization.Localizer;
import ru.stn.telegram.tests.states.protocols.basics.BaseNavigator;
import ru.stn.telegram.tests.states.protocols.basics.BaseProtocol;
import ru.stn.telegram.tests.states.protocols.states.forward.ForwardState;
import ru.stn.telegram.tests.states.protocols.states.forward.PrivateForwardState;
import ru.stn.telegram.tests.states.services.BotService;
import ru.stn.telegram.tests.states.services.QuestionService;

import java.util.ResourceBundle;

@Component("ShowQuestionProtocol")
public class ShowQuestionProtocol extends BaseProtocol<QuestionContext> {
    @Autowired
    private QuestionService questionService;

    public ShowQuestionProtocol(
            PrivateForwardState forwardState,
            BotService botService,
            Localizer localizer
    ) {
        super(new BaseNavigator<>(forwardState, botService, localizer));
    }

    @Override
    public void commit(Session session, QuestionContext context, ResourceBundle resourceBundle) {
        Question question = questionService.find(context.getChatId(), context.getPostId());
        String text = question == null ?
                localizer.localize(Entry.QUESTION_ABSENT, resourceBundle) :
                String.format(
                        localizer.localize(Entry.QUESTION_FORMAT, resourceBundle),
                        question.getKeyword(),
                        question.getMessage(),
                        question.getTimeout() / (60 * 60),
                        question.getCorrect(),
                        question.getAttempt(),
                        question.getMaximum()
                );
        botService.sendMessage(session.getUserId(), text);
    }
}
