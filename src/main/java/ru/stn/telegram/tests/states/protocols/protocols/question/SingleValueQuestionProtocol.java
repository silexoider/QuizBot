package ru.stn.telegram.tests.states.protocols.protocols.question;

import org.telegram.telegrambots.meta.api.objects.Message;
import ru.stn.telegram.tests.states.entities.Question;
import ru.stn.telegram.tests.states.entities.sessions.Session;
import ru.stn.telegram.tests.states.localization.Entry;
import ru.stn.telegram.tests.states.localization.Localizer;
import ru.stn.telegram.tests.states.protocols.basics.BaseNavigator;
import ru.stn.telegram.tests.states.protocols.basics.BaseProtocol;
import ru.stn.telegram.tests.states.protocols.basics.State;
import ru.stn.telegram.tests.states.protocols.basics.Transition;
import ru.stn.telegram.tests.states.protocols.states.forward.ForwardContext;
import ru.stn.telegram.tests.states.protocols.states.forward.ForwardState;
import ru.stn.telegram.tests.states.protocols.states.forward.PrivateForwardState;
import ru.stn.telegram.tests.states.services.BotService;
import ru.stn.telegram.tests.states.services.QuestionService;
import ru.stn.telegram.tests.states.services.SessionService;

import java.util.ResourceBundle;

public abstract class SingleValueQuestionProtocol extends BaseQuestionProtocol {
    public SingleValueQuestionProtocol(
            PrivateForwardState forwardState,
            State<? super QuestionContext> state,
            QuestionService questionService,
            BotService botService,
            Localizer localizer
    ) {
        super(
                new SingleValueNavigator(forwardState, botService, localizer, questionService),
                new BaseNavigator<>(state, botService, localizer)
        );
    }

    @Override
    protected boolean commitInternal(Session session, Question question, QuestionContext context, ResourceBundle resourceBundle) {
        if (question == null) {
            botService.sendMessage(session.getUserId(), localizer.localize(Entry.QUESTION_ABSENT, resourceBundle));
            return false;
        } else {
            commitInternal2(question, context);
            return true;
        }
    }

    protected abstract void commitInternal2(Question question, QuestionContext context);
}
