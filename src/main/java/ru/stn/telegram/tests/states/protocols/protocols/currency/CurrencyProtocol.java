package ru.stn.telegram.tests.states.protocols.protocols.currency;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.stn.telegram.tests.states.entities.Channel;
import ru.stn.telegram.tests.states.entities.Question;
import ru.stn.telegram.tests.states.entities.sessions.CurrencySession;
import ru.stn.telegram.tests.states.entities.sessions.Session;
import ru.stn.telegram.tests.states.localization.Entry;
import ru.stn.telegram.tests.states.localization.Localizer;
import ru.stn.telegram.tests.states.protocols.basics.BaseNavigator;
import ru.stn.telegram.tests.states.protocols.basics.BaseProtocol;
import ru.stn.telegram.tests.states.protocols.protocols.question.QuestionContext;
import ru.stn.telegram.tests.states.protocols.states.dual.DualState;
import ru.stn.telegram.tests.states.protocols.states.forward.ForwardState;
import ru.stn.telegram.tests.states.protocols.states.forward.PrivateForwardState;
import ru.stn.telegram.tests.states.protocols.states.plural.PluralState;
import ru.stn.telegram.tests.states.protocols.states.singular.SingularState;
import ru.stn.telegram.tests.states.services.BotService;
import ru.stn.telegram.tests.states.services.ChannelService;
import ru.stn.telegram.tests.states.services.SessionService;

import java.util.Currency;
import java.util.ResourceBundle;

@Component("CurrencyProtocol")
public class CurrencyProtocol extends BaseProtocol<CurrencyContext> {
    @Autowired
    private ChannelService channelService;

    public CurrencyProtocol(
            PrivateForwardState forwardState,
            SingularState singularState,
            DualState dualState,
            PluralState pluralState,
            BotService botService,
            Localizer localizer
    ) {
        super(
                new BaseNavigator<>(forwardState, botService, localizer),
                new BaseNavigator<>(singularState, botService, localizer),
                new BaseNavigator<>(dualState, botService, localizer),
                new BaseNavigator<>(pluralState, botService, localizer)
        );
    }

    @Override
    public void commit(Session session, CurrencyContext context, ResourceBundle resourceBundle) {
        Channel channel = channelService.get(context.getChatId());
        channelService.setCurrency(channel, context.getSingular(), context.getDual(), context.getPlural());
        super.commit(session, context, resourceBundle);
    }
}
