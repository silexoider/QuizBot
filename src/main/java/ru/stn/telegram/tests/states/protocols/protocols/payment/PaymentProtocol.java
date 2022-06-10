package ru.stn.telegram.tests.states.protocols.protocols.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.stn.telegram.tests.states.entities.Answer;
import ru.stn.telegram.tests.states.entities.Question;
import ru.stn.telegram.tests.states.entities.sessions.Session;
import ru.stn.telegram.tests.states.localization.Entry;
import ru.stn.telegram.tests.states.localization.Localizer;
import ru.stn.telegram.tests.states.protocols.basics.BaseNavigator;
import ru.stn.telegram.tests.states.protocols.basics.BaseProtocol;
import ru.stn.telegram.tests.states.protocols.protocols.question.QuestionContext;
import ru.stn.telegram.tests.states.protocols.states.amount.PayAmountState;
import ru.stn.telegram.tests.states.protocols.states.comment.CommentState;
import ru.stn.telegram.tests.states.protocols.states.forward.ForwardState;
import ru.stn.telegram.tests.states.services.AnswerService;
import ru.stn.telegram.tests.states.services.BotService;
import ru.stn.telegram.tests.states.services.SessionService;

import java.util.ResourceBundle;

@Component("PaymentProtocol")
public class PaymentProtocol extends BaseProtocol<PaymentContext> {
    @Autowired
    private AnswerService answerService;

    public PaymentProtocol(
            ForwardState forwardState,
            CommentState commentState,
            PayAmountState payAmountState,
            BotService botService,
            Localizer localizer
    ) {
        super(
                new BaseNavigator<>(forwardState, botService, localizer),
                new BaseNavigator<>(commentState, botService, localizer),
                new BaseNavigator<>(payAmountState, botService, localizer)
        );
    }

    @Override
    public void commit(Session session, PaymentContext context, ResourceBundle resourceBundle) {
        Answer answer = answerService.get(context.getChatId(), context.getPostId(), context.getUserId());
        answerService.pay(answer, context.getPayAmount());
        super.commit(session, context, resourceBundle);
    }
}
