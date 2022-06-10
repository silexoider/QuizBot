package ru.stn.telegram.tests.states.protocols.protocols.question;

import org.telegram.telegrambots.meta.api.objects.Message;
import ru.stn.telegram.tests.states.entities.sessions.Session;
import ru.stn.telegram.tests.states.localization.Entry;
import ru.stn.telegram.tests.states.localization.Localizer;
import ru.stn.telegram.tests.states.protocols.basics.BaseNavigator;
import ru.stn.telegram.tests.states.protocols.basics.State;
import ru.stn.telegram.tests.states.protocols.basics.Transition;
import ru.stn.telegram.tests.states.protocols.states.forward.ForwardContext;
import ru.stn.telegram.tests.states.services.BotService;
import ru.stn.telegram.tests.states.services.QuestionService;

import java.util.ResourceBundle;

public class SingleValueNavigator extends BaseNavigator<ForwardContext, QuestionContext> {
    private final QuestionService questionService;

    public SingleValueNavigator(State<ForwardContext> state, BotService botService, Localizer localizer, QuestionService questionService) {
        super(state, botService, localizer);
        this.questionService = questionService;
    }

    @Override
    protected Transition<ForwardContext, QuestionContext> check(Transition<ForwardContext, QuestionContext> transition, Session session, QuestionContext context, Message message, ResourceBundle resourceBundle) {
        if (questionService.find(context.getChatId(), context.getPostId()) == null) {
            transition = Transition.cancel();
            botService.sendMessage(session.getUserId(), localizer.localize(Entry.QUESTION_ABSENT, resourceBundle));
        }
        return transition;
    }
}
