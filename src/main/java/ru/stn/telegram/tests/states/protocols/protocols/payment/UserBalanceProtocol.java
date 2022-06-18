package ru.stn.telegram.tests.states.protocols.protocols.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.stn.telegram.tests.states.entities.Channel;
import ru.stn.telegram.tests.states.entities.sessions.Session;
import ru.stn.telegram.tests.states.localization.Entry;
import ru.stn.telegram.tests.states.localization.Localizer;
import ru.stn.telegram.tests.states.protocols.basics.BaseNavigator;
import ru.stn.telegram.tests.states.protocols.basics.BaseProtocol;
import ru.stn.telegram.tests.states.protocols.states.comment.CommentState;
import ru.stn.telegram.tests.states.protocols.states.forward.PrivateForwardState;
import ru.stn.telegram.tests.states.services.AnswerService;
import ru.stn.telegram.tests.states.services.BotService;
import ru.stn.telegram.tests.states.services.ChannelService;

import java.util.ResourceBundle;

@Component("UserBalanceProtocol")
public class UserBalanceProtocol extends BaseProtocol<UserBalanceContext> {
    @Autowired
    private AnswerService answerService;
    @Autowired
    private ChannelService channelService;

    public UserBalanceProtocol(
            PrivateForwardState forwardState,
            CommentState commentState,
            BotService botService,
            Localizer localizer
    ) {
        super(
                new BaseNavigator<>(forwardState, botService, localizer),
                new BaseNavigator<>(commentState, botService, localizer)
        );
    }

    @Override
    public void commit(Session session, UserBalanceContext context, ResourceBundle resourceBundle) {
        int balance = answerService.getChatBalance(context.getChatId(), context.getOtherUserId());
        Channel channel = channelService.find(context.getChatId());
        botService.sendMessage(
                session.getUserId(),
                String.format(
                        localizer.localize(Entry.USER_BALANCE_FORMAT, resourceBundle),
                        channelService.getValueInCurrency(channel, balance)
                )
        );
    }
}
