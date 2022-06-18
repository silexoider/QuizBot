package ru.stn.telegram.tests.states.protocols.protocols.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.stn.telegram.tests.states.entities.Channel;
import ru.stn.telegram.tests.states.entities.sessions.Session;
import ru.stn.telegram.tests.states.localization.Entry;
import ru.stn.telegram.tests.states.localization.Localizer;
import ru.stn.telegram.tests.states.protocols.basics.BaseNavigator;
import ru.stn.telegram.tests.states.protocols.basics.BaseProtocol;
import ru.stn.telegram.tests.states.protocols.states.forward.PublicForwardState;
import ru.stn.telegram.tests.states.services.AnswerService;
import ru.stn.telegram.tests.states.services.BotService;
import ru.stn.telegram.tests.states.services.ChannelService;

import java.util.ResourceBundle;

@Component("OwnBalanceProtocol")
public class OwnBalanceProtocol extends BaseProtocol<OwnBalanceContext> {
    @Autowired
    private AnswerService answerService;
    @Autowired
    private ChannelService channelService;

    public OwnBalanceProtocol(
            PublicForwardState forwardState,
            BotService botService,
            Localizer localizer
    ) {
        super(
                new BaseNavigator<>(forwardState, botService, localizer)
        );
    }

    @Override
    public void commit(Session session, OwnBalanceContext context, ResourceBundle resourceBundle) {
        int balance = answerService.getChatBalance(context.getChatId(), session.getUserId());
        Channel channel = channelService.find(context.getChatId());
        botService.sendMessage(
                session.getUserId(),
                String.format(
                        localizer.localize(Entry.OWN_BALANCE_FORMAT, resourceBundle),
                        channelService.getValueInCurrency(channel, balance)
                )
        );
    }
}
